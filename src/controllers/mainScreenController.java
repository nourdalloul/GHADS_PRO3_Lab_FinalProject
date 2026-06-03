
package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import config.connection;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class mainScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private BorderPane rootPane;
    
    @FXML
    private void handleLogin(ActionEvent event) throws SQLException {
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please enter both username and password.");
            return;
        }

        String userType = checkCredentials(username, password);

        if (userType == null) {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
        } else {
            if (userType.equalsIgnoreCase("Admin")) {
                switchScreen(event,"/views/admin_dashboard.fxml","Admin Dashboard");
            } else if (userType.equalsIgnoreCase("Coordinator")) {
                switchScreen(event, "/views/coordinator_dashboard.fxml", "Coordinator Dashboard");
            }
        }
    }

    private String checkCredentials(String username, String password) {

        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error during login validation:");
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }

        return null;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   
    private void switchScreen(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Could not load the screen: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    
    @FXML private void handleExit() { System.exit(0); }
    
    @FXML private void handleAbout() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("About Developer");
        info.setContentText("Gaza Humanitarian Aid Distribution System (GHADS) is a desktop application built in Java that helps humanitarian organizations in Gaza coordinate aid distribution for displaced families. Developer Nour Nehad Dalloul ,IT student in the third level in IUG");
        info.showAndWait();
    }
    @FXML
    private void changeFontFamily(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String fontName = item.getText();

        if (item.getParentPopup() != null && item.getParentPopup().getOwnerWindow() != null) {
            Scene scene = item.getParentPopup().getOwnerWindow().getScene();
            scene.getRoot().setStyle("-fx-font-family: '" + fontName + "';");
        }
    }

    @FXML
    private void changeFontSize(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String size = item.getText();

        if (item.getParentPopup() != null && item.getParentPopup().getOwnerWindow() != null) {
            Scene scene = item.getParentPopup().getOwnerWindow().getScene();
            scene.getRoot().setStyle("-fx-font-size: " + size + ";");
        }
}
    @FXML
    private void lightMode(ActionEvent event) {
        BorderPane currentRoot = (BorderPane) ((Node) event.getTarget()).getScene().getRoot();
        currentRoot.setStyle("-fx-background-color: #e3f2fd;");
    }
    
    @FXML
    private void DarkMode(ActionEvent event) {
        BorderPane currentRoot = (BorderPane) ((Node) event.getTarget()).getScene().getRoot();
        currentRoot.setStyle("-fx-background-color: #263238;");
    }
}