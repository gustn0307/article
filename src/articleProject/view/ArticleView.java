package articleProject.view;

import articleProject.dto.ArticleDto;
import articleProject.dto.CommentDto;
import articleProject.service.ArticleService;
import articleProject.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArticleView {
    private final Scanner sc;
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleView(Scanner sc, ArticleService articleService, CommentService commentService) {
        this.sc = sc;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    // 전체 게시글 보기(댓글 포함)
    public void showAll() {
        List<ArticleDto> articles = new ArrayList<>();
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

        List<ArticleDto> articles = new ArrayList<>();
        ArticleDto articleDto = new ArticleDto(name, title, content);

        articleService.newArticle(articleDto);
        articles.add(articleDto);
    }

    public void showDetail() {
        List<ArticleDto> articles = new ArrayList<>();
        articles = articleService.all();
        if (!articles.isEmpty()) { // 게시글 목록이 비어있는지 확인
            Long id = -1L;
            do {
                System.out.print("확인할 게시글의 아이디를 입력하세요. ");
                id = sc.nextLong();
                sc.nextLine(); // 버퍼 비우기
            } while (id < 1 || id > articles.get(articles.size() - 1).getId());

            for (ArticleDto articleDto1 : articles) {  // 입력받은 아이디가 article 목록에 있는지 확인
                if (articleDto1.getId().equals(articleService.detail(id).getId())) {

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
                    } else {
                        System.out.println("해당 게시글에는 댓글이 없습니다.");
                    }

                    int cChoice = -1; // Comment 기능 선택 입력받기 위한 변수

                    do {
                        System.out.println("1.댓글입력  2.댓글수정  3.댓글삭제  4.돌아가기");
                        System.out.print("> ");
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
                            if (!articleDto.getCommentList().isEmpty()) { // 댓글 목록이 비어있는지 확인
                                System.out.print("수정할 댓글 번호 입력: ");
                                Long updateCommentId = sc.nextLong();
                                sc.nextLine(); // 버퍼 비우기
                                String originName = "";

                                // 수정 전 현재 정보 보여주기
                                for (CommentDto commentDto1 : articleDto.getCommentList()) {
                                    if (commentDto1.getCommentId().equals(updateCommentId)) {
                                        originName = commentDto1.getName();
                                        System.out.println("수정 전 내용: " + commentDto1.getContent());
                                    }
                                }

                                // 수정할 내용 입력 받기
                                System.out.print("내용 수정 입력: ");
                                String updateContent = sc.nextLine();

                                CommentDto updateCommentDto = new CommentDto(updateCommentId, articleDto.getId(), originName, updateContent);
                                commentService.commentUpdate(updateCommentDto);

                                System.out.println(updateCommentId + " 번 댓글이 수정되었습니다.");
                                break;
                            }
                            System.out.println("수정할 댓글이 없습니다.");
                            break;
                        case 3: // 댓글 삭제
                            if (!articleDto.getCommentList().isEmpty()) { // 댓글 목록이 비어있는지 확인
                                System.out.print("삭제할 댓글 번호 입력: ");
                                Long deleteCommentId = sc.nextLong();
                                sc.nextLine(); // 버퍼 비우기

                                commentService.commentDelete(deleteCommentId);
                                System.out.println(deleteCommentId + " 번 댓글이 삭제되었습니다.");
                                break;
                            }
                            System.out.println("삭제할 댓글이 없습니다.");
                            break;
                        case 4: // 돌아가기
                            return;
                        default: // 위의 do-while문때문에 cChoice값은 0~5 중 하나이므로 default 필요없음(생략가능)
                    }
                }
            }
        } else {
            System.out.println("게시글이 없습니다");
        }
    }

    public void showDelete() {
        List<ArticleDto> articles = new ArrayList<>();
        articles = articleService.all();
        if (!articles.isEmpty()) {  // 게시글 목록이 비어있는지 확인
            showAll(); // 삭제 전에 기존 게시들 모두 보여주기

            Long deleteId = -1L;

            do { // 입력 제대로 받기
                System.out.print("삭제할 게시물 ID: ");
                deleteId = sc.nextLong();
            } while (deleteId < 1L || deleteId > articles.get(articles.size() - 1).getId());

            for (ArticleDto articleDto : articles) { // 게시글 목록에 입력받은 id를 가진 ArticleDto가 있는지 확인
                if (articleDto.getId().equals(deleteId)) {
                    articleService.delete(deleteId);
                    articles.remove(articleDto);
                    System.out.println("삭제됐습니다.");
                    return;
                }
            }
            System.out.println("실패했습니다.");
        } else {
            System.out.println("게시글이 없습니다.");
        }
    }

    public void showUpdate() {
        List<ArticleDto> articles = new ArrayList<>();
        articles = articleService.all();
        if (!articles.isEmpty()) {  // 게시글 목록이 비어있는지 확인
            showAll(); // 수정 전에 기존 게시들 모두 보여주기

            Long updateId = -1L;

            // 입력 제대로 받기 (게시글 목록이 존재하는 것이니 1보다 작으면 안되고
            // 중간에 삽입되지 않고 id값은 변하지 않으므로 게시글 목록 마지막의 id값보다 작은 값을 받아야 함
            do {
                System.out.print("수정할 게시물 ID: ");
                updateId = sc.nextLong();
                sc.nextLine(); // 버퍼 비우기
            } while (updateId < 1L || updateId > articles.get(articles.size() - 1).getId());

            for (ArticleDto articleDto : articles) { // 게시글 목록에 입력받은 id를 가진 ArticleDto가 있는지 확인
                if (articleDto.getId().equals(updateId)) {
                    System.out.println("현재 제목: " + articleDto.getTitle());
                    System.out.println("현재 내용: "+articleDto.getContent());
                    System.out.println();
                    System.out.print("수정할 제목: ");
                    String updateTitle = sc.nextLine();
                    System.out.println("수정할 내용: ");
                    String updateContent = sc.nextLine();
                    ArticleDto updateArticleDto = new ArticleDto(updateId,
                            articleDto.getName(),
                            updateTitle,
                            updateContent,
                            articleDto.getInsertedDate(),
                            articleDto.getUpdatedDate());
                    articleService.update(updateArticleDto); // DB에 수정 쿼리 날리기
                    articles.set(articles.indexOf(articleDto), updateArticleDto); // articles 수정 ## 추후 연동되는거 다 지우기
                    System.out.println("수정됐습니다.");
                    return;
                }
            }
            System.out.println("실패했습니다.");
        } else {
            System.out.println("게시글이 없습니다.");
        }
    }
}