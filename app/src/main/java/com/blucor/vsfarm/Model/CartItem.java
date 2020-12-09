package com.blucor.vsfarm.Model;

public class CartItem {
    private  String cart_id;
    private  String product_id;
    private  String user_id;
    private  String product_name;
    private  String product_image;
    private  String product_price;
    private  String product_quantity;
    private  String product_size;



    public CartItem(String cart_id, String product_id, String user_id, String product_name, String product_image, String product_price, String product_quantity,String product_size) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_size = product_size;




    }

    public CartItem() {

    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }
}
