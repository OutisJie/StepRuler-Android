package com.example.ready.stepruler.module.login;


public interface LoginView {
    public String getUsername();
    public String getUserpwd();

    public void showToast(String msg);

    public void changeView(int id);
}
