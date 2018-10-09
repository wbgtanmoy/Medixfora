package com.brandhype.medixfora.models;

/**
 * Created by tanmoy on 29/07/15.
 */
public class PharmacyItem {


    private String title;
    private String id;
    private String _image;
    private String image_name;

    public PharmacyItem() {

    }

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

    public String get_Image() {
        return _image;
    }

    public void set_Image(String image) {
        this._image = image;
    }

   public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
