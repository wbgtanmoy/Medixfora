package com.brandhype.medixfora.database;

/**
 * Created by Tanmoy Banerjee on 18-10-2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {


    public DataBase(Context context) {

        super(context, DataBaseConstants.DATABASE_NAME, null,
                DataBaseConstants.DATABASE_VERSION);

        /////////-----************----open this for test purpose -- db outside app---***********//////////

       /*super(context, "/mnt/sdcard/"+DataBaseConstants.DATABASE_NAME, null,
				 DataBaseConstants.DATABASE_VERSION);*/

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + DataBaseConstants.TableMyRequest.TABLE_NAME
                + " ( " + DataBaseConstants.TableMyRequest.ID + " TEXT NOT NULL, "
                + DataBaseConstants.TableMyRequest.PATIENT_ID + " TEXT, "
                + DataBaseConstants.TableMyRequest.ITEM_ID + " TEXT, "
                + DataBaseConstants.TableMyRequest.ITEM_TYPE + " TEXT, "
                + DataBaseConstants.TableMyRequest.REQ_DATETIME + " TEXT, "
                + DataBaseConstants.TableMyRequest.REQ_ADDRESS + " TEXT,"
                + DataBaseConstants.TableMyRequest.REQ_MSG + " TEXT,"
                + DataBaseConstants.TableMyRequest.SUBMITTED_DATETIME + " TEXT,"
                + DataBaseConstants.TableMyRequest.PATIENT_NAME + " TEXT,"
                + DataBaseConstants.TableMyRequest.ITEM_NAME + " TEXT,"
                + DataBaseConstants.TableMyRequest.SHORT_DESC + " TEXT,"
                + DataBaseConstants.TableMyRequest.PRICE + " TEXT,"
                + DataBaseConstants.TableMyRequest.ITEM_IMAGE_FILE + " TEXT"
                //+ " PRIMARY KEY ( " + DataBaseConstants.TableMyRequest.ID + " )"
                +" ) " );

        Log.d("@ DB", " Table request created ");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TableMyRequest.TABLE_NAME);
            onCreate(db);
        }
    }
}
