package articleProject.repository;

import articleProject.dto.ArticleDto;
import articleProject.dto.CommentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final Connection conn; // DB 연결

    public Repository(Connection conn) {
        this.conn = conn;
    }

    // 각 Article마다 'SELECT * FROM comments WHERE article_id=?' 쿼리 실행
    // 결과를 article.addComments(comment) 로 추가
    // 최종 ArticleDto List 반환
    // => DB에서 article의 댓글들을 불러와 ArticleDto 객체의 CommentList에 추가해주고
    // 해당 객체를 ArticleDto List에  추가해주고 댓글이 추가된 ArticleDto 객체들을 가진 ArticleDto List를 반환
    private List<ArticleDto> getArticleComments(List<ArticleDto> articleDtoList) { // 널이면 빈 댓글리스트
        PreparedStatement psmt = null; // 쿼리를 실행할 도구
        ResultSet rs = null; // 레코드 셋 결과를 담을 도구

        try {
            for (ArticleDto articleDto : articleDtoList) {
                String sql = "SELECT * FROM comments WHERE article_id=?";
                psmt = conn.prepareStatement(sql);
                psmt.setLong(1, articleDto.getId());

                rs = psmt.executeQuery();

                // rs에 들어간 결과를 addComments() 메서드로 CommentDto의 CommentList 리스트에 담는다.
                while (rs.next()) { // 다음 레코드가 있으면 반복문 수행
                    CommentDto commentDto = new CommentDto(
                            rs.getLong("comment_id"),
                            rs.getLong("article_id"),
                            rs.getString("name"),
                            rs.getString("content")
                    ); // DB에서 읽어온 정보들을 가진 CommentDto 객체 생성
                    articleDto.addComments(commentDto); // DB에서 읽어온 정보들을 가진 CommentDto를 articleDto의 CommentList에 추가
                }
            }
        } catch (Exception e) {
            System.out.println("getArticleComments() 오류: " + e.getMessage());
        }

        return articleDtoList;
    }

    // SELECT * FROM article ORDER BY id DESC 쿼리로 article들 불러오고
    // 결과를 Article 리스트로 변환
    // 마지막에 getArticleComments(articles) 호출 — 각 게시글에 댓글 로드
    public List<ArticleDto> all() {
        List<ArticleDto> articleList = new ArrayList<>();
        PreparedStatement psmt = null; // 쿼리를 실행할 도구
        ResultSet rs = null; // 레코드 셋 결과를 담을 도구

        try {
            String sql = "SELECT * FROM article ORDER BY id"; // 쿼리
            psmt = conn.prepareStatement(sql);

            rs = psmt.executeQuery(); // SQL 쿼리 실행 결과를 rs에 받는다 테이블 상태가 바뀌지 않는 쿼리는 executeQuery() 메서드 사용

            // rs에 들어간 결과를 articleList 리스트에 담는다.
            while (rs.next()) { // 다음 레코드가 있으면 반복문 수행
                ArticleDto articleDto = new ArticleDto(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getObject("inserted_date", LocalDateTime.class),
                        rs.getObject("updated_date", LocalDateTime.class)
                ); // 읽어온 레코드를 담을 빈 DTO를 생성
                articleList.add(articleDto); // articleList에 DB에서 읽어온 정보들을 추가한 articleDto 객체 추가
            }
            psmt.close(); // 사용 후 닫아주기
            rs.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("all() 오류: " + e.getMessage());
        }

        return getArticleComments(articleList);
    }

    // 'INSERT INTO article(name, title, content, inserted_date, updated_date) VALUES(?,?,?,?,?)' 쿼리로 article 추가
    public void newArticle(ArticleDto articleDto) {
        PreparedStatement psmt = null; // 쿼리를 실행할 도구

        try {
            String sql = "INSERT INTO article(name, title, content, inserted_date, updated_date) VALUES(?,?,?,?,?)"; // 쿼리
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, articleDto.getName());
            psmt.setString(2, articleDto.getTitle());
            psmt.setString(3, articleDto.getContent());
            psmt.setTimestamp(4, Timestamp.valueOf(articleDto.getInsertedDate()));
            psmt.setTimestamp(5, Timestamp.valueOf(articleDto.getUpdatedDate()));

            psmt.executeUpdate(); // SQL 쿼리 실행(테이블 상태가 바뀌는 쿼리는 executeUpdate() 메서드 사용)
            psmt.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("newArticle() 오류: " + e.getMessage());
        }
    }

    // INSERT INTO comments(name, content, article_id) VALUES(?,?,?) 쿼리 실행
    public void insertComment(CommentDto commentDto) {
        PreparedStatement psmt = null; // 쿼리를 실행할 도구

        try {
            String sql = " INSERT INTO comments(name, content, article_id) VALUES(?,?,?)"; // 쿼리
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, commentDto.getName());
            psmt.setString(2, commentDto.getContent());
            psmt.setLong(3, commentDto.getArticleId());

            psmt.executeUpdate(); // 댓글 추가 SQL 쿼리 실행(테이블 상태가 바뀌는 쿼리는 executeUpdate() 메서드 사용)
            psmt.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("insertComment() 오류: " + e.getMessage());
        }
    }

    // SELECT * FROM article WHERE id=? 쿼리 실행
    public ArticleDto detail(Long id) {
        ArticleDto articleDto = new ArticleDto();
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM article WHERE id=?"; // 해당 ID를 가진 기사 가져오기
            psmt = conn.prepareStatement(sql);
            psmt.setLong(1, id);

            rs = psmt.executeQuery();

            while (rs.next()) {
                articleDto.setId(rs.getLong("id"));
                articleDto.setName(rs.getString("name"));
                articleDto.setTitle(rs.getString("title"));
                articleDto.setContent(rs.getString("content"));
                articleDto.setInsertedDate(rs.getObject("inserted_date", LocalDateTime.class));
                articleDto.setUpdatedDate(rs.getObject("inserted_date", LocalDateTime.class));
            }

            // 해당 기사의 댓글들도 가져오기
            sql = "SELECT * FROM comments WHERE article_id=?";
            psmt = conn.prepareStatement(sql);
            psmt.setLong(1, articleDto.getId());

            rs = psmt.executeQuery();

            // rs에 들어간 결과를 addComments() 메서드로 CommentDto의 CommentList 리스트에 담는다.
            while (rs.next()) { // 다음 레코드가 있으면 반복문 수행
                CommentDto commentDto = new CommentDto(
                        rs.getLong("comment_id"),
                        rs.getLong("article_id"),
                        rs.getString("name"),
                        rs.getString("content")
                ); // DB에서 읽어온 정보들을 가진 CommentDto 객체 생성
                articleDto.addComments(commentDto); // DB에서 읽어온 정보들을 가진 CommentDto를 articleDto의 CommentList에 추가
            }

            psmt.close(); // 사용 후 닫아주기
            rs.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("detail() 오류: " + e.getMessage());
        }
        return articleDto;
    }

    // DELETE FROM comments WHERE comment_id=?
    public void deleteComment(Long commentId) {
        PreparedStatement psmt = null;

        try {
            String sql = "DELETE FROM comments WHERE comment_id=?";
            psmt = conn.prepareStatement(sql);
            psmt.setLong(1, commentId);

            psmt.executeUpdate();  // SQL 쿼리 실행(테이블 상태가 바뀌는 쿼리는 executeUpdate() 메서드 사용)
            psmt.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("deleteComment 오류: " + e.getMessage());
        }
    }

    // UPDATE comments SET content=? WHERE comment_id=?
    public void updateComment(CommentDto updateCommentDto) {
        PreparedStatement psmt = null; // 쿼리를 실행할 도구

        try {
            String sql = "UPDATE comments SET content=? WHERE comment_id=?"; // 쿼리
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, updateCommentDto.getContent());
            psmt.setLong(2, updateCommentDto.getCommentId());

            psmt.executeUpdate(); // 댓글 추가 SQL 쿼리 실행(테이블 상태가 바뀌는 쿼리는 executeUpdate() 메서드 사용)
            psmt.close(); // 사용 후 닫아주기
        } catch (Exception e) {
            System.out.println("updateComment() 오류: " + e.getMessage());
        }
    }
}