package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 11-10-2017.
 */

public class RequestItem {
    String id,patient_id,item_id,item_type,req_datetime,req_address,req_msg,submitted_datetime,patient_name,item_name,short_desc;
    String price,item_image_file;

    public RequestItem() {
    }

    public RequestItem(String id, String patient_id, String item_id, String item_type, String req_datetime, String req_address, String req_msg, String submitted_datetime, String patient_name, String item_name, String short_desc, String price, String item_image_file) {
        this.id = id;
        this.patient_id = patient_id;
        this.item_id = item_id;
        this.item_type = item_type;
        this.req_datetime = req_datetime;
        this.req_address = req_address;
        this.req_msg = req_msg;
        this.submitted_datetime = submitted_datetime;
        this.patient_name = patient_name;
        this.item_name = item_name;
        this.short_desc = short_desc;
        this.price = price;
        this.item_image_file = item_image_file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getReq_datetime() {
        return req_datetime;
    }

    public void setReq_datetime(String req_datetime) {
        this.req_datetime = req_datetime;
    }

    public String getReq_address() {
        return req_address;
    }

    public void setReq_address(String req_address) {
        this.req_address = req_address;
    }

    public String getReq_msg() {
        return req_msg;
    }

    public void setReq_msg(String req_msg) {
        this.req_msg = req_msg;
    }

    public String getSubmitted_datetime() {
        return submitted_datetime;
    }

    public void setSubmitted_datetime(String submitted_datetime) {
        this.submitted_datetime = submitted_datetime;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
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

    public String getItem_image_file() {
        return item_image_file;
    }

    public void setItem_image_file(String item_image_file) {
        this.item_image_file = item_image_file;
    }
}
