package com.example.viveksharma.upload_admin;

public class ImageUploadModel {

    public String title;
    public String desc;
    public String url;

    public ImageUploadModel(){

    }



    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public ImageUploadModel(String title, String desc, String url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
