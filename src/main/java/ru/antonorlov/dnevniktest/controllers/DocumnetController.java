package ru.antonorlov.dnevniktest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.antonorlov.dnevniktest.exception.DocumentNotFoundException;
import ru.antonorlov.dnevniktest.model.Document;
import ru.antonorlov.dnevniktest.services.DocumentService;

import java.util.Optional;

/**
 * Created by antonorlov on 23/02/16.
 */
@Controller
public class DocumnetController {

    private static final Logger logger = LoggerFactory
            .getLogger(FileUploadController.class);

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = {"document/{id}"})
    public String getDocument(final Model model,
                              @PathVariable Long id) throws DocumentNotFoundException {
        logger.info("Document page for document with id[{}] called", id);
        Optional<Document> documentOptional = documentService.getDocument(id);
        if (!documentOptional.isPresent()) {
            logger.error("Document with id[{}] not found", id);
            throw new DocumentNotFoundException("Document with id[" + id + "] not found");
        } else {
            model.addAttribute("document", documentOptional.get());
        }

        return "document";
    }
}
