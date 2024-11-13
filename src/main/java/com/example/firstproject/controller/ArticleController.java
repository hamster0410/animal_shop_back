package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleFormDTO;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticle(){
        return "article/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleFormDTO form){
        log.info(form.toString());
//        1. DTO를 엔티티로 전환
        Article article = form.toEntity();
        log.info(article.toString());

        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        return "";
    }


}
