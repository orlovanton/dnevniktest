package ru.antonorlov.dnevniktest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.antonorlov.dnevniktest.exception.DocumentError;
import ru.antonorlov.dnevniktest.exception.DocumentProcessingException;
import ru.antonorlov.dnevniktest.services.FileProcessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory
            .getLogger(FileUploadController.class);

    @Autowired
    private FileProcessor fileProcessor;

    @Value("${dnevniktest.filestorage.path}")
    private String fileStoragePath;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String provideUploadInfo() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttrs) {
        final String fileName = file.getOriginalFilename();
        logger.info("Uploading file {}", fileName);
        if (!file.isEmpty()) {
            File saveFile;
            try {
                byte[] bytes = file.getBytes();
                saveFile = new File(fileStoragePath + fileName);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(saveFile));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                logger.error("Failed to upload " + fileName, e);
                List<DocumentError> errors = new ArrayList<>();
                errors.add(new DocumentError(DocumentError.Message.GENERAL_ERROR));
                redirectAttrs.addAttribute("errors", errors);
                return "redirect:/index";
            }
            try {
                fileProcessor.processFile(saveFile);
            } catch (DocumentProcessingException ex) {
                redirectAttrs.addFlashAttribute("errors", ex.getErrors());
                return "redirect:/index";
            }
            logger.info("{} successfully uploaded", fileName);
            return "redirect:/index";
        } else {
            logger.error("Failed to upload " + fileName + " because the file was empty.");
            return "redirect:/error";
        }
    }

}
