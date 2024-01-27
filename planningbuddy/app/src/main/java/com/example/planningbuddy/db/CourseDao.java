package com.example.planningbuddy.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    List<Task> getAll();

    @Insert
    void insertAll(Course... courses);

    @Delete
    void delete(Course course);
}
