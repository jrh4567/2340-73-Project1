package com.example.planningbuddy.ui.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Course;
import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends ViewModel {

    private final MutableLiveData<List<Course>> mCourse;

    public CourseViewModel() {
        mCourse = new MutableLiveData<>();
        // Initially, populate the list with all exams
        mCourse.setValue(TaskManager.getAllCourses());
    }

    public LiveData<List<Course>> getCourses() {
        mCourse.setValue(TaskManager.getAllCourses());
        return mCourse;
    }
}