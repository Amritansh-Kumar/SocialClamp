package com.example.amritansh.socialclamp.models;

public class User {

    private String username;
    private String status;
    private String imageUrl;
    private String thumbImageUrl;

//    public User(String username, String status, String imageUrl, String thumbImageUrl){
//        this.username = username;
//        this.status = status;
//        this.imageUrl = imageUrl;
//        this.thumbImageUrl = thumbImageUrl;
//    }

    public User(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

}
