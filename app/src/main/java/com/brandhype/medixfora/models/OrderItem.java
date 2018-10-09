package com.brandhype.medixfora.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmoy Banerjee on 14-11-2017.
 */

public class OrderItem implements Serializable {
    //{"order_number":"MDX_20171110114","order_date":"2017-11-10 11:47:07","total_amount":"1595",
    // "transaction_id":null,"is_payment_completed":"N","order_status":"PENDING","order_details":



    String id;
    String order_number,order_date,total_amount,transaction_id,is_payment_completed,order_status;
    List<OrderDetails> order_details = new ArrayList<>();

    public OrderItem() {
    }

    public OrderItem(String order_number, String order_date, String total_amount, String transaction_id, String is_payment_completed, String order_status, List<OrderDetails> order_details) {
        this.order_number = order_number;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.transaction_id = transaction_id;
        this.is_payment_completed = is_payment_completed;
        this.order_status = order_status;
        this.order_details = order_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getIs_payment_completed() {
        return is_payment_completed;
    }

    public void setIs_payment_completed(String is_payment_completed) {
        this.is_payment_completed = is_payment_completed;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OrderDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<OrderDetails> order_details) {
        this.order_details = order_details;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "order_number='" + order_number + '\'' +
                ", order_date='" + order_date + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", is_payment_completed='" + is_payment_completed + '\'' +
                ", order_status='" + order_status + '\'' +
                ", order_details=" + order_details +
                '}';
    }
}
