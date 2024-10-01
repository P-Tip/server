package com.ptip.models;

public class Haksik {
    private String date;
    private String haksik_type;
    private String haksik_items;

    public Haksik(){}

    public Haksik(String date, String haksik_type,String haksik_items){
        this.date = date;
        this.haksik_type = haksik_type;
        this.haksik_items = haksik_items;
    }

    // Getter 및 Setter 메서드
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHaksik_type() {
        return haksik_type;
    }

    public void setHaksik_type(String haksik_type) {
        this.haksik_type = haksik_type;
    }

    public String getHaksik_items() {
        return haksik_items;
    }

    public void setHaksik_items(String haksik_items) {
        this.haksik_items = haksik_items;
    }
}
