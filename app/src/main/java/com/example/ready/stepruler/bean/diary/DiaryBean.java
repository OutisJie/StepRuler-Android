package com.example.ready.stepruler.bean.diary;

/**
 * Created by ready on 2017/12/13.
 */

public class DiaryBean {
    private String date;
    private String title;
    private String content;
    private String tag;

    public DiaryBean(String date, String title,String content, String tag){
        this.date = date;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
