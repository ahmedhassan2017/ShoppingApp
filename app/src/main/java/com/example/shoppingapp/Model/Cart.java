package com.example.shoppingapp.Model;

public class Cart {
    private String PID,date,description,discount,image,pName,price,quantity,time;

    public String getPid() {
        return PID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPid(String pid) {
        this.PID = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Cart(String pid, String date, String description, String discount, String image, String pName, String price, String quantity, String time) {
        this.PID = pid;
        this.date = date;
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.pName = pName;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
    }

    public Cart()
    {

    }

}
