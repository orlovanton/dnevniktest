package ru.antonorlov.dnevniktest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antonorlov.dnevniktest.model.Document;
import ru.antonorlov.dnevniktest.repository.DocumentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory
            .getLogger(DocumentServiceImpl.class);

    @Autowired
    private DocumentRepository repository;

    @Override
    @Transactional
    public Document saveDocument(final Document document) {
        logger.info("Saving document {}", document.getName());
        Document savedDocument = repository.save(document);
        logger.info("Document saved {}" + document.toString());
        return savedDocument;
    }

    @Override
    public Optional<Document> getDocument(final Long documentId) {
        logger.info("Get document with id[{}]", documentId);
        Document doc = repository.findOne(documentId);
        if(doc== null){
            logger.warn("Document not found");
            return Optional.empty();
        }else{
            logger.info("Document found");
            return Optional.of(doc);
        }
    }

    @Override
    public List<Document> getAllDocuments() {
        return repository.findAll();
    }
}
