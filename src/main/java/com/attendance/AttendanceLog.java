package com.attendance;

public class AttendanceLog {
    private String studentId;
    private String studentName;
    private String action; // "TIME IN" or "TIME OUT"
    private String timestamp;

    public AttendanceLog() {}

    public AttendanceLog(String studentId, String studentName, String action, String timestamp) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}