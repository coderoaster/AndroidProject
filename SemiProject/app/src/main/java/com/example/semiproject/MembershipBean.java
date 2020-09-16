package com.example.semiproject;

public class MembershipBean {

    private int seq;
    private String email;
    private String password;


    public MembershipBean(int seq, String email, String password) {
        this.seq = seq;
        this.email = email;
        this.password = password;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
