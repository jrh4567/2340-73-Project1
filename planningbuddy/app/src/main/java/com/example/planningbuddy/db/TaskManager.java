package com.example.planningbuddy.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.planningbuddy.db.TaskType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static List<Task> taskList = new ArrayList<>();
    private static MutableLiveData<List<Task>> taskLiveData = new MutableLiveData<>();

    public static void addTask(Task task) {
        taskList.add(task);
        updateLiveData();
    }
    public static void removeTask(Task task) {
        taskList.remove(task);
        updateLiveData();
    }
    private static void updateLiveData() {
        taskLiveData.setValue(new ArrayList<>(taskList));
    }

    public static List<Task> getTasksByType(TaskType type) {
        return taskList.stream()
                .filter(task -> task.getType() == type)
                .collect(Collectors.toList());
    }
    public static List<Task> getAssignmentsByDate() {
        return taskList.stream()
                .filter(task -> task.getType() == TaskType.ASSIGNMENT && task.getDueDate() != null)
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }
    public static List<Task> getAssignmentsByCourse() {
        return taskList.stream()
                .filter(task -> task.getType() == TaskType.ASSIGNMENT && task.getAssociatedCourse() != null)
                .sorted(Comparator.comparing(Task::getAssociatedCourse))
                .collect(Collectors.toList());
    }
    public static List<Task> getAllTasks() {
        return new ArrayList<>(taskList);
    }
    public static MutableLiveData<List<Task>> getTaskLiveData() {
        return taskLiveData;
    }
}
