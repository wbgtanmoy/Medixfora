package com.brandhype.medixfora.models;

import java.io.Serializable;

/**
 * Created by Tanmoy Banerjee on 14-11-2017.
 */

public class OrderDetails implements Serializable{
    //{"item_qty":"1","item_price":"99","line_total":"75","item_name":"Crocin",
    // "item_image_file":"1502104340.jpg","item_type":"itm"}

    String item_qty,item_price,line_total,item_name,item_image_file,item_type;

    public OrderDetails() {
    }

    public OrderDetails(String item_qty, String item_price, String line_total, String item_name, String item_image_file, String item_type) {
        this.item_qty = item_qty;
        this.item_price = item_price;
        this.line_total = line_total;
        this.item_name = item_name;
        this.item_image_file = item_image_file;
        this.item_type = item_type;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getLine_total() {
        return line_total;
    }

    public void setLine_total(String line_total) {
        this.line_total = line_total;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image_file() {
        return item_image_file;
    }

    public void setItem_image_file(String item_image_file) {
        this.item_image_file = item_image_file;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "item_qty='" + item_qty + '\'' +
                ", item_price='" + item_price + '\'' +
                ", line_total='" + line_total + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_image_file='" + item_image_file + '\'' +
                ", item_type='" + item_type + '\'' +
                '}';
    }
}
