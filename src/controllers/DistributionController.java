package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.aidDistribution;
import config.connection;
import java.sql.*;

public class DistributionController {
    @FXML private TableView<aidDistribution> distTable;
    @FXML private TableColumn<aidDistribution, String> orgCol, typeCol, dateCol;
    @FXML private ComboBox<String> orgSearchCombo;

    @FXML
    public void initialize() {
        orgCol.setCellValueFactory(cell -> cell.getValue().orgNameProperty());
        typeCol.setCellValueFactory(cell -> cell.getValue().aidTypeProperty());
        dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty());

        loadDistributions("SELECT d.*, o.name FROM aid_distributions d JOIN organizations o ON d.organization_id = o.org_id");
        loadOrgCombo();
    }

    private void loadDistributions(String query) {
        ObservableList<aidDistribution> list = FXCollections.observableArrayList();
        try (Connection conn = connection.getInstance().getConnection();
             ResultSet rs = conn.createStatement().executeQuery(query)) {
            while (rs.next()) {
                list.add(new aidDistribution(
                    rs.getString("distribution_id"), 
                    rs.getString("name"), 
                    rs.getString("aid_type"), 
                    rs.getString("distribution_date")
                ));
            }
            distTable.setItems(list);
            distTable.refresh(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    private void loadOrgCombo() {
        orgSearchCombo.getItems().add("All");
        try (Connection conn = connection.getInstance().getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT name FROM organizations")) {
            while (rs.next()) orgSearchCombo.getItems().add(rs.getString("name"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleSearch() {
        String selected = orgSearchCombo.getValue();
        if (selected == null || selected.equals("All")) {
            loadDistributions("SELECT d.*, o.name FROM aid_distributions d JOIN organizations o ON d.organization_id = o.org_id");
        } else {
            loadDistributions("SELECT d.*, o.name FROM aid_distributions d JOIN organizations o ON d.organization_id = o.org_id WHERE o.name = '" + selected + "'");
        }
    }
}
