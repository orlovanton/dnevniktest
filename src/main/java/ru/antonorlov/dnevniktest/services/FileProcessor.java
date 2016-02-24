package ru.antonorlov.dnevniktest.services;

import ru.antonorlov.dnevniktest.exception.DocumentProcessingException;

import java.io.File;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

public interface FileProcessor {

    /**
     * Обработка док-та
     * Сохранинение в БД
     *
     * @param file сохраненный на сервере файл
     * @throws DocumentProcessingException ошибки при обработке
     */
    void processFile(final File file) throws DocumentProcessingException;

}
