package com.example.ready.stepruler.bean.push;

/**
 * Created by single dog on 2018/1/2.
 */

public class TitleBean {
    private String title;
    private String subtitle;
    public TitleBean(){
        title=null;
        subtitle=null;
    }
    public TitleBean(String s1, String s2){
        title=s1;
        subtitle=s2;
    }

    public void setTitle(String s){title=s;}
    public void setSubtitle(String s){subtitle=s;}

    public String getTitle(){return  title;}
    public String getSubtitle(){return subtitle;}
}
