package com.example.planningbuddy.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Task>> mAssignments;

    public DashboardViewModel() {
        mAssignments = new MutableLiveData<>();
        mAssignments.setValue(TaskManager.getTasksByType(TaskType.ASSIGNMENT));
    }

    public LiveData<List<Task>> getAssignments() {
        return mAssignments;
    }
}