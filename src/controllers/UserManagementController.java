package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.users;
import config.connection;
import java.sql.*;
import java.util.Optional;

public class UserManagementController {

    @FXML private TableView<users> userTable;
    @FXML private TableColumn<users, String> nameCol, usernameCol, emailCol, roleCol,orgCol;
    @FXML private TextField nameField, usernameField, emailField;
    @FXML private PasswordField passField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private ComboBox<String> orgCombo;
    
    private ObservableList<users> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
       
        nameCol.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        roleCol.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        orgCol.setCellValueFactory(cellData -> cellData.getValue().organizationIdProperty());

       
        roleCombo.getItems().addAll("Admin", "Coordinator");

        loadUserData();

        loadOrgIds();
        
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getFullName());
                usernameField.setText(newVal.getUsername());
                emailField.setText(newVal.getEmail());
                roleCombo.setValue(newVal.getRole());
            }
        });
    }

    private void loadUserData() {
        userList.clear();
        String query = "SELECT full_name, username, email, role, organization_id FROM users";
        try (Connection conn = connection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                userList.add(new users(rs.getString("full_name"), 
                                      rs.getString("username"),  
                                      rs.getString("email"), 
                                      rs.getString("role"), 
                                      rs.getString("organization_id")));
            }
            userTable.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadOrgIds() {
    String query = "SELECT org_id FROM organizations"; 
    try (Connection conn = connection.getInstance().getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        orgCombo.getItems().clear(); 
        while (rs.next()) {
            orgCombo.getItems().add(rs.getString("org_id"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void handleAddUser() {
        if (orgCombo.getValue() == null) {
            showError("Error", "Please select an organization!");
            return;
        }

        String query = "INSERT INTO users (full_name, username, password, email, role, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, usernameField.getText());
            pstmt.setString(3, passField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, roleCombo.getValue());
            pstmt.setString(6, orgCombo.getValue()); 

            pstmt.executeUpdate();
            loadUserData();
            clearFields();
        } catch (SQLException e) {
            showError("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleEditUser() {
        users selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a user from the table first!");
            return;
        }

       
        if (orgCombo.getValue() == null || orgCombo.getValue().isEmpty()) {
            showError("Error", "Please select an organization!");
            return;
        }

        String query = "UPDATE users SET full_name=?, email=?, password=?, role=?, organization_id=? WHERE username=?";
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, passField.getText());
            pstmt.setString(4, roleCombo.getValue());

           
            int orgId = Integer.parseInt(orgCombo.getValue());
            pstmt.setInt(5, orgId);

            pstmt.setString(6, selected.getUsername());

            pstmt.executeUpdate();
            loadUserData();
            clearFields();
        } catch (NumberFormatException e) {
            showError("Input Error", "Organization ID must be a valid number!");
        } catch (SQLException e) {
            showError("Update Error", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser() {
        users selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a user to delete!");
            return;
        }

        String query = "DELETE FROM users WHERE username = ?";
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, selected.getUsername());
            pstmt.executeUpdate();

            loadUserData(); 
            clearFields();
        } catch (SQLException e) {
            showError("Delete Error", e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear(); usernameField.clear(); emailField.clear(); passField.clear();
        roleCombo.getSelectionModel().clearSelection();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
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