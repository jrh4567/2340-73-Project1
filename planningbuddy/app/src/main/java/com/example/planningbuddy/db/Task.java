package com.example.planningbuddy.db;

import java.util.ArrayList;
import java.util.Date;

public class Task {
    private String name;
    private Date dueDate;
    private TaskType type;
    private Course associatedCourse;
    private boolean isCompleted;

    private static ArrayList<Task> currentTasks = new ArrayList<Task>();

    public Task(String name, Date dueDate, TaskType type, Course associatedCourse, boolean isCompleted) {
        this.name = name;
        this.dueDate = dueDate;
        this.type = type;
        this.associatedCourse = associatedCourse;
        this.isCompleted = isCompleted;
    }

    public static void addTask(Task task) {
        currentTasks.add(task);
    }

    public static Task getCurrentTask(int index) {
        return currentTasks.get(index);
    }

    public static ArrayList<Task> getCurrentTasks() {
        return currentTasks;
    }

    public String toString() {
        return String.format("%s: %s, due on %s for %s", name, type, dueDate, ("" + associatedCourse.getDepartment() + associatedCourse.getNumber()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Course getAssociatedCourse() {
        return associatedCourse;
    }

    public void setAssociatedCourse(Course associatedCourse) {
        this.associatedCourse = associatedCourse;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
