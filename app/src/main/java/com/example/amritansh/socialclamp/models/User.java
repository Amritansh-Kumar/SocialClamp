package com.example.amritansh.socialclamp.models;

public class User {

    private String username;
    private String status;
    private String image;
    private String thumb_image;

    public User(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
