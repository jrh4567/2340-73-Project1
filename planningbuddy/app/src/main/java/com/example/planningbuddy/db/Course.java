package com.example.planningbuddy.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Course implements Comparable<Course> {
    private String department;
    private int number;
    private String name;
    private MeetingTime[] meetingTimes;

    private static ArrayList<Course> currentCourses = new ArrayList<Course>();

    public Course(String department, int number, String name, MeetingTime[] meetingTimes) {
        this.department = department;
        this.number = number;
        this.name = name;
        this.meetingTimes = meetingTimes;
    }
    public Course(String department, int number) {
        this(department, number, null, null);
    }

    public static void addTask(Course course) {
        currentCourses.add(course);
    }

    public static Course getCurrentCourse(int index) {
        return currentCourses.get(index);
    }

    public static ArrayList<Course> getCurrentCourses() {
        return currentCourses;
    }

    public String toString() {
        String result = String.format("%s %d: %s\n", department, number, name);
        for (MeetingTime meeting : meetingTimes) {
            result += String.format("%s: %s to %s\t", meeting.meetingType, meeting.startDateTime.toString(), meeting.endDateTime.toString());
        }
        return result;
    }

    public String getDepartment() {
        return department;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public MeetingTime[] getMeetingTimes() {
        return meetingTimes;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMeetingTimes(MeetingTime[] meetingTimes) {
        this.meetingTimes = meetingTimes;
    }
    public int compareTo(Course other) {
        // Compare based on department
        int departmentComparison = this.department.compareTo(other.department);
        if (departmentComparison != 0) {
            return departmentComparison;
        }

        // Compare based on number
        int numberComparison = Integer.compare(this.number, other.number);
        if (numberComparison != 0) {
            return numberComparison;
        }

        // Compare based on name
        if (this.name == null && other.name == null) {
            return 0;
        } else if (this.name == null) {
            return -1;
        } else if (other.name == null) {
            return 1;
        } else {
            return this.name.compareTo(other.name);
        }
    }

    public static class MeetingTime {
        private String building;
        private GregorianCalendar startDateTime;
        private GregorianCalendar endDateTime;
        private MeetingType meetingType;

        public MeetingTime(MeetingType meetingType, String building, GregorianCalendar startDateTime, GregorianCalendar endDateTime) {
            this.meetingType = meetingType;
            this.building = building;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }

        public String toString() {
            return String.format("%s: %s, %s to %s", meetingType, building, startDateTime, endDateTime);
        }

        //TODO: add getDaysOfWeek() and other relevant getters

        public String getBuilding() {
            return building;
        }

        public GregorianCalendar getStartDateTime() {
            return startDateTime;
        }

        public GregorianCalendar getEndDateTime() {
            return endDateTime;
        }

        public MeetingType getMeetingType() {
            return meetingType;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public void setStartDateTime(GregorianCalendar startDateTime) {
            this.startDateTime = startDateTime;
        }

        public void setEndDateTime(GregorianCalendar endDateTime) {
            this.endDateTime = endDateTime;
        }

        public void setMeetingType(MeetingType meetingType) {
            this.meetingType = meetingType;
        }
    }
}
