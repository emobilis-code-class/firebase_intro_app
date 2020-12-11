package com.eac.productlistingapp.model;

public class Product {
    //pojo
    private String name;
    private String desc;
    private String price;
    private String image;

    public Product(String name, String desc, String price, String image) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
