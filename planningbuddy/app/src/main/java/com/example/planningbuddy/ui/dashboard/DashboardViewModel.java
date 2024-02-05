package com.example.planningbuddy.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.planningbuddy.db.Task;
import com.example.planningbuddy.db.TaskManager;
import com.example.planningbuddy.db.TaskType;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    //commented out code is to display sort by date or sort by course depending on input from spinner
    /**private final MutableLiveData<List<Task>> mAssignments;
    private final MutableLiveData<Boolean> mSortByDate;

    public DashboardViewModel() {
        mAssignments = new MutableLiveData<List<Task>>();
        mSortByDate = new MutableLiveData<>(true); // Default value is true (Sort by Date)
        updateAssignments();
    }

    public LiveData<List<Task>> getAssignments() {
        return mAssignments;
    }

    public boolean getSortByDate() {
        return Boolean.TRUE.equals(mSortByDate.getValue());
    }

    public void setSortByDate(boolean sortByDate) {
        mSortByDate.setValue(sortByDate);
        updateAssignments();
    }

    private void updateAssignments() {
        if (mSortByDate.getValue() != null && mSortByDate.getValue()) {
            mAssignments.setValue(TaskManager.getAssignmentsByDate());
        } else {
            mAssignments.setValue(TaskManager.getAssignmentsByCourse());
        }
    }
     */
    private final MutableLiveData<List<Task>> mAssignments;

    public DashboardViewModel() {
        mAssignments = TaskManager.getTaskLiveData();
        // Initially, populate the list with all exams
        mAssignments.setValue(TaskManager.getAssignmentsByDate());
    }

    public LiveData<List<Task>> getAssignments() {
        return mAssignments;
    }
}
