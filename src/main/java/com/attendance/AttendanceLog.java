package com.attendance;

import java.util.Map;

public class AttendanceLog {
    private String id;
    private String name;
    private String action; 
    private String time;
    private String date;
    private Map<String, String> details; // <-- THIS SAVES THE GC, ANNOUNCEMENT, ETC.

    public AttendanceLog() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details; }
}
