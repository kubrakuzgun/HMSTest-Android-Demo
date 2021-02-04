package com.example.androiddemo;

public class ToDo {
    private int todoID;
    private int todoUserID;
    private String todoTitle;
    private String todoDesc;
    private String todoDate;
    private String todoStatus;

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public int getTodoUserID() {
        return todoUserID;
    }

    public void setTodoUserID(int todoUserID) {
        this.todoUserID = todoUserID;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDesc() {
        return todoDesc;
    }

    public void setTodoDesc(String todoDesc) {
        this.todoDesc = todoDesc;
    }

    public String getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
    }


    public String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(String todoStatus) {
        this.todoStatus = todoStatus;
    }
}
