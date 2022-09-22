package com.mysite.taskapp.model;

public class TasksModel {
    private String taskId;
    private String taskName;
    private boolean daily;
    private String userId;

    public TasksModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TasksModel(String taskId, String taskName, boolean daily, String userId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.daily = daily;
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public boolean isDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }
}