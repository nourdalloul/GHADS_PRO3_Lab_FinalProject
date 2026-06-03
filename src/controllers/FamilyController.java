package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.families;
import config.connection;
import java.sql.*;
import java.util.Optional;

public class FamilyController {
    @FXML private TableView<families> familyTable;
    @FXML private TableColumn<families, String> idCol, nameCol, locationCol, phoneCol, nationalIdCol, sizeCol, vulnerabilityCol;
    @FXML private TextField familyIdField, nameField, locationField, phoneField, nationalIdField, sizeField, vulnerabilityField;

    private ObservableList<families> familyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cell -> cell.getValue().familyIdProperty());
        nameCol.setCellValueFactory(cell -> cell.getValue().householdNameProperty());
        locationCol.setCellValueFactory(cell -> cell.getValue().locationProperty());
        phoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        nationalIdCol.setCellValueFactory(cell -> cell.getValue().nationalIdProperty());
        sizeCol.setCellValueFactory(cell -> cell.getValue().familySizeProperty());
        vulnerabilityCol.setCellValueFactory(cell -> cell.getValue().vulnerabilityLevelProperty());

        loadData();
    }

    private void loadData() {
        familyList.clear();
        try (Connection conn = connection.getInstance().getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM families")) {
            while (rs.next()) {
                familyList.add(new families(
                    rs.getString("family_id"), rs.getString("household_name"), 
                    rs.getString("location"), rs.getString("phone"),
                    rs.getString("national_id"), rs.getString("family_size"),
                    rs.getString("vulnerability_level")
                ));
            }
            familyTable.setItems(familyList);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleAdd() {
        String query = "INSERT INTO families (family_id, household_name, location, phone, national_id, family_size, vulnerability_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, familyIdField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, locationField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setString(5, nationalIdField.getText());
            pstmt.setString(6, sizeField.getText()); 
            pstmt.setString(7, vulnerabilityField.getText());

            pstmt.executeUpdate();
            loadData();
            clearFields();
        } catch (Exception e) {
            showError("Error", "Check your inputs: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        families selected = familyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        if (sizeField.getText().isEmpty()) {
            showError("Input Error", "Family Size cannot be empty!");
            return;
        }

        String query = "UPDATE families SET household_name=?, location=?, phone=?, national_id=?, family_size=?, vulnerability_level=? WHERE family_id=?";
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, locationField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, nationalIdField.getText());

            try {
                pstmt.setInt(5, Integer.parseInt(sizeField.getText()));
            } catch (NumberFormatException e) {
                showError("Input Error", "Family Size must be a valid number!");
                return;
            }

            pstmt.setString(6, vulnerabilityField.getText());
            pstmt.setString(7, selected.getFamilyId());

            pstmt.executeUpdate();
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        families selected = familyTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try (Connection conn = connection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM families WHERE family_id=?")) {
            pstmt.setString(1, selected.getFamilyId());
            pstmt.executeUpdate();
            loadData();
            clearFields();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void clearFields() {
        familyIdField.clear(); nameField.clear(); locationField.clear(); 
        phoneField.clear(); nationalIdField.clear(); sizeField.clear(); vulnerabilityField.clear();
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title); alert.setContentText(content); alert.show();
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