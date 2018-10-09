package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 28-08-2017.
 */

public class PrescriptionItem {

    String id;
    String title;
    String patient_id;
    String upload_filename;
    String prescription_file_path;



    //patient_id":"22","prescription_title":"Test prescription 1","upload_filename":"1503379242.pdf","uploaded_datetime":"2017-08-28 00:00:00","prescription_file_path":"http:\/\/brandhypedigital.in\/demo\/medixfora\/assets\/uploaded_files\/patient_prescription\/1503379242.pdf"


    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getUpload_filename() {
        return upload_filename;
    }

    public void setUpload_filename(String upload_filename) {
        this.upload_filename = upload_filename;
    }

    public String getPrescription_file_path() {
        return prescription_file_path;
    }

    public void setPrescription_file_path(String prescription_file_path) {
        this.prescription_file_path = prescription_file_path;
    }


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


    public PrescriptionItem(String id, String title) {
        this.id = id;
        this.title = title;
    }
    public PrescriptionItem() {

    }

    @Override
    public String toString() {
        return "PrescriptionItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", patient_id='" + patient_id + '\'' +
                ", upload_filename='" + upload_filename + '\'' +
                ", prescription_file_path='" + prescription_file_path + '\'' +
                '}';
    }
}
