package me.leehoseung.service;

import lombok.RequiredArgsConstructor;
import me.leehoseung.domain.Article;
import me.leehoseung.dto.AddArticleRequest;
import me.leehoseung.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(AddArticleRequest request) {
        return articleRepository.save(request.toEntity());
    }
}
