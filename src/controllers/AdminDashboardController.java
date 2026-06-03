
package controllers;

import config.connection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML private BorderPane adminRoot;
    @FXML private VBox contentArea;
    
    @FXML private Label totalOrgsLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalFamiliesLabel;
    @FXML private Label servedFamiliesLabel;
    @FXML private Label unservedFamiliesLabel;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        
        try (Connection conn = connection.getInstance().getConnection()) {
            
            
            totalOrgsLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM organizations")));
            
            
            totalUsersLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM users WHERE role = 'Coordinator'")));
            
            
            int totalFamilies = getCount(conn, "SELECT COUNT(*) FROM families");
            totalFamiliesLabel.setText(String.valueOf(totalFamilies));
            
            int servedFamilies = getCount(conn, "SELECT COUNT(DISTINCT family_id) FROM aid_distributions");
            servedFamiliesLabel.setText(String.valueOf(servedFamilies));
            
           
            int unservedFamilies = totalFamilies - servedFamilies;
            unservedFamiliesLabel.setText(String.valueOf(unservedFamilies));
            
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to load dashboard statistics.");
        }
    }

   
    private int getCount(Connection conn, String query) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    
    @FXML
    private void showDashboardView(ActionEvent event) {
        
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/views/admin_dashboard.fxml"));
            adminRoot.setCenter(((BorderPane) view).getCenter());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void showUserManagementView(ActionEvent event) {
        loadSubView("/views/user_management.fxml");
    }

    @FXML
    private void showOrgManagementView(ActionEvent event) {
        loadSubView("/views/organization_management.fxml");
    }

    @FXML
    private void showFamilyManagementView(ActionEvent event) {
        loadSubView("/views/family_management.fxml");
    }

    @FXML
    private void showAidDistributionView(ActionEvent event) {
        loadSubView("/views/distribution_management.fxml");
    }

    @FXML
    private void showChangePasswordView(ActionEvent event) {
        loadSubView("/views/change_password.fxml");
    }

    private void loadSubView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            adminRoot.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not load screen: " + fxmlPath);
        }
    }

   
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/mainScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Unified Aid Management System - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}