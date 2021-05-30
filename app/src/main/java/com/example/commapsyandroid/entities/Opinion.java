package com.example.commapsyandroid.entities;

import javax.json.JsonObject;

public class Opinion {

    private Integer ID;

    private String User_Mail;

    private Integer PlaceID;

    private Integer Rating;

    private String Comment;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUser_Mail() {
        return User_Mail;
    }

    public void setUser_Mail(String user_Mail) {
        User_Mail = user_Mail;
    }

    public Integer getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(Integer placeID) {
        PlaceID = placeID;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public static Opinion jsonToOpinion(JsonObject json)
    {
        try {
            Opinion op = new Opinion();

            op.setID(json.getInt("id"));
            op.setUser_Mail(json.getString("user_Mail"));
            op.setPlaceID(json.getInt("placeID"));
            op.setRating(json.getInt("rating"));
            op.setComment(json.getString("comment"));


            return op;
        }catch(Exception ex)
        {
            return null;
        }


    }
}
