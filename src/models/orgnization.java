package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class orgnization {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final StringProperty contactInfo;

    public orgnization(String id, String name, String type, String contactInfo) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.contactInfo = new SimpleStringProperty(contactInfo);
    }

    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public StringProperty contactInfoProperty() { return contactInfo; }

    public String getType() {
        return type.get();
    }

    public String getContactInfo() {
        return contactInfo.get();
    }

    public String getName() {
        return name.get();
    }

    public String getId() {
        return id.get();
    }
   
}