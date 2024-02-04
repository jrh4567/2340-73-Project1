package com.example.planningbuddy.db;

import com.example.planningbuddy.db.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static List<Task> taskList = new ArrayList<>();

    public static void addTask(Task task) {
        taskList.add(task);
    }

    public static List<Task> getTasksByType(TaskType type) {
        return taskList.stream()
                .filter(task -> task.getType() == type)
                .collect(Collectors.toList());
    }

    public static List<Task> getAllTasks() {
        return new ArrayList<>(taskList);
    }
}
