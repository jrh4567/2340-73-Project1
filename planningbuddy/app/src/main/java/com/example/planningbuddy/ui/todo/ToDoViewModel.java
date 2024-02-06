package com.example.planningbuddy.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.util.ArrayList;
import java.util.List;

public class ToDoViewModel extends ViewModel {

    private final MutableLiveData<List<Task>> mToDo;

    public ToDoViewModel() {
        mToDo = new MutableLiveData<>();
        // initially populate list
        mToDo.setValue(TaskManager.getTasksByType(TaskType.TODO));
    }

    public LiveData<List<Task>> getToDos() {
        mToDo.setValue(TaskManager.getTasksByType(TaskType.TODO));
        return mToDo;
    }
}