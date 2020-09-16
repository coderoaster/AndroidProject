package com.example.semiproject;



public class AddressBook {
    private String no;
    private String name;
    private String phone;
    private String address;
    private String relation;

    public AddressBook(String no, String name, String phone, String relation) {
        this.no = no;
        this.name = name;
        this.phone = phone;
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



