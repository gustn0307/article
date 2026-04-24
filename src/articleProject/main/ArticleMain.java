package articleProject.main;

import articleProject.db.DBConn;
import articleProject.service.ArticleService;
import articleProject.service.CommentService;
import articleProject.view.ArticleView;

import java.sql.Connection;
import java.util.Scanner;

public class ArticleMain {
    public static void main(String[] args) {
        Connection connection = DBConn.getConnection(); // DB 연결

        Scanner sc = new Scanner(System.in); // 사용자에게 입력을 받기 위한 Scanner 객체 sc 생성

        // 필요한 인스턴스들 생성
        ArticleService articleService = new ArticleService();
        CommentService commentService = new CommentService();
        ArticleView articleView = new ArticleView(sc, articleService, commentService);

        while (true) {
            int input;
            do {
                System.out.println("0. 전체보기  1. 새글  2. 자세히보기  3. 게시글삭제  4. 수정  5. 종료");
                System.out.print("▶ 메뉴 입력 : ");
                input = sc.nextInt(); // ## 타입 다른 입력했을 때 예외처리 필요 ##
                sc.nextLine(); // 버퍼 비우기
                if (input < 0 || input > 5) {
                    System.out.println("0 ~ 5 중 하나를 입력해주세요.");
                }
            } while (input < 0 || input > 5);

            switch (input) {
                case 0: // 전체보기
                    articleView.showAll();
                    break;
                case 1:
                    articleView.showNewArticle();
                    break;
                case 2:
                    articleView.showDetail();
                    break;
                case 3:
                    articleView.showDelete();
                    break;
                case 4:
                    articleView.showUpdate();
                    break;
                case 5:
                    System.out.println("종료합니다.");
                    DBConn.close();
                    return;
                default:
                    System.out.println("1 ~ 5 중 하나를 입력해주세요");
            }
        }
    }
}
