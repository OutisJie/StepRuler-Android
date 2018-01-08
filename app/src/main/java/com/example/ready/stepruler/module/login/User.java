package com.example.ready.stepruler.module.login;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class User {
    private String userName;
    private String userPwd;
    private static final String MASTER_PASSWORD = "FORYOU";  // AES加密算法的种子
    private static final String JSON_ID = "userName";
    private static final String JSON_PWD = "userPwd";
    private static final String TAG = "User";

    public User(String name,String pwd){
        this.userName=name;
        this.userPwd=pwd;
    }

    public User(JSONObject json)throws Exception{
        if (json.has(JSON_ID)) {
            String id=json.getString(JSON_ID);
            String pwd=json.getString(JSON_PWD);
            //解密后存放
            this.userName=AES.decrypt(MASTER_PASSWORD,id);
            this.userPwd=AES.decrypt(MASTER_PASSWORD,pwd);
        }
    }

    public JSONObject toJSON()throws Exception{
        //加密后保存
        String id=AES.encrypt(MASTER_PASSWORD,userName);
        String pwd=AES.encrypt(MASTER_PASSWORD,userPwd);
        Log.i(TAG,"加密后："+id+" "+pwd);
        JSONObject json=new JSONObject();
        try{
            json.put(JSON_ID,id);
            json.put(JSON_PWD,pwd);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public String getId(){
        return userName;
    }
    public String getPwd(){
        return userPwd;
    }
}
