package com.example.planningbuddy.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey
    public int uid;
    public String name;
    public Date date;
    public TaskType type;
    public Course associatedCourse;
    public boolean isCompleted;
}
