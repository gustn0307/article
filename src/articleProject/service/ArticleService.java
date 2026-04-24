package articleProject.service;

import articleProject.dto.ArticleDto;
import articleProject.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleService {
    private final Repository repository;

    public ArticleService(Repository articleRepository) {
        this.repository = articleRepository;
    }

    // articleRepository에게 전달
    public List<ArticleDto> all() {
        return repository.all();
    }

    public void newArticle(ArticleDto articleDto) {
        articleDto.setInsertedDate(LocalDateTime.now());
        articleDto.setUpdatedDate(LocalDateTime.now());
        repository.newArticle(articleDto);
    }

    public ArticleDto detail(Long id) {
        return repository.detail(id);
    }
}
