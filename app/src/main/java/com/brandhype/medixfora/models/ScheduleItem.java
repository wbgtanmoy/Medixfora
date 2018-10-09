package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 08-09-2017.
 */

public class ScheduleItem {
    String id,doctor_id,week_day,checkup_start_time,checkup_end_time,appoint_number_of_patient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
