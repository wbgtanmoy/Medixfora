package com.brandhype.medixfora.models;

/**
 * Created by Tanmoy Banerjee on 10-10-2017.
 */

public class MessageItem {
    String id;
    String question;
    String answeer;
    String dtime;


    public MessageItem() {
    }

    public MessageItem(String id, String question, String answeer) {
        this.id = id;
        this.question = question;
        this.answeer = answeer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnsweer() {
        return answeer;
    }

    public void setAnsweer(String answeer) {
        this.answeer = answeer;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }
}
