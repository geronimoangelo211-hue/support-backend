package com.attendance;

public class Student {
    private String id;
    private String name;
    private String gcHandle;

    // Constructors
    public Student() {}
    public Student(String id, String name, String gcHandle) {
        this.id = id;
        this.name = name;
        this.gcHandle = gcHandle;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGcHandle() { return gcHandle; }
    public void setGcHandle(String gcHandle) { this.gcHandle = gcHandle; }
}