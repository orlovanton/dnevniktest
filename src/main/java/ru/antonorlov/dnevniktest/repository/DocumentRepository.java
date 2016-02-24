package ru.antonorlov.dnevniktest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.antonorlov.dnevniktest.model.Document;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
