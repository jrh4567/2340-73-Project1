package com.example.planningbuddy.ui.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mClassList;

    public CourseViewModel() {
        mClassList = new MutableLiveData<>();
        mClassList.setValue(new ArrayList<>());
    }

    public LiveData<List<String>> getClassList() {
        return mClassList;
    }

    public void addClass(String className) {
        List<String> currentList = mClassList.getValue();
        currentList.add(className);
        mClassList.setValue(currentList);
    }
}