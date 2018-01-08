package com.example.ready.stepruler.bean.push;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ready on 2017/11/21.
 */

public class PushArticleDataBean implements Parcelable{
    @SerializedName("articleId")
    private int articleId;
    @SerializedName("articleTitle")
    private String articleTitle;
    @SerializedName("articleSubTitle")
    private String articleSubTitle;
    @SerializedName("articleAuthor")
    private String articleAuthor;
    @SerializedName("articleDate")
    private String articleDate;
    @SerializedName("articleTheme")
    private String articleTheme;
    @SerializedName("articleBodyhtml")
    private String articleBodyhtml;
    @SerializedName("articleImg")
    private String articleImg;
    @SerializedName("articleImgs")
    private String articleImgs;
    @SerializedName("articleText")
    private String articleText;

    public  PushArticleDataBean(){}

    protected PushArticleDataBean(Parcel in) {
        articleId = in.readInt();
        articleTitle = in.readString();
        articleSubTitle = in.readString();
        articleAuthor = in.readString();
        articleDate = in.readString();
        articleTheme = in.readString();
        articleBodyhtml = in.readString();
        articleImg = in.readString();
        articleImgs = in.readString();
        articleText = in.readString();
    }

    public static final Creator<PushArticleDataBean> CREATOR = new Creator<PushArticleDataBean>() {
        @Override
        public PushArticleDataBean createFromParcel(Parcel in) {
            return new PushArticleDataBean(in);
        }

        @Override
        public PushArticleDataBean[] newArray(int size) {
            return new PushArticleDataBean[size];
        }
    };

    public int getArticleId(){
        return articleId;
    }
    public void setArticleId(int id){
        this.articleId = id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
    public void setArticleTitle(String title) {
        this.articleTitle = title;
    }

    public String getArticleSubTitle() {
        return articleSubTitle;
    }
    public void setArticleSubTitle(String subTitle) {
        this.articleSubTitle = subTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String article_author) {
        this.articleAuthor = article_author;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String article_date) {
        this.articleDate = article_date;
    }

    public String getArticleTheme() {
        return articleTheme;
    }

    public void setArticleTheme(String article_theme) {
        this.articleTheme = article_theme;
    }

    public String getArticleBodyhtml(){
        return articleBodyhtml;
    }
    public void setArticleBodyhtml(String content){
        this.articleBodyhtml = content;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public String getArticleImgs() {
        return articleImgs;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public void setArticleImgs(String articleImgs) {
        this.articleImgs = articleImgs;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(articleId);
        dest.writeString(articleTitle);
        dest.writeString(articleSubTitle);
        dest.writeString(articleAuthor);
        dest.writeString(articleDate);
        dest.writeString(articleTheme);
        dest.writeString(articleBodyhtml);
        dest.writeString(articleImg);
        dest.writeString(articleImgs);
        dest.writeString(articleText);
    }
}