package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.orgnization;
import config.connection;
import java.sql.*;
import java.util.Optional;

public class organization_managemenController {
    @FXML private TableView<orgnization> orgTable;
    @FXML private TableColumn<orgnization, String> idCol, nameCol, typeCol, contactCol;
    @FXML private TextField nameField, typeField, contactField;

    private ObservableList<orgnization> orgList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty());
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        typeCol.setCellValueFactory(cell -> cell.getValue().typeProperty());
        contactCol.setCellValueFactory(cell -> cell.getValue().contactInfoProperty());

        loadOrgData();

        orgTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
                typeField.setText(newVal.getType()); 
                contactField.setText(newVal.getContactInfo());
            }
        });
    }

    private void loadOrgData() {
        orgList.clear();
        try (Connection conn = connection.getInstance().getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM organizations")) {
            while (rs.next()) {
                orgList.add(new orgnization(rs.getString("org_id"), rs.getString("name"), 
                                             rs.getString("type"), rs.getString("contact_info")));
            }
            orgTable.setItems(orgList);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleAdd() {
        try (Connection conn = connection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO organizations (name, type, contact_info) VALUES (?, ?, ?)")) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, typeField.getText());
            pstmt.setString(3, contactField.getText());
            pstmt.executeUpdate();
            loadOrgData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleEdit() {
        orgnization selected = orgTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try (Connection conn = connection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE organizations SET name=?, type=?, contact_info=? WHERE org_id=?")) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, typeField.getText());
            pstmt.setString(3, contactField.getText());
            pstmt.setString(4, selected.getId());
            pstmt.executeUpdate();
            loadOrgData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleDelete() {
        orgnization selected = orgTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try (Connection conn = connection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM organizations WHERE org_id=?")) {
            pstmt.setString(1, selected.getId());
            pstmt.executeUpdate();
            loadOrgData();
        } catch (SQLException e) { e.printStackTrace(); }
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
