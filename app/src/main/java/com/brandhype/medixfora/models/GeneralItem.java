package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 11-09-2017.
 */

public class GeneralItem {

    String id;
    String title;
    String subtitle;
    String image;
    Boolean isSelected;


    public GeneralItem(){}

    public GeneralItem(String id, String title, String subtitle, Boolean isSelected) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.isSelected = isSelected;
    }

    public GeneralItem(String id, String title, String subtitle, String image, Boolean isSelected) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.isSelected = isSelected;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
