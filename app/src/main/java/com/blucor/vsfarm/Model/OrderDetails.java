package com.blucor.vsfarm.Model;

public class OrderDetails {
    private  String user_id;
    private  String product_id;
    private  String product_name;
    private  String product_image;
    private  String product_quantity;
    private  String order_id;
    private  String product_price;

    private  String order_date_month;

    public OrderDetails() {
    }

    public OrderDetails(String user_id, String product_id, String product_name,String product_image, String product_quantity, String order_date_month,String order_id,String product_price) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_quantity = product_quantity;
        this.order_date_month = order_date_month;
        this.order_id=order_id;
        this.product_price=product_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getOrder_date_month() {
        return order_date_month;
    }

    public void setOrder_date_month(String order_date_month) {
        this.order_date_month = order_date_month;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
    public String getProduct_price() {
        return product_price;
    }

}
