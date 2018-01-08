package com.example.ready.stepruler.module.login;

/**
 * Created by single dog on 2017/12/18.
 */

public interface OnLoginFinishedListener {
    void onUsernameError();
    void onPasswordError();
    void onSuccess(int id, String name);
}
