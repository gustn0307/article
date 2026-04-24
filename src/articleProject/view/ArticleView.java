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
        
        if (!articles.isEmpty()) { // 게시글 목록이 비어있는지 확인
            System.out.println("=============================================");
            System.out.println("id      name         title         작성일");
            System.out.println("=============================================");
            for (ArticleDto articleDto : articles) {
                System.out.println(articleDto.toString());
                if (!articleDto.getCommentList().isEmpty()) { // 댓글이 있는지 확인
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
        System.out.print("확인할 게시글의 아이디를 입력하세요. ");
        Long id = sc.nextLong();
        sc.nextLine(); // 버퍼 비우기

        ArticleDto articleDto = new ArticleDto();
        articleDto = articleService.detail(id);

        System.out.println("🚀 ID          : " + articleDto.getId());
        System.out.println("🚀 Name    : " + articleDto.getName());
        System.out.println("🚀 Title       : " + articleDto.getTitle());
        System.out.println("🚀 Content : " + articleDto.getContent());
        System.out.println("🚀 작성일     : " + articleDto.getInsertedDate());
        System.out.println("🚀 수정일     : " + articleDto.getUpdatedDate());
        System.out.println();
        System.out.println("🎶🎶  댓글 리스트  🎶🎶");
        if (!articleDto.getCommentList().isEmpty()) {
            for (CommentDto commentDto : articleDto.getCommentList()) {
                System.out.println(commentDto);
            }
        }else {
            System.out.println("해당 게시글에는 댓글이 없습니다.");
        }

        int cChoice = -1; // Comment 기능 선택 입력받기 위한 변수

        do {
            System.out.println("1.댓글입력  2.댓글수정  3.댓글삭제  4.돌아가기");
            cChoice = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기
            if (cChoice < 1 || cChoice > 4) {
                System.out.println("1 ~ 4 중 하나를 입력해주세요.");
            }
        } while (cChoice < 1 || cChoice > 4);

        switch (cChoice) {
            case 1: // 댓글 입력
                System.out.print("댓글 등록자 이름: ");
                String name = sc.nextLine();
                System.out.print("댓글 내용: ");
                String content = sc.nextLine();
                CommentDto commentDto = new CommentDto(null, articleDto.getId(), name, content);
                commentService.commentAdd(commentDto);
                break;
            case 2: // 댓글 수정
                break;
            case 3: // 댓글 삭제
                break;
            case 4: // 돌아가기
                return;
            default: // 위의 do-while문때문에 cChoice값은 0~5 중 하나이므로 default 필요없음(생략가능)
        }
    }

    public void showDelete() {
    }

    public void showUpdate() {
    }
}
