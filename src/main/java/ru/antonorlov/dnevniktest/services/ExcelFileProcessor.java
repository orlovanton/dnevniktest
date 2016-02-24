package ru.antonorlov.dnevniktest.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.antonorlov.dnevniktest.exception.DocumentError;
import ru.antonorlov.dnevniktest.exception.DocumentProcessingException;
import ru.antonorlov.dnevniktest.model.Document;
import ru.antonorlov.dnevniktest.model.DocumentRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

@Service
public class ExcelFileProcessor implements FileProcessor {

    private static final Logger logger = LoggerFactory
            .getLogger(ExcelFileProcessor.class);
    @Autowired
    private DocumentService service;

    @Override
    public void processFile(final File file) throws DocumentProcessingException {
        final String name = file.getName();
        if (name.contains(".xlsx")) {
            processXlsxFile(file);
        } else {
            throw new DocumentProcessingException(new DocumentError(DocumentError.Message.WRONG_FILE_FORMAT_ERROR));
        }
        //удаляем обработанный файл
        boolean delete = file.delete();
        if (!delete) {
            logger.error("Fail to delete processed file {}", file.getName());
        } else {
            logger.info("Processed file {} deleted", file.getName());
        }
    }

    /**
     * Обработка .xslx файла
     *
     * @param file входной файл
     * @return сохраненный в БД документ
     * @throws DocumentProcessingException ошибка обработки доку-та
     */
    private Document processXlsxFile(final File file) throws DocumentProcessingException {
        final Document document;
        List<DocumentError> errorList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<DocumentRow> list = new ArrayList<>();
            document = new Document(file.getName(), new Date(), list);
            Iterator<Row> rowIterator = sheet.iterator();

            Integer startRow = null;

            /*
            Правила обработки док-та
            1. Документ начинается с щапки? где первая ячейка содержит слово "код"
            2. Все поля док-та(Код,Наименование,Цена,Дата) обязательны
            3. Документ заканчивается на строке с ячейкой с оствутствующим значением в столбце код
             */
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isHeaderRow(row)) {
                    break;
                }
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (startRow == null) {
                    startRow = row.getRowNum();
                }
                DocumentRow documentRow = new DocumentRow();
                final int numOfCells = row.getPhysicalNumberOfCells();
                if (numOfCells >= 4) {

                    final Cell codeCell = row.getCell(0);
                    final Cell nameCell = row.getCell(1);
                    final Cell priceCell = row.getCell(2);
                    final Cell dateCell = row.getCell(3);

                    if (Cell.CELL_TYPE_NUMERIC == codeCell.getCellType()) {
                        final Double code = codeCell.getNumericCellValue();
                        documentRow.setCode(code.intValue());
                    } else {
                        //если это первая строка посде шапки, считаем что док-т пустой
                        if (row.getRowNum() == startRow.intValue()) {
                            logger.error("Empty document");
                            errorList.add(new DocumentError(
                                    DocumentError.Message.EMPTY_OR_WRONG_FORMATED_DOCUMENT_ERROR));
                        }
                        // не первая - просто конец док-та
                        break;
                    }

                    if (Cell.CELL_TYPE_STRING == nameCell.getCellType()) {
                        final String name = nameCell.getStringCellValue();
                        documentRow.setName(name);
                    } else {
                        logger.error("Price sell incorrect. Skipping row");
                        errorList.add(new DocumentError(
                                DocumentError.Message.CELL_ERROR,
                                row.getRowNum(), nameCell.getColumnIndex()));
                    }

                    if (Cell.CELL_TYPE_STRING == priceCell.getCellType()) {

                        final String priceStr = priceCell.getStringCellValue();
                        try {
                            final Double price = Double.valueOf(priceStr);
                            documentRow.setPrice(price);
                        } catch (NumberFormatException ex) {
                            errorList.add(new DocumentError(
                                    DocumentError.Message.CELL_ERROR,
                                    row.getRowNum(), priceCell.getColumnIndex()));
                            logger.error("Price sell incorrect. Skipping row");
                        }

                    } else if (Cell.CELL_TYPE_NUMERIC == priceCell.getCellType()) {
                        final double price = priceCell.getNumericCellValue();
                        documentRow.setPrice(price);
                    } else {
                        errorList.add(new DocumentError(
                                DocumentError.Message.CELL_ERROR,
                                row.getRowNum(), priceCell.getColumnIndex()));
                        logger.error("Price sell incorrect. Skipping row");
                    }

                    if (Cell.CELL_TYPE_NUMERIC == dateCell.getCellType()) {
                        final Date date = dateCell.getDateCellValue();
                        documentRow.setDate(date);

                    } else {
                        errorList.add(new DocumentError(
                                DocumentError.Message.CELL_ERROR,
                                row.getRowNum(), dateCell.getColumnIndex()));
                        logger.error("Date sell incorrect. Skipping row");
                    }

                    list.add(documentRow);
                } else {
                    errorList.add(new DocumentError(
                            DocumentError.Message.ROW_ERROR,
                            row.getRowNum()));
                    logger.error("Row has less than 4 cells");
                }
            }
        } catch (IOException ex) {
            logger.error("Error processing file " + file.getName(), ex);
            errorList.add(new DocumentError(DocumentError.Message.DOCUMENT_PROCESSING_ERROR));
            throw new DocumentProcessingException(errorList);
        }
        if (errorList.isEmpty()) {
            return service.saveDocument(document);
        } else {
            throw new DocumentProcessingException(errorList);
        }
    }

    /**
     * Повека на то что строка - это шапка док-та
     * критерий - первая ячейка содержить слово "код"
     *
     * @param row сторка
     * @return является ли строка шапкой
     */
    private boolean isHeaderRow(final Row row) {
        if (row.getPhysicalNumberOfCells() >= 4) {
            Cell cell = row.getCell(0);
            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                String stringCellValue = cell.getStringCellValue();
                if (stringCellValue != null && stringCellValue
                        .trim().toLowerCase().equals("код")) {
                    return true;
                }
            }
        }
        return false;
    }
}
