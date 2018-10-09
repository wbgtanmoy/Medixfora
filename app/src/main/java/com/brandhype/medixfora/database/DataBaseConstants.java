package com.brandhype.medixfora.database;

/**
 * Created by Tanmoy Banerjee on 18-10-2017.
 */

public class DataBaseConstants {

    static String DATABASE_NAME 			= "db_medixfora.db.sqlite";
    static int DATABASE_VERSION 			= 1;


    public interface TableMyRequest{


        String TABLE_NAME			= "MyReqest";

        String ID			        = "id";
        String PATIENT_ID	        = "patient_id";
        String ITEM_ID			    = "item_id";
        String ITEM_TYPE			= "item_type";
        String REQ_DATETIME			= "req_datetime";
        String REQ_ADDRESS			= "req_address";
        String REQ_MSG			    = "req_msg";
        String SUBMITTED_DATETIME	= "submitted_datetime";
        String PATIENT_NAME			= "patient_name";
        String ITEM_NAME			= "item_name";
        String SHORT_DESC			= "short_desc";
        String PRICE			    = "price";
        String ITEM_IMAGE_FILE		= "item_image_file";

    }

}
