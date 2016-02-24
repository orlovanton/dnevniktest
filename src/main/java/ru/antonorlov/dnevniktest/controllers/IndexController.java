package ru.antonorlov.dnevniktest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.antonorlov.dnevniktest.model.Document;
import ru.antonorlov.dnevniktest.services.DocumentService;

import java.util.List;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory
            .getLogger(IndexController.class);

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(final Model model) {
        logger.info("Index page called");
        final List<Document> allDocuments = documentService.getAllDocuments();
        model.addAttribute("documents", allDocuments);
        return "index";
    }

}
