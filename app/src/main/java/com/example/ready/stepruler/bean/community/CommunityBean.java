package com.example.ready.stepruler.bean.community;

/**
 * Created by single dog on 2017/12/27.
 */

public class CommunityBean {
    private int communityId;
    private String communityText;
    private String communityDate;
    private int userId;
    private int communityZan;
    private int communityComment;

    public CommunityBean(){
        this.communityZan = 0;
        this.communityComment = 0;
    }
    public CommunityBean(int community_id, String community_text, String community_date, int user_id, int community_zan, int community_comment){
        this.communityId = community_id;
        this.communityText = community_text;
        this.communityDate = community_date;
        this.userId = user_id;
        this.communityZan  = community_zan;
        this.communityComment = community_comment;
    }

    public int getCommunityId() {
        return communityId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCommunityDate() {
        return communityDate;
    }

    public String getCommunityText() {
        return communityText;
    }

    public int getCommunityZan() {
        return communityZan;
    }

    public int getCommunityComment() {
        return communityComment;
    }

    public void setCommunityComment(int communityComment) {
        this.communityComment = communityComment;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setCommunityDate(String communityDate) {
        this.communityDate = communityDate;
    }

    public void setCommunityText(String communityText) {
        this.communityText = communityText;
    }

    public void setCommunityZan(int communityZan) {
        this.communityZan = communityZan;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
