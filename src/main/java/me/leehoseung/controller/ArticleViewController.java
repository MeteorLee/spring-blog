package me.leehoseung.controller;

import lombok.RequiredArgsConstructor;
import me.leehoseung.domain.Article;
import me.leehoseung.dto.ArticleListViewResponse;
import me.leehoseung.dto.ArticleViewResponse;
import me.leehoseung.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") long id,
                             Model model) {

        Article article = articleService.findById(id);

        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }


    @GetMapping("/new-article")
    public String newArticle(@RequestParam(name="id", required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
