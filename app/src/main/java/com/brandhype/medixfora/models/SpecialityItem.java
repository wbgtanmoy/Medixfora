package com.brandhype.medixfora.models;

/**
 * Created by tanmoy on 29/07/15.
 */
public class SpecialityItem {

    private boolean showNotify;
    private String title;
    private String id;
    private String speciality_image;
    private String image_name;

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSpeciality_image() {
        return speciality_image;
    }

    public void setSpeciality_image(String speciality_image) {
        this.speciality_image = speciality_image;
    }


    public SpecialityItem() {

    }

    public SpecialityItem(boolean showNotify, String title) {
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
}
