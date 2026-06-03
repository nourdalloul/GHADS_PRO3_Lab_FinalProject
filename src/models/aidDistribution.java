
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class aidDistribution {
    private final StringProperty distId;
    private final StringProperty distributedBy; 
    private final StringProperty aidType;
    private final StringProperty distributionDate;

    public aidDistribution(String distId, String orgName, String aidType, String date) {
        this.distId = new SimpleStringProperty(distId);
        this.distributedBy = new SimpleStringProperty(orgName);
        this.aidType = new SimpleStringProperty(aidType);
        this.distributionDate = new SimpleStringProperty(date);
    }
    
    // Getters
    public StringProperty distIdProperty() { return distId; }
    public StringProperty orgNameProperty() { return distributedBy; }
    public StringProperty aidTypeProperty() { return aidType; }
    public StringProperty dateProperty() { return distributionDate; }
}