package com.yahyagonder.instagramclonejava.model;

public class Post {

    public String email;

    public String comment;

    public String downloadURL;

    public Post(String email, String comment, String downloadURL) {
        this.email = email;
        this.comment = comment;
        this.downloadURL = downloadURL;
    }
}
