package com.example.planningbuddy.db;

import java.util.Date;

public class Task {
    public String name;
    public Date dueDate;
    public TaskType type;
    public Course associatedCourse;
    public boolean isCompleted;
}
