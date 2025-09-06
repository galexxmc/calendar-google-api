package com.oefa.servicio_calendario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    public void saveRefreshToken(String userId, String refreshToken) {
        String sql = "INSERT INTO users (user_id, refresh_token) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE refresh_token = VALUES(refresh_token)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, refreshToken);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getRefreshToken(String userId) {
        String sql = "SELECT refresh_token FROM users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("refresh_token");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}