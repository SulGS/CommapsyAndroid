package com.example.commapsyandroid.entities;

import javax.json.JsonObject;

public class User {

    private String Mail;

    private String Name;

    private String Surname;

    private String Password;

    private String _Key;

    private boolean Is_Enable;

    private String Profile_Photo;

    private String Gender;

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String get_Key() {
        return _Key;
    }

    public void set_Key(String _Key) {
        this._Key = _Key;
    }

    public boolean isIs_Enable() {
        return Is_Enable;
    }

    public void setIs_Enable(boolean is_Enable) {
        Is_Enable = is_Enable;
    }

    public String getProfile_Photo() {
        return Profile_Photo;
    }

    public void setProfile_Photo(String profile_Photo) {
        Profile_Photo = profile_Photo;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }


    public static User jsonToUser(JsonObject json)
    {
        try {
            User user = new User();

            user.setMail(json.getString("mail"));
            user.setName(json.getString("name"));
            user.setSurname(json.getString("surname"));
            user.setPassword(json.getString("password"));
            user.set_Key(json.getString("_Key"));
            user.setIs_Enable(json.getBoolean("is_Enable"));
            user.setProfile_Photo(json.getString("profile_Photo"));
            user.setGender(json.getString("gender"));


            return user;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }


    }

}
