package controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CoordinatorController {

    @FXML private BorderPane adminRoot;
    @FXML private VBox contentArea;


    @FXML
    private void showDashboard(ActionEvent event) {
        loadSubView("/views/coordinator_dashboard.fxml");
    }

    @FXML
    private void showProfile(ActionEvent event) {
        loadSubView("/views/profile.fxml");
    }

    @FXML
    private void showFamilyManagement(ActionEvent event) {
        loadSubView("/views/coord_family_mgmt.fxml");
    }

    @FXML
    private void showAidDistribution(ActionEvent event) {
        loadSubView("/views/aid_distribution.fxml");
    }

    @FXML
    private void showChangePassword(ActionEvent event) {
        loadSubView("/views/change_password.fxml");
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
            e.printStackTrace();
            showError("Logout Error", "Could not return to login screen.");
        }
    }

   
    private void loadSubView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            adminRoot.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not load: " + fxmlPath);
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