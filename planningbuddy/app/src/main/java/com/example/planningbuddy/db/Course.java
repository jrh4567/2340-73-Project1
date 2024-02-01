package com.example.planningbuddy.db;

import java.util.Date;

public class Course {
    private String department;
    private int number;
    private String name;
    private MeetingTime[] meetingTimes;

    private class MeetingTime {
        private String building;
        private Date startDateTime;
        private Date endDateTime;
        private MeetingType meetingType;

        public MeetingTime(MeetingType meetingType, String building, Date startDateTime, Date endDateTime) {
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

        public Date getStartDateTime() {
            return startDateTime;
        }

        public Date getEndDateTime() {
            return endDateTime;
        }

        public MeetingType getMeetingType() {
            return meetingType;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public void setStartDateTime(Date startDateTime) {
            this.startDateTime = startDateTime;
        }

        public void setEndDateTime(Date endDateTime) {
            this.endDateTime = endDateTime;
        }

        public void setMeetingType(MeetingType meetingType) {
            this.meetingType = meetingType;
        }
    }

    public Course(String department, int number, String name, MeetingTime[] meetingTimes) {
        this.department = department;
        this.number = number;
        this.name = name;
        this.meetingTimes = meetingTimes;
    }

    public String toString() {
        String result = String.format("%s %d: %s", department, number, name);
        for (MeetingTime meeting : meetingTimes) {
            result += "\t" + meeting.toString();
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
}
