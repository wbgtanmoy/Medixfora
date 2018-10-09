package com.brandhype.medixfora.models;

/**
 * Created by tanmoy on 29/07/15.
 */
public class DoctorItem {

    private boolean showNotify;
    private String title;

    private String id;
    private String name;
    private String city_name;
    private String doctor_fees;
    private String address;
    private String speciality_name;
    private String speciality_id;
    private String degree;
    private String image;
    private String about_doctor;

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    private String is_verified;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    private String designation;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDoctor_fees() {
        return doctor_fees;
    }

    public void setDoctor_fees(String doctor_fees) {
        this.doctor_fees = doctor_fees;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public void setSpeciality_name(String speciality_name) {
        this.speciality_name = speciality_name;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(String speciality_id) {
        this.speciality_id = speciality_id;
    }


    public DoctorItem() {    }

    public DoctorItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout_doctor() {
        return about_doctor;
    }

    public void setAbout_doctor(String about_doctor) {
        this.about_doctor = about_doctor;
    }

    @Override
    public String toString() {
        return "DoctorItem{" +
                "showNotify=" + showNotify +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", doctor_fees='" + doctor_fees + '\'' +
                ", address='" + address + '\'' +
                ", speciality_name='" + speciality_name + '\'' +
                ", speciality_id='" + speciality_id + '\'' +
                ", degree='" + degree + '\'' +
                ", image='" + image + '\'' +
                ", about_doctor='" + about_doctor + '\'' +
                ", is_verified='" + is_verified + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
