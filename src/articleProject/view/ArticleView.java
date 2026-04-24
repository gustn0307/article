package articleProject.view;

import articleProject.dto.ArticleDto;
import articleProject.dto.CommentDto;
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

    // 전체 게시글 보기(댓글 포함)
    public void showAll() {
        articles = articleService.all();
        System.out.println("리스트확인: "+articles);
        if (!articles.isEmpty()) { // 게시글 목록이 비어있는지 확인
            System.out.println("=============================================");
            System.out.println("id      name         title         작성일");
            System.out.println("=============================================");
            for (ArticleDto articleDto : articles){
                System.out.println(articleDto.toString());
                if (!articleDto.getCommentList().isEmpty()){ // 댓글이 있는지 확인
                    for (CommentDto commentDto : articleDto.getCommentList()) {
                        System.out.println("\t" + commentDto.toString());
                    }
                }
            }
            System.out.println("=============================================");
            return;
        }
        System.out.println("게시글이 없습니다.");
    }

    public void showNewArticle() {
        // ## 추후 validation 클래스 추가해서 입력 예외 처리 기능 추가하기 ##
        System.out.println("새글 입력창입니다.");
        System.out.print("작성자: ");
        String name = sc.nextLine();
        System.out.print("제목: ");
        String title = sc.nextLine();
        System.out.print("내용: ");
        String content = sc.nextLine();

        ArticleDto articleDto = new ArticleDto(name, title, content);

        articleService.newArticle(articleDto);
    }

    public void showDetail() {
    }

    public void showDelete() {
    }

    public void showUpdate() {
    }
}
