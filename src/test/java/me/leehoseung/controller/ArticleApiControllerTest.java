package me.leehoseung.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.leehoseung.domain.Article;
import me.leehoseung.dto.AddArticleRequest;
import me.leehoseung.dto.UpdateArticleRequest;
import me.leehoseung.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    public void mockMvcSetUP() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        articleRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    void addArticle() throws Exception {
        // Given
        final String url = "/api/articles";
        final String title = "제목";
        final String content = "내용";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // When
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        result
                .andExpect(status().isCreated());

        List<Article> articles = articleRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    void findAllArticles() throws Exception {
        // Given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // When
        ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));

    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    void findArticle() throws Exception {
        // Given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        System.out.println(savedArticle.getId());

        // When
        ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));

    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    void deleteArticle() throws Exception {
        // Given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // When
        ResultActions result = mockMvc.perform(delete(url, savedArticle.getId()));

        // Then
        result
                .andExpect(status().isOk());

        assertThat(articleRepository.findById(savedArticle.getId())).isEmpty();

    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    void updateArticle() throws Exception {
        // Given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "newTitle";
        final String newContent = "newContent";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // When
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then
        result
                .andExpect(status().isOk());

        Article article = articleRepository.findById(savedArticle.getId()).orElseThrow();

        assertThat(article.getTitle()).isEqualTo(request.getTitle());
        assertThat(article.getContent()).isEqualTo(request.getContent());

    }
}