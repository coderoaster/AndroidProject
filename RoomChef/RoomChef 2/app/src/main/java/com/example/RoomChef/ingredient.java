package com.example.RoomChef;

public class ingredient {
    String name;
    String date;
    int seq;

    public ingredient(String name, String date, int seq) {

        this.name = name;
        this.date = date;
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
