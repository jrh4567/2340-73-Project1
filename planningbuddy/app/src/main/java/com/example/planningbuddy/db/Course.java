package com.example.planningbuddy.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Course {
    @PrimaryKey
    public int uid;
    public String name;
    public String department;
    public int number;
}
