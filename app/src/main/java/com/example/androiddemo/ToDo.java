package com.example.androiddemo;

public class ToDo {
    String task;
    String taskDate;
    String taskStart;
    String taskEnd;
//const

    public ToDo(String task, String taskDate, String taskStart, String taskEnd) {
        this.task = task;
        this.taskDate = taskDate;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
    }
    public  String toString(){
        return task+" "+taskDate+" "+taskStart+" "+taskEnd;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(String taskStart) {
        this.taskStart = taskStart;
    }

    public String getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(String taskEnd) {
        this.taskEnd = taskEnd;
    }


}
