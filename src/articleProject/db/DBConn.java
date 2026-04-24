package articleProject.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 프로젝트 시작할 때 DB 연결을 구성
// 딱 한 번만 연결을 함(Singleton 패턴)
// 종료시 연결 해제
// 연결을 static으로 생성
public class DBConn {
    private static Connection dbConn;

    // DB 연결
    public static Connection getConnection() {
        if (dbConn == null) { // 이미 연결이 되어있지 않다면 연결
            try {
                String dbDriver = "com.mysql.jdbc.Driver"; // 드라이버 경로
                String dbUrl = "jdbc:mysql://localhost:3306/article_db"; // DB 경로
                String dbUser = "root"; // 유저 아이디
                String dbPassword = "1111"; // 유저 비밀번호

                Class.forName(dbDriver); // 드라이버 클래스를 메모리로 가져온다.
                dbConn = DriverManager.getConnection(dbUrl, dbUser, dbPassword); // DB 연결 생성
                System.out.println("DB 연결 성공~~~");
            } catch (ClassNotFoundException e) {
                System.out.println("드라이버가 없어요. 연결 실패");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("DB 연결 실패");
                e.printStackTrace();
            }
        }
        return dbConn;
    }

    // DB 연결 종료시 처리
    public static void close() {
        if (dbConn != null) { // DB 연결이 되어있다면
            try {
                if (!dbConn.isClosed()) { // 아직 연결이 닫히지 않았다면
                    dbConn.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        dbConn = null;
    }
}
