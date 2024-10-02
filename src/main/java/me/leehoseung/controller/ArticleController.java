package me.leehoseung.controller;

import lombok.RequiredArgsConstructor;
import me.leehoseung.domain.Article;
import me.leehoseung.dto.AddArticleRequest;
import me.leehoseung.dto.ArticleResponse;
import me.leehoseung.dto.UpdateArticleRequest;
import me.leehoseung.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = articleService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = articleService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id) {
        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {

        articleService.deleteById(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") long id,
                                                         @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = articleService.update(id, request);

        return ResponseEntity.ok()
                .body(new ArticleResponse(updatedArticle));
    }
}
