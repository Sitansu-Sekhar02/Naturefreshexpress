package com.blucor.vsfarm.Model;

public class CategoryProducts {
    private  String category_id;
    private  String product_id;
    private  String user_id;
    private  String product_name;
    private  String product_image;
    private  String product_price;
    private  String product_desc;
    private  String product_size;

    public CategoryProducts() {

    }
    public CategoryProducts(String category_id,String product_id, String user_id,String product_name, String product_image, String product_price, String product_desc,String product_size) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.user_id=user_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_desc = product_desc;
        this.product_size=product_size;
    }
    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    } public String getUser_id() {
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

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }
    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }
}
