package com.brandhype.medixfora.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.httphandler.MultipartUtility2;
import com.brandhype.medixfora.interfaces.AsyncResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanmoy Banerjee on 17-08-2017.
 */

public class UploadCartItemAsynctask extends AsyncTask<String, String, String> {
    String file_name="",server_response = "";

    // you may separate this or combined to caller class.
    public AsyncResponse delegate = null;
    ProgressDialog pDialog;
    Context context;
    String url,filename;
    HashMap<String, String> map;
    File uploadFile1;



    public UploadCartItemAsynctask(Context context, HashMap<String, String> map,String filename, String url, AsyncResponse delegate){
        this.delegate = delegate;
        this.context=context;
        this.map=map;
        this.url=url;
        this.file_name=filename;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait ... ");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String[] params) {



        try{


            //String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "my_cart/add_to_cart/";
            //String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/add_to_cart/";
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
                Log.d("@ Upload proceed ",entry.getKey()+"-"+entry.getValue() );
            }

            //---------adding filefield to the multipart entity if present------------------
            if(!file_name.equals("") ){
                Log.d("@ Upload proceed ","uploading with file attachement " + file_name );
                uploadFile1 = new File(file_name);

            }

            if(!file_name.equals("") && !file_name.equals("Attach document") )
            {
                Log.d("@ Upload proceed ","uploading with file attachement " + file_name );
                multipart2.addFilePart("fileToUpload", uploadFile1);
            }
            else
            {
                Log.d("@ Upload proceed ","uploading WITHOUT file" + file_name );
            }


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