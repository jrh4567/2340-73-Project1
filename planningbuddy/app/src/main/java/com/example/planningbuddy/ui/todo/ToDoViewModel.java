package com.example.planningbuddy.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToDoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ToDoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is todo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}