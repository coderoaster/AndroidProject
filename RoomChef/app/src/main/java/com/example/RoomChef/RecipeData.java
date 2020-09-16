package com.example.RoomChef;

public class RecipeData {
    protected static String SEARCH=null;
    protected static String USERID=null;
    protected static final String CENIP="172.30.1.33";
    private String name;
    private String imgurl;
    private String seq;
    private String food;
    private String how;
    private String view;


    public RecipeData(String name, String imgurl, String seq, String food,String how, String view  ) {
        this.name = name;
        this.imgurl = imgurl;
        this.seq = seq;
        this.food = food;
        this.how = how;
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
