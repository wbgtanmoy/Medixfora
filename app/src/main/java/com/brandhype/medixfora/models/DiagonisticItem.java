package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 08-08-2017.
 */

public class DiagonisticItem {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    private String title;
    private String id;
    private String _image;
    private String image_name;
}
