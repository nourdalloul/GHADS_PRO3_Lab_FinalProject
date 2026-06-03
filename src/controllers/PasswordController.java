package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import config.connection;
import java.sql.*;

public class PasswordController {
    @FXML private PasswordField currentPassField, newPassField;
    
    private String currentUsername = "nour"; 

    @FXML
    private void handlePasswordChange() {
        String current = currentPassField.getText();
        String newPass = newPassField.getText();

        try (Connection conn = connection.getInstance().getConnection()) {
           
            String query = "SELECT password FROM users WHERE UPPER(TRIM(username)) = UPPER(TRIM(?))";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, currentUsername); 

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (storedPassword.equals(current)) {
                    
                    String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
                    PreparedStatement updatePstmt = conn.prepareStatement(updateQuery);
                    updatePstmt.setString(1, newPass);
                    updatePstmt.setString(2, currentUsername);
                    updatePstmt.executeUpdate();

                    showInfo("Success", "Password updated successfully!");
                   
                } else {
                    showError("Error", "Incorrect current password!");
                }
            } else {
                showError("Error", "Username not found!");
            }
        } catch (SQLException e) {
            showError("Error", "Database error: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title); alert.setContentText(content); alert.show();
    }
    
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setContentText(content); alert.show();
    }
}
