package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 07-08-2017.
 */

public class MedicineItem {

    private String id;
    private String item_type;
    private String item_name;
    private String short_desc;

    private String offer_price;
    private String price;
    private String status;
    private String category_id;
    private String category_name;
    private String image_name;
    private String item_image_file_path;
    private String company_name;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }



    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getItem_image_file_path() {
        return item_image_file_path;
    }

    public void setItem_image_file_path(String item_image_file_path) {
        this.item_image_file_path = item_image_file_path;
    }


    @Override
    public String toString() {
        return "MedicineItem{" +
                "id='" + id + '\'' +
                ", item_type='" + item_type + '\'' +
                ", item_name='" + item_name + '\'' +
                ", short_desc='" + short_desc + '\'' +
                ", offer_price='" + offer_price + '\'' +
                ", price='" + price + '\'' +
                ", status='" + status + '\'' +
                ", category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", image_name='" + image_name + '\'' +
                ", item_image_file_path='" + item_image_file_path + '\'' +
                ", company_name='" + company_name + '\'' +
                '}';
    }
}
