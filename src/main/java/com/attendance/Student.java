package com.attendance;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String gcHandle;
    private String classLevel; // <--- NEW: Tells the backend to save the Class Level!
    private List<String> assignedDays = new ArrayList<>(); 

    public Student() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getGcHandle() { return gcHandle; }
    public void setGcHandle(String gcHandle) { this.gcHandle = gcHandle; }

    // <--- NEW Getters and Setters for Class Level --->
    public String getClassLevel() { return classLevel; }
    public void setClassLevel(String classLevel) { this.classLevel = classLevel; }
    
    public List<String> getAssignedDays() { return assignedDays; }
    public void setAssignedDays(List<String> assignedDays) { this.assignedDays = assignedDays; }
}
