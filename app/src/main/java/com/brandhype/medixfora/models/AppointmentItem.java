package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 04-08-2017.
 */

public class AppointmentItem {

    private String slotID;
    private String start_time;
    private String end_time;
    private String appt_date;
    private String disp_date;
    private String doctor_id;
    private String patient_id;
    private String patient_name;
    private String patient_email;
    private String patient_contact;
    private String patient_health;
    private String fees;





    public AppointmentItem(String slotID, String start_time, String end_time, String appt_date, String disp_date, String doctor_id, String patient_id, String patient_name, String patient_email, String patient_contact, String fees) {
        this.slotID = slotID;
        this.start_time = start_time;
        this.end_time = end_time;
        this.appt_date = appt_date;
        this.disp_date = disp_date;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.patient_email = patient_email;
        this.patient_contact = patient_contact;
        this.fees = fees;
    }


    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getPatient_health() {
        return patient_health;
    }

    public void setPatient_health(String patient_health) {
        this.patient_health = patient_health;
    }



    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }


    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAppt_date() {
        return appt_date;
    }

    public void setAppt_date(String appt_date) {
        this.appt_date = appt_date;
    }

    public String getDispdate() {
        return disp_date;
    }

    public void setDispdate(String dispdate) {
        this.disp_date = dispdate;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
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



    public AppointmentItem() {

    }


    @Override
    public String toString() {
        return "AppointmentItem{" +
                "slotID='" + slotID + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", appt_date='" + appt_date + '\'' +
                ", disp_date='" + disp_date + '\'' +
                ", doctor_id='" + doctor_id + '\'' +
                ", patient_id='" + patient_id + '\'' +
                ", patient_name='" + patient_name + '\'' +
                ", patient_email='" + patient_email + '\'' +
                ", patient_contact='" + patient_contact + '\'' +
                ", patient_health='" + patient_health + '\'' +
                ", fees='" + fees + '\'' +
                '}';
    }
}
