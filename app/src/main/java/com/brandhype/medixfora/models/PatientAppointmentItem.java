package com.brandhype.medixfora.models;

import java.io.Serializable;

/**
 * Created by Tanmoy Banerjee on 13-11-2017.
 */

public class PatientAppointmentItem implements Serializable {


    String doctor_appointment_date,doctor_name,email,address,country_name,city_name;
    String postcode,contact_number,degree,designation,doctor_profile_image,week_day,checkup_start_time,checkup_end_time;



    String item_price;


    public PatientAppointmentItem() {
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public PatientAppointmentItem(String doctor_appointment_date, String doctor_name, String email, String address, String country_name, String city_name, String postcode, String contact_number, String degree, String designation, String doctor_profile_image, String week_day, String checkup_start_time, String checkup_end_time) {
        this.doctor_appointment_date = doctor_appointment_date;
        this.doctor_name = doctor_name;
        this.email = email;
        this.address = address;
        this.country_name = country_name;
        this.city_name = city_name;
        this.postcode = postcode;
        this.contact_number = contact_number;
        this.degree = degree;
        this.designation = designation;
        this.doctor_profile_image = doctor_profile_image;
        this.week_day = week_day;
        this.checkup_start_time = checkup_start_time;
        this.checkup_end_time = checkup_end_time;
    }


    public String getDoctor_appointment_date() {
        return doctor_appointment_date;
    }

    public void setDoctor_appointment_date(String doctor_appointment_date) {
        this.doctor_appointment_date = doctor_appointment_date;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDoctor_profile_image() {
        return doctor_profile_image;
    }

    public void setDoctor_profile_image(String doctor_profile_image) {
        this.doctor_profile_image = doctor_profile_image;
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

    @Override
    public String toString() {
        return "PatientAppointmentItem{" +
                "doctor_appointment_date='" + doctor_appointment_date + '\'' +
                ", doctor_name='" + doctor_name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", country_name='" + country_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", postcode='" + postcode + '\'' +
                ", contact_number='" + contact_number + '\'' +
                ", degree='" + degree + '\'' +
                ", designation='" + designation + '\'' +
                ", doctor_profile_image='" + doctor_profile_image + '\'' +
                ", week_day='" + week_day + '\'' +
                ", checkup_start_time='" + checkup_start_time + '\'' +
                ", checkup_end_time='" + checkup_end_time + '\'' +
                ", item_price='" + item_price + '\'' +
                '}';
    }
}
