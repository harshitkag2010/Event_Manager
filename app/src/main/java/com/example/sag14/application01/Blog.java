package com.example.sag14.application01;

/**
 * Created by sag14 on 17/11/16.
 */

public class Blog {

    private String title;
    private String desc;
    private String image;



    public void setUsername(String username) {
        this.username = username;
    }

    private String username;


    public Blog(){

    }


    public Blog(String title, String image, String desc) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUsername() {
        return username;
    }


}
