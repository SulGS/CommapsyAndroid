package com.example.commapsyandroid.entities;

import java.sql.Date;

public class PlaceRequest {

    private Integer ID;

    private String UserMail;

    private Date SendDate;

    private Integer PlaceID;

    private Double Latitude;

    private Double Longitude;

    private String Name;

    private String Photo;

    private String Description;

    private String Category;

    private String Admin_Mail;

    private boolean IsAccepted;

    private String ReplyBody;

    private Date ReplyDate;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
    }

    public String getUserMail() {
        return UserMail;
    }

    public void setUserMail(String userMail) {
        UserMail = userMail;
    }

    public Date getSendDate() {
        return SendDate;
    }

    public void setSendDate(java.util.Date sendDate) {
        SendDate = new Date(sendDate.getTime());
    }

    public Integer getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(Integer placeID) {
        PlaceID = placeID;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAdmin_Mail() {
        return Admin_Mail;
    }

    public void setAdmin_Mail(String admin_Mail) {
        Admin_Mail = admin_Mail;
    }

    public boolean isIsAccepted() {
        return IsAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        IsAccepted = isAccepted;
    }

    public String getReplyBody() {
        return ReplyBody;
    }

    public void setReplyBody(String replyBody) {
        ReplyBody = replyBody;
    }

    public Date getReplyDate() {
        return ReplyDate;
    }

    public void setReplyDate(java.util.Date replyDate) {
        ReplyDate = new Date(replyDate.getTime());
    }

}
