package com.example.ready.stepruler.module.login;

public class User {
    private static final String TAG = "User";

    private String userName;
    private String userPwd;
    public User(String name,String pwd){
        this.userName=name;
        this.userPwd=pwd;
    }
    public String getId(){
        return userName;
    }
    public String getPwd(){
        return userPwd;
    }
}
