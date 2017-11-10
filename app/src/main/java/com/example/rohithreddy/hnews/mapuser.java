package com.example.rohithreddy.hnews;

/**
 * Created by rohithreddy on 26/07/17.
 */

public class mapuser {
    private int id;
    private String title;
    private String message;
    private String imageUrl;
    private String timestamp;

    public mapuser(String title, String message, String imageUrl, String timestamp) {
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.timestamp=timestamp;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
