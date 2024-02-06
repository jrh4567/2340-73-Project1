package com.example.planningbuddy.db;

import android.util.Log;

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
    private static List<Course>  courseList = new ArrayList<>();

    public static void addTask(Task task) {
        Log.d("TASKMANAGER", "addTask");
        taskList.add(task);
    }
    public static void addCourse(Course task) {
        courseList.add(task);
    }
    public static void removeTask(int position) {
        taskList.remove(position);
    }
    public static void updateTask(int position, Task task) {
        taskList.set(position, task);
    }
    public static void removeCourse(int position) {
        courseList.remove(position);
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
    public static List<Course> getAllCourses() {
        return new ArrayList<>(courseList);
    }
}
