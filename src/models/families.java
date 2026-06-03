
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class families {
    private final StringProperty familyId;
    private final StringProperty householdName;
    private final StringProperty location;
    private final StringProperty phone;
    private final StringProperty nationalId;
    private final StringProperty familySize;
    private final StringProperty vulnerabilityLevel;

    public families(String familyId, String householdName, String location, String phone, 
                  String nationalId, String familySize, String vulnerabilityLevel) {
        this.familyId = new SimpleStringProperty(familyId);
        this.householdName = new SimpleStringProperty(householdName);
        this.location = new SimpleStringProperty(location);
        this.phone = new SimpleStringProperty(phone);
        this.nationalId = new SimpleStringProperty(nationalId);
        this.familySize = new SimpleStringProperty(familySize);
        this.vulnerabilityLevel = new SimpleStringProperty(vulnerabilityLevel);
    }

    // Properties للجدول
    public StringProperty familyIdProperty() { return familyId; }
    public StringProperty householdNameProperty() { return householdName; }
    public StringProperty locationProperty() { return location; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty nationalIdProperty() { return nationalId; }
    public StringProperty familySizeProperty() { return familySize; }
    public StringProperty vulnerabilityLevelProperty() { return vulnerabilityLevel; }

    // Getters
    public String getFamilyId() { return familyId.get(); }
    public String getHouseholdName() { return householdName.get(); }
    public String getLocation() { return location.get(); }
    public String getPhone() { return phone.get(); }
    public String getNationalId() { return nationalId.get(); }
    public String getFamilySize() { return familySize.get(); }
    public String getVulnerabilityLevel() { return vulnerabilityLevel.get(); }
}