package com.attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class CloudData {
    
    @Id
    private String dataKey; // Will be "STUDENTS" or "LOGS"
    
    @Lob // @Lob allows the database to store massive amounts of text
    @Column(columnDefinition = "TEXT")
    private String jsonData;

    public CloudData() {}

    public CloudData(String dataKey, String jsonData) {
        this.dataKey = dataKey;
        this.jsonData = jsonData;
    }

    public String getDataKey() { return dataKey; }
    public void setDataKey(String dataKey) { this.dataKey = dataKey; }

    public String getJsonData() { return jsonData; }
    public void setJsonData(String jsonData) { this.jsonData = jsonData; }
}
