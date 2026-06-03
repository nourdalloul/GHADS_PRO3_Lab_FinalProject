package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import config.connection;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CoordFamilyController {
    
    @FXML private TextField nationalIdField, familyNameField, membersCountField, addressField;

    @FXML
    private void handleRegisterFamily() {
        String nationalId = nationalIdField.getText().trim();
        String name = familyNameField.getText().trim();
        String sizeStr = membersCountField.getText().trim();
        String address = addressField.getText().trim();

        
        if (nationalId.isEmpty() || name.isEmpty() || sizeStr.isEmpty() || address.isEmpty()) {
            showError("Missing Data", "Please fill in all fields!");
            return;
        }
        
        try (Connection conn = connection.getInstance().getConnection()) {
            
            String checkSql = "SELECT COUNT(*) FROM families WHERE national_id = ?";
            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setString(1, nationalId);
            ResultSet rs = psCheck.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                showError("Duplicate Error", "Family with this National ID already exists!");
                return;
            }

            String insertSql = "INSERT INTO families (national_id, household_name, family_size, location) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(insertSql);
            psInsert.setString(1, nationalId);
            psInsert.setString(2, name);
            psInsert.setInt(3, Integer.parseInt(sizeStr));
            psInsert.setString(4, address);
            
            psInsert.executeUpdate();
            showInfo("Success", "Family registered successfully!");
            
           
            nationalIdField.clear();
            familyNameField.clear();
            membersCountField.clear();
            addressField.clear();
            
        } catch (NumberFormatException e) {
            showError("Input Error", "Members count must be a number!");
        } catch (SQLException e) {
            showError("Database Error", "Could not save to database: " + e.getMessage());
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

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

   
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/mainScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UAMS - Login");
            stage.show();
        } catch (IOException e) {
            showError("Logout Error", "Could not return to login screen.");
        }
    }
    private boolean showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}