package ru.antonorlov.dnevniktest.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

/**
 * Документ загруженный на сервер
 */
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * Имя док-та
     */
    @Column(nullable = false)
    private String name;

    /**
     * Дата загрузки док-та
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    /**
     * Строки док-та
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
//    mappedBy = "document")
    private List<DocumentRow> documentRows;

    public Document() {
    }

    public Document(final String name, final Date uploadDate, final List<DocumentRow> documentRows) {
        this.name = name;
        this.uploadDate = uploadDate;
        this.documentRows = documentRows;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(final Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public List<DocumentRow> getDocumentRows() {
        return documentRows;
    }

    public void setDocumentRows(final List<DocumentRow> documentRows) {
        this.documentRows = documentRows;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Document{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", uploadDate=").append(uploadDate);
        sb.append(", rows=").append(documentRows);
        sb.append('}');
        return sb.toString();
    }
}
