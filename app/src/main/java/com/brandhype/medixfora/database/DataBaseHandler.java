package com.brandhype.medixfora.database;

/**
 * Created by Tanmoy Banerjee on 18-10-2017.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.brandhype.medixfora.models.RequestItem;

public class DataBaseHandler {

    private Context m_Context = null;
    private DataBase m_Database = null;
    private static SQLiteDatabase m_SqliteDatabase = null;
    private static DataBaseHandler dbHandler = null;


    private DataBaseHandler(Context context) {
        m_Context = context;
        m_Database = new DataBase(m_Context);

    }

    public static DataBaseHandler getInstance(Context context) {
        if (dbHandler == null) {
            dbHandler = new DataBaseHandler(context);
        }
        return dbHandler;
    }


    public void open() throws SQLException {
        m_SqliteDatabase = m_Database.getWritableDatabase();
    }


    public void close() {
         if (m_Database != null)
        m_Database.close();
    }

    Locale locale = new Locale("en");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale);



    public void ExecuteRequest(String query) {
        System.out.println("ExecuteRequest: " + query);
        m_SqliteDatabase.execSQL(query);
    }

    //##########################################################################
    //=======public methods to select,insert,update into database===============
    //##########################################################################

    public void insertorupdateMyRequest(List<RequestItem> reqList) {

        SQLiteStatement statement = null;
        try {
            m_SqliteDatabase.beginTransaction();
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT OR REPLACE INTO "
                    + DataBaseConstants.TableMyRequest.TABLE_NAME + "(");
            builder.append(DataBaseConstants.TableMyRequest.ID + ",");
            builder.append(DataBaseConstants.TableMyRequest.PATIENT_ID + ",");
            builder.append(DataBaseConstants.TableMyRequest.ITEM_ID + ",");
            builder.append(DataBaseConstants.TableMyRequest.ITEM_TYPE + ",");
            builder.append(DataBaseConstants.TableMyRequest.REQ_DATETIME + ",");
            builder.append(DataBaseConstants.TableMyRequest.REQ_ADDRESS + ",");
            builder.append(DataBaseConstants.TableMyRequest.REQ_MSG + ",");
            builder.append(DataBaseConstants.TableMyRequest.SUBMITTED_DATETIME + ",");
            builder.append(DataBaseConstants.TableMyRequest.PATIENT_NAME + ",");
            builder.append(DataBaseConstants.TableMyRequest.ITEM_NAME + ",");
            builder.append(DataBaseConstants.TableMyRequest.SHORT_DESC + ",");
            builder.append(DataBaseConstants.TableMyRequest.PRICE + ",");
            builder.append(DataBaseConstants.TableMyRequest.ITEM_IMAGE_FILE + ") ");

            builder.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            statement = m_SqliteDatabase.compileStatement(builder.toString());
            for (RequestItem reqObj : reqList) {
                statement.clearBindings();
                statement.bindString(1, reqObj.getId());
                statement.bindString(2, reqObj.getPatient_id());
                statement.bindString(3, reqObj.getItem_id());
                statement.bindString(4, reqObj.getItem_type());
                statement.bindString(5, reqObj.getReq_datetime());
                statement.bindString(6, reqObj.getReq_address());
                statement.bindString(7, reqObj.getReq_msg());
                statement.bindString(8, reqObj.getSubmitted_datetime());
                statement.bindString(9, reqObj.getPatient_name());
                statement.bindString(10, reqObj.getItem_name());
                statement.bindString(11, reqObj.getShort_desc());
                statement.bindString(12, reqObj.getPrice());
                statement.bindString(13, reqObj.getItem_image_file());
                statement.execute();
            }
            m_SqliteDatabase.setTransactionSuccessful();
            //System.out.println("@ Request Data inserted .....................");
            Log.d("@ DB", reqList.size()+ " Request Data inserted ............. ");

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            m_SqliteDatabase.endTransaction();
        }
    }



    public void deleteRequest(String id) {

        long result = -1;
        try {

            String[] whereArgs = { id };
            String whereClause = DataBaseConstants.TableMyRequest.ID
                    + "=?";
            result = m_SqliteDatabase.delete(
                    DataBaseConstants.TableMyRequest.TABLE_NAME, whereClause,
                    whereArgs);

            //System.out.println("@ ---- request  deleted  from local database---------"+ id );
            Log.d("@ DB ", " request  deleted  from local database---------"+ id );

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {

        }

    }


    public void deleteAllRequest() {
        long result = -1;
        try {
            result = m_SqliteDatabase.delete(DataBaseConstants.TableMyRequest.TABLE_NAME,null ,null);
            //System.out.println("----all request  deleted  from local database---------");
            Log.d("@ DB ", " all request  deleted  from local databas ");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
        }
    }



    public RequestItem getRequestById(String id) {

        RequestItem requestItem=null;

        Cursor cursor = null;
        try {
            String sql = "SELECT * FROM "
                    + DataBaseConstants.TableMyRequest.TABLE_NAME + " WHERE "
                    + DataBaseConstants.TableMyRequest.ID + "='" + id
                    + "'";

            cursor = m_SqliteDatabase.rawQuery(sql, null);

            if (cursor != null) {
                Log.d("@ DB", " TableMyRequest record count : "+ cursor.getCount());
                //return cursor.getCount();

                if (cursor != null && cursor.getCount() > 0)
                {

                    cursor.moveToFirst();
                    do {
                        requestItem=new RequestItem();

                        requestItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ID)));
                        requestItem.setPatient_id(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PATIENT_ID)));
                        requestItem.setItem_id(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_ID)));
                        requestItem.setItem_type(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_TYPE)));
                        requestItem.setReq_datetime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_DATETIME)));
                        requestItem.setReq_address(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_ADDRESS)));
                        requestItem.setReq_msg(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_MSG)));
                        requestItem.setSubmitted_datetime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.SUBMITTED_DATETIME)));
                        requestItem.setPatient_name(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PATIENT_NAME)));
                        requestItem.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_NAME)));
                        requestItem.setShort_desc(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.SHORT_DESC)));
                        requestItem.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PRICE)));
                        requestItem.setItem_image_file(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_IMAGE_FILE)));

                    } while (cursor.moveToNext());

                    return requestItem;
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }

        return requestItem;
    }

    //------------------return country list-------------------------
    public ArrayList<RequestItem> getRequestList() {

        ArrayList<RequestItem> reqList=null;

        Cursor cursor = null;
        try {
            String sql = "SELECT * FROM "
                    + DataBaseConstants.TableMyRequest.TABLE_NAME + "";

            cursor = m_SqliteDatabase.rawQuery(sql, null);

            if (cursor != null) {
                Log.d("@ DB", " TableMyRequest record count : "+ cursor.getCount());
                //return cursor.getCount();

                reqList=new ArrayList<RequestItem>(cursor.getCount());;
                if (cursor != null && cursor.getCount() > 0)
                {

                    cursor.moveToFirst();
                    do {

                        RequestItem requestItem=new RequestItem();

                        requestItem.setId(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ID)));
                        requestItem.setPatient_id(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PATIENT_ID)));
                        requestItem.setItem_id(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_ID)));
                        requestItem.setItem_type(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_TYPE)));
                        requestItem.setReq_datetime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_DATETIME)));
                        requestItem.setReq_address(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_ADDRESS)));
                        requestItem.setReq_msg(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.REQ_MSG)));
                        requestItem.setSubmitted_datetime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.SUBMITTED_DATETIME)));
                        requestItem.setPatient_name(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PATIENT_NAME)));
                        requestItem.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_NAME)));
                        requestItem.setShort_desc(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.SHORT_DESC)));
                        requestItem.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.PRICE)));
                        requestItem.setItem_image_file(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.TableMyRequest.ITEM_IMAGE_FILE)));

                        reqList.add( requestItem) ;

                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }

        return reqList;
    }


}
