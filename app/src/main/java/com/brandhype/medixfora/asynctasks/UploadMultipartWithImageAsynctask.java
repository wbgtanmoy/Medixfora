package com.brandhype.medixfora.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.httphandler.MultipartUtility2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanmoy Banerjee on 17-08-2017.
 */

public class UploadMultipartWithImageAsynctask extends AsyncTask<String, String, String> {

    String file_name="",server_response = "";

    public AsyncResponse delegate = null;
    ProgressDialog pDialog;
    Context context;
    String url;
    HashMap<String, String> map;
    File uploadFile1;
    String dialogMessage;

    ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();

    public UploadMultipartWithImageAsynctask(Context context,String dialogMsg, HashMap<String, String> map, ArrayList<HashMap<String, String>> fileList, String url, AsyncResponse delegate){
        this.delegate = delegate;
        this.context=context;
        this.map=map;
        this.url=url;
        this.fileList=fileList;
        this.dialogMessage=dialogMsg;
    }

    public UploadMultipartWithImageAsynctask(Context context, HashMap<String, String> map, ArrayList<HashMap<String, String>> fileList, String url, AsyncResponse delegate){
        this.delegate = delegate;
        this.context=context;
        this.map=map;
        this.url=url;
        this.fileList=fileList;
        this.dialogMessage="Please wait...";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(dialogMessage);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String[] params) {

        try{

            String token= NetworkIOConstant.CS_Token.TOKEN;

            System.out.println("@ asynctask hitting at "+ url );
            String charset = "UTF-8";
            String requestURL = url;

            MultipartUtility2 multipart2 = new MultipartUtility2(requestURL, charset);

            multipart2.addHeaderField("Content-Type", "application/json");
            //multipart.addHeaderField("Test-Header", "Header-Value");


            multipart2.addFormField("apitoken", token);
            multipart2.addFormField("device_type", "1");

            //-- adding form fields to the multipart entity------------------------
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                multipart2.addFormField(entry.getKey(),entry.getValue());
            }

            //---------adding filefield to the multipart entity if present------------------

            if(fileList !=null && fileList.size()>0) {

                for (HashMap<String, String> map : fileList) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        Log.d("@ File details : ", entry.getKey() + " => " + entry.getValue());
                        uploadFile1 = new File(entry.getValue());
                        Log.d("@ Upload proceed ", "uploading with file attachement " + entry.getValue());
                        multipart2.addFilePart(entry.getKey(), uploadFile1);
                    }
                }
            }

            //---post to server and get JSON as response ---
            String responseJson = multipart2.finishPost();
            System.out.println("@ SERVER REPLIED:"+ responseJson.toString());

            server_response=responseJson;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return server_response;
    }


    protected void onPostExecute(String result) {
        // dismiss the dialog once got all details
        pDialog.dismiss();
        delegate.processFinish(result);
    }

}