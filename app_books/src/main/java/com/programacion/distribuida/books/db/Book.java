package com.programacion.distribuida.books.db;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "tittle")
    private String tittle;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "author_id")
    private Integer authorId;
}
