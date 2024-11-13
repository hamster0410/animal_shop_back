package com.example.firstproject.entity;

import jakarta.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;


    public Article(Object o, String title, String content) {
        this.title = title;
        this.content =content;
    }
}

