package articleProject.view;

import articleProject.dto.ArticleDto;
import articleProject.service.ArticleService;
import articleProject.service.CommentService;

import java.util.List;
import java.util.Scanner;

public class ArticleView {
    private final Scanner sc;
    private final ArticleService articleService;
    private final CommentService commentService;
    private List<ArticleDto> articles;

    public ArticleView(Scanner sc, ArticleService articleService, CommentService commentService) {
        this.sc = sc;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    public void showAll() {
    }

    public void showNewArticle() {
    }

    public void showDetail() {
    }

    public void showDelete() {
    }

    public void showUpdate() {
    }
}
