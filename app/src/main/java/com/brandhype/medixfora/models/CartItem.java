package com.brandhype.medixfora.models;

import java.io.Serializable;

/**
 * Created by Tanmoy Banerjee on 18-08-2017.
 */

public class CartItem implements Serializable {
    //rowid":"69ae917fc22c8843c417a89653901a20","id":"1","itemtype":"itm","item_name":"Crocin",
    // "price":"99","qty":"2","category_names":"Child & Mother Care Products",
    // "company_name":"Ranbaxy","item_image_file":"1502104340.jpg",
    // "item_image_file_path":"http:\/\/brandhypedigital.in\/demo\/medixfora\/assets\/uploaded_files\/medicine_image\/medium\/1502104340.jpg","subtotal":198

    //"speciality_names":"Medicine,Anaesthesia","hospital_name":"Ganga Ram Hospital",
     // "doctor_profile_image":"\/mukta-k-1-6.jpg",
    // "doctor_profile_image_path":"http:\/\/brandhypedigital.in\/demo\/medixfora\/assets\/uploaded_files\/doctor_image\/medium\/http:\/\/russian.indiahealthhelp.com\/wp-content\/uploads\/2017\/04\/mukta-k-1-6.jpg",
    // "doctor_id":"2179","week_day":"1","checkup_start_time":"6:00 PM","checkup_end_time":"7:00 PM",
    // "appoint_number_of_patient":"10","subtotal":1000,"patient_name":"Santanu Kundu",
    // "patient_email":"s.kundu@gmail.com","patient_contact":"9432902300","appt_date":"2017-08-19"}

    String rowid,id,itemtype,item_name,price,qty,category_names,company_name,item_image_file,item_image_file_path;

    String speciality_names,hospital_name,doctor_profile_image,doctor_profile_image_path,doctor_id,week_day,checkup_start_time,checkup_end_time;
    String appoint_number_of_patient,subtotal,patient_name,patient_email,patient_contact,appt_date;
    String attachment_link;

    String appoint_number_of_patient_updated,no_of_patient_appointed;


    public CartItem(){}

    public CartItem(String rowid, String id, String itemtype, String item_name, String price, String qty, String category_names, String company_name, String item_image_file, String item_image_file_path, String speciality_names, String hospital_name, String doctor_profile_image, String doctor_profile_image_path, String doctor_id, String week_day, String checkup_start_time, String checkup_end_time, String appoint_number_of_patient, String subtotal, String patient_name, String patient_email, String patient_contact, String appt_date) {
        this.rowid = rowid;
        this.id = id;
        this.itemtype = itemtype;
        this.item_name = item_name;
        this.price = price;
        this.qty = qty;
        this.category_names = category_names;
        this.company_name = company_name;
        this.item_image_file = item_image_file;
        this.item_image_file_path = item_image_file_path;
        this.speciality_names = speciality_names;
        this.hospital_name = hospital_name;
        this.doctor_profile_image = doctor_profile_image;
        this.doctor_profile_image_path = doctor_profile_image_path;
        this.doctor_id = doctor_id;
        this.week_day = week_day;
        this.checkup_start_time = checkup_start_time;
        this.checkup_end_time = checkup_end_time;
        this.appoint_number_of_patient = appoint_number_of_patient;
        this.subtotal = subtotal;
        this.patient_name = patient_name;
        this.patient_email = patient_email;
        this.patient_contact = patient_contact;
        this.appt_date = appt_date;

    }


    public String getAppoint_number_of_patient_updated() {
        return appoint_number_of_patient_updated;
    }

    public void setAppoint_number_of_patient_updated(String appoint_number_of_patient_updated) {
        this.appoint_number_of_patient_updated = appoint_number_of_patient_updated;
    }

    public String getNo_of_patient_appointed() {
        return no_of_patient_appointed;
    }

    public void setNo_of_patient_appointed(String no_of_patient_appointed) {
        this.no_of_patient_appointed = no_of_patient_appointed;
    }
    public String getAttachment_link() {
        return attachment_link;
    }

    public void setAttachment_link(String attachment_link) {
        this.attachment_link = attachment_link;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCategory_names() {
        return category_names;
    }

    public void setCategory_names(String category_names) {
        this.category_names = category_names;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getItem_image_file() {
        return item_image_file;
    }

    public void setItem_image_file(String item_image_file) {
        this.item_image_file = item_image_file;
    }

    public String getItem_image_file_path() {
        return item_image_file_path;
    }

    public void setItem_image_file_path(String item_image_file_path) {
        this.item_image_file_path = item_image_file_path;
    }

    public String getSpeciality_names() {
        return speciality_names;
    }

    public void setSpeciality_names(String speciality_names) {
        this.speciality_names = speciality_names;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoctor_profile_image() {
        return doctor_profile_image;
    }

    public void setDoctor_profile_image(String doctor_profile_image) {
        this.doctor_profile_image = doctor_profile_image;
    }

    public String getDoctor_profile_image_path() {
        return doctor_profile_image_path;
    }

    public void setDoctor_profile_image_path(String doctor_profile_image_path) {
        this.doctor_profile_image_path = doctor_profile_image_path;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public String getCheckup_start_time() {
        return checkup_start_time;
    }

    public void setCheckup_start_time(String checkup_start_time) {
        this.checkup_start_time = checkup_start_time;
    }

    public String getCheckup_end_time() {
        return checkup_end_time;
    }

    public void setCheckup_end_time(String checkup_end_time) {
        this.checkup_end_time = checkup_end_time;
    }

    public String getAppoint_number_of_patient() {
        return appoint_number_of_patient;
    }

    public void setAppoint_number_of_patient(String appoint_number_of_patient) {
        this.appoint_number_of_patient = appoint_number_of_patient;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getPatient_contact() {
        return patient_contact;
    }

    public void setPatient_contact(String patient_contact) {
        this.patient_contact = patient_contact;
    }

    public String getAppt_date() {
        return appt_date;
    }

    public void setAppt_date(String appt_date) {
        this.appt_date = appt_date;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "rowid='" + rowid + '\'' +
                ", id='" + id + '\'' +
                ", itemtype='" + itemtype + '\'' +
                ", item_name='" + item_name + '\'' +
                ", price='" + price + '\'' +
                ", qty='" + qty + '\'' +
                ", category_names='" + category_names + '\'' +
                ", company_name='" + company_name + '\'' +
                ", item_image_file='" + item_image_file + '\'' +
                ", item_image_file_path='" + item_image_file_path + '\'' +
                ", speciality_names='" + speciality_names + '\'' +
                ", hospital_name='" + hospital_name + '\'' +
                ", doctor_profile_image='" + doctor_profile_image + '\'' +
                ", doctor_profile_image_path='" + doctor_profile_image_path + '\'' +
                ", doctor_id='" + doctor_id + '\'' +
                ", week_day='" + week_day + '\'' +
                ", checkup_start_time='" + checkup_start_time + '\'' +
                ", checkup_end_time='" + checkup_end_time + '\'' +
                ", appoint_number_of_patient='" + appoint_number_of_patient + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", patient_name='" + patient_name + '\'' +
                ", patient_email='" + patient_email + '\'' +
                ", patient_contact='" + patient_contact + '\'' +
                ", appt_date='" + appt_date + '\'' +
                ", attachment_link='" + attachment_link + '\'' +
                '}';
    }
}
