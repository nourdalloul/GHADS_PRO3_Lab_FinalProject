package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import config.connection;
import java.sql.*;
import java.time.LocalDate;

public class AidController {
    @FXML private TextField familyIdField, aidTypeField, orgNameField, distributedByField;

    @FXML
    private void handleSaveAid() {
        String familyId = familyIdField.getText();
        
        try (Connection conn = connection.getInstance().getConnection()) {
            // 1. جلب بيانات العائلة
            String query = "SELECT household_name, vulnerability_level FROM families WHERE family_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, familyId);
            ResultSet rs = ps.executeQuery();
            
            if (!rs.next()) {
                showError("Not Found", "Family ID not found.");
                return;
            }
            
            String name = rs.getString("household_name");
            String vuln = rs.getString("vulnerability_level");

            // 2. فحص التكرار (آخر 30 يوم)
            if (vuln.equals("MEDIUM") || vuln.equals("LOW")) {
                String checkDate = "SELECT organization_id, distribution_date FROM aid_distributions " +
                                   "WHERE family_id = ? AND distribution_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
                PreparedStatement psCheck = conn.prepareStatement(checkDate);
                psCheck.setString(1, familyId);
                ResultSet rsCheck = psCheck.executeQuery();
                
                if (rsCheck.next()) {
                    showError("Distribution Rejected", 
                        "Family: " + name + "\n" +
                        "Vulnerability: " + vuln + "\n" +
                        "Last Aid Org: " + rsCheck.getString("name") + "\n" +
                        "Date: " + rsCheck.getDate("distribution_date").toString() + "\n" +
                        "Reason: Received aid in the last 30 days.");
                    return;
                }
            }

            // 3. حفظ التوزيع الجديد
            String insert = "INSERT INTO aid_distributions(family_id, aid_type, organization_id, distribution_date, distributed_by) VALUES (?, ?, ?, CURDATE(), ?)";
            PreparedStatement psInsert = conn.prepareStatement(insert);
            psInsert.setString(1, familyId);
            psInsert.setString(2, aidTypeField.getText());
            psInsert.setString(3, orgNameField.getText());
            psInsert.setString(4, distributedByField.getText()); // القيمة المأخوذة من الحقل
            psInsert.executeUpdate();
            
            showInfo("Success", "Aid distributed successfully!");
            
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void showInfo(String t, String c) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(t);
        alert.setHeaderText(null); 
        alert.setContentText(c);
        alert.showAndWait();
    }


    private void showError(String t, String c) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(t);
        alert.setHeaderText(null);
        alert.setContentText(c);
        alert.showAndWait();
    }
}
