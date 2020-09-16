package com.example.RoomChef;

public class Review {

    int seq;
    String image;
    String title;
    String content;

    public Review(int seq, String image, String title, String content) {
        this.seq = seq;
        this.image = image;
        this.title = title;
        this.content = content;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
