package com.example.planningbuddy.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<Task>> mExams;

    public NotificationsViewModel() {
        mExams = new MutableLiveData<>();
        // Initially, populate the list with all exams
        mExams.setValue(TaskManager.getTasksByType(TaskType.EXAM));
    }

    public LiveData<List<Task>> getExams() {
        return mExams;
    }
}