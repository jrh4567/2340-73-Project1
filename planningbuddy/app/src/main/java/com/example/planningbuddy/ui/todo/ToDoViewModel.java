package com.example.planningbuddy.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ToDoViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mClassList;

    public ToDoViewModel() {
        mClassList = new MutableLiveData<>();
        mClassList.setValue(new ArrayList<>());
    }

    public LiveData<List<String>> getClassList() {
        return mClassList;
    }

    public void addClass(String className) {
        List<String> currentList = mClassList.getValue();
        assert currentList != null;
        currentList.add(className);
        mClassList.setValue(currentList);
    }
}