package com.blucor.vsfarm.Model;

import org.json.JSONArray;

public class CategoryProducts {
    private  String category_id;
    private  String product_id;
    private  String user_id;
    private  String product_name;
    private  String product_image;
    private  String product_price;
    private  String product_desc;
    private  String product_size;
    private  String avl_qt;
    private  String tax_rate;
    private JSONArray array;

    public CategoryProducts() {


    }
    public CategoryProducts(String category_id,String product_id, String user_id,String product_name, String product_image, String product_price, String product_desc,String product_size,String avl_qt,String tax_rate) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.user_id=user_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_desc = product_desc;
        this.product_size=product_size;
        this.avl_qt=avl_qt;
        this.tax_rate=tax_rate;
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

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public String getAvl_qt() {
        return avl_qt;
    }

    public void setAvl_qt(String avl_qt) {
        this.avl_qt = avl_qt;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }
}
