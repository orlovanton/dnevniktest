package ru.antonorlov.dnevniktest.services;

import ru.antonorlov.dnevniktest.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */
public interface DocumentService {

    Document saveDocument(final Document document);

    Optional<Document> getDocument(final Long documentId);

    List<Document> getAllDocuments();

}
