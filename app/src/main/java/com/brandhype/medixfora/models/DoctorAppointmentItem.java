package com.brandhype.medixfora.models;

import java.io.Serializable;

/**
 * Created by Tanmoy Banerjee on 13-11-2017.
 */

public class DoctorAppointmentItem implements Serializable {

    String doctor_appointment_date,checkup_start_time,checkup_end_time,first_name,last_name,address;
    String city_name;
    String country_name;
    String postcode;
    String contact_number;
    String email;
    String profile_image_file;
    String item_price;
    String doctor_appointment_format_date;



    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getDoctor_appointment_format_date() {
        return doctor_appointment_format_date;
    }

    public void setDoctor_appointment_format_date(String doctor_appointment_format_date) {
        this.doctor_appointment_format_date = doctor_appointment_format_date;
    }


    public DoctorAppointmentItem() {
    }

    public DoctorAppointmentItem(String doctor_appointment_date, String checkup_start_time, String checkup_end_time, String first_name, String last_name, String address, String city_name, String country_name, String postcode, String contact_number, String email, String profile_image_file) {
        this.doctor_appointment_date = doctor_appointment_date;
        this.checkup_start_time = checkup_start_time;
        this.checkup_end_time = checkup_end_time;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.city_name = city_name;
        this.country_name = country_name;
        this.postcode = postcode;
        this.contact_number = contact_number;
        this.email = email;
        this.profile_image_file = profile_image_file;
    }

    public String getDoctor_appointment_date() {
        return doctor_appointment_date;
    }

    public void setDoctor_appointment_date(String doctor_appointment_date) {
        this.doctor_appointment_date = doctor_appointment_date;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image_file() {
        return profile_image_file;
    }

    public void setProfile_image_file(String profile_image_file) {
        this.profile_image_file = profile_image_file;
    }

    @Override
    public String toString() {
        return "DoctorAppointmentItem{" +
                "doctor_appointment_date='" + doctor_appointment_date + '\'' +
                ", checkup_start_time='" + checkup_start_time + '\'' +
                ", checkup_end_time='" + checkup_end_time + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", city_name='" + city_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", postcode='" + postcode + '\'' +
                ", contact_number='" + contact_number + '\'' +
                ", email='" + email + '\'' +
                ", profile_image_file='" + profile_image_file + '\'' +
                ", item_price='" + item_price + '\'' +
                ", doctor_appointment_format_date='" + doctor_appointment_format_date + '\'' +
                '}';
    }
}
