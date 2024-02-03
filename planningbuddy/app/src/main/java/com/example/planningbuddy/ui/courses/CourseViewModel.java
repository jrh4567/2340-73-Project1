package com.example.planningbuddy.ui.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends ViewModel {

    private final MutableLiveData<List<Course>> mClassList;

    public CourseViewModel() {
        mClassList = new MutableLiveData<>();
        mClassList.setValue(Course.getCurrentCourses());
    }

    public LiveData<List<Course>> getClassList() {
        return mClassList;
    }

    public void addClass(Course newCourse) {
        List<Course> currentList = mClassList.getValue();
        assert currentList != null;
        currentList.add(newCourse);
        mClassList.setValue(currentList);
    }
}