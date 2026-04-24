package articleProject.service;

import articleProject.dto.ArticleDto;
import articleProject.repository.Repository;

import java.util.List;

public class ArticleService {
    private final Repository articleRepository;

    public ArticleService(Repository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // articleRepository에게 전달
    public List<ArticleDto> all() {
        return articleRepository.all();
    }
}
