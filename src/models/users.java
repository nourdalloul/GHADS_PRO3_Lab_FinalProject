
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class users {
    private final StringProperty fullName;
    private final StringProperty username; 
    private final StringProperty email;
    private final StringProperty role;
    private final StringProperty organizationId;

    public users(String fullName, String username, String password, String email, String role, String organizationId) {
        this.fullName = new SimpleStringProperty(fullName);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
        this.organizationId = new SimpleStringProperty(organizationId);
    }
    
    public users(String fullName, String username, String email, String role, String organizationId) {
        this.fullName = new SimpleStringProperty(fullName);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
        this.organizationId = new SimpleStringProperty(organizationId);
    }

    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
    public StringProperty roleProperty() { return role; }
    public StringProperty organizationIdProperty() { return organizationId; }

    public String getFullName() { return fullName.get(); }
    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getRole() { return role.get(); }
    public String getOrganizationId() { return organizationId.get(); }
    
}