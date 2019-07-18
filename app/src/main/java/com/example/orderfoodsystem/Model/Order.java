package com.example.orderfoodsystem.Model;

public class Order {
    private int ID;
    private String ProductID;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order(){

    }

    public Order(String productId, String productName, String quantity, String price, String discount){
        ProductID = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;

    }

    public Order(int ID ,String productId, String productName, String quantity, String price, String discount){
        this.ID = ID;
        ProductID = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
