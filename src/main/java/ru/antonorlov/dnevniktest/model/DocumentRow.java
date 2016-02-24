package ru.antonorlov.dnevniktest.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Author:      oav <br>
 * Date:        22.02.16, 21:43 <br>
 */

/**
 * Строка в документе
 * Т.е. не указаны ограничения на поля док-та,
 * кроме их типа - считаем, что все поня nullable и не имеют ограничений по формату
 */
@Entity
public class DocumentRow {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * Наименование - строка
     */
    @Column
    private Integer code;

    /**
     * Наименование - строка
     */
    @Column
    private String name;

    /**
     * Цена - число с плавающей запятой
     */
    @Column
    private Double price;

    /**
     * Дата - дата и время
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

//    /**
//     * Докумекнт к которому относится строка
//     */
//    @ManyToOne(optional = false)
//    private Document document;

    public DocumentRow() {
    }

    public Long getId() {
        return id;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
