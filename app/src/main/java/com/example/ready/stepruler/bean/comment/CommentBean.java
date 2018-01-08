package com.example.ready.stepruler.bean.comment;

/**
 * Created by single dog on 2018/1/3.
 */

public class CommentBean {
    private String userName;
    private String commentText;

    CommentBean() {
        userName = "";
        commentText = "";
    }

    CommentBean(String name, String text) {
        userName = name;
        commentText = text;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public void setCommentText(String text) {
        commentText = text;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentText() {
        return commentText;
    }
}
