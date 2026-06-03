package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import config.connection;
import java.sql.*;
import javafx.scene.control.Alert;

public class ProfileController {
    
    @FXML private TextField userIdField, roleField, orgIdField, fullNameField, emailField;

    private int userId = 1; 

    @FXML
    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        try (Connection conn = connection.getInstance().getConnection()) {
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                userIdField.setText(String.valueOf(rs.getInt("user_id")));
                roleField.setText(rs.getString("role"));
                orgIdField.setText(String.valueOf(rs.getInt("organization_id")));
                fullNameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    @FXML
    private void handleUpdateProfile() {
        try (Connection conn = connection.getInstance().getConnection()) {
           
            String sql = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, fullNameField.getText());
            ps.setString(2, emailField.getText());
            ps.setInt(3, userId); 
            
            int rowsAffected = ps.executeUpdate();
        
            if (rowsAffected > 0) {
               
                showInfo("Success", "Profile updated successfully!");
            }
            System.out.println("Profile Updated Successfully!");
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    private void showInfo(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
}