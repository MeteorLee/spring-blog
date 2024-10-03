package me.leehoseung.controller;

import lombok.RequiredArgsConstructor;
import me.leehoseung.dto.ArticleListViewResponse;
import me.leehoseung.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = articleService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);

        return "articleList";

    }
}
