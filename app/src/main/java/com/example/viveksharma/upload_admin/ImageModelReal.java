package com.example.viveksharma.upload_admin;

public class ImageModelReal {

    public String title;
    public String description;
    public String url;

    public ImageModelReal() {
    }

    public ImageModelReal(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
