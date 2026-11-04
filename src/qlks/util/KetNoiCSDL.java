package qlks.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiCSDL {
    private static final String URL = "jdbc:mysql://localhost:3306/qlks_datphong?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Khong tim thay driver MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void dong(AutoCloseable... ds) {
        for (AutoCloseable d : ds) {
            if (d != null) {
                try { d.close(); } catch (Exception ignored) {}
            }
        }
    }
}
