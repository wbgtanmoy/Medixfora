package com.brandhype.medixfora.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.httphandler.MultipartUtility2;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.AppointmentItem;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by Tanmoy Banerjee on 17-08-2017.
 */

public class UploadFileAsynctask extends AsyncTask<String, String, String> {
    String file_name="",server_response = "";

    // you may separate this or combined to caller class.
    public AsyncResponse delegate = null;
    ProgressDialog pDialog;
    Context context;

    public UploadFileAsynctask(Context context, AsyncResponse delegate){
        this.delegate = delegate;
        this.context=context;
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

        String bookingSlotID="0";
        String user_id="0";
        File uploadFile1=new File("");
        try {

            file_name=""+params[0];

            AppPreferences pref3 = new AppPreferences(context);
            AppointmentItem appointmentItem3=new AppointmentItem();

            try{
                String jsonStr=pref3.LoadPreferences(AppPreferences.Storage.APPOINTMENTDATA.name());
                Gson gson = new Gson();
                appointmentItem3 = gson.fromJson(jsonStr,AppointmentItem.class);

                //Toast.makeText(context, "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
                Log.d("@$ appointment data ",appointmentItem3.toString());

                String gsonobj=gson.toJson(appointmentItem3);
                Log.d("@$ appointment data ",gsonobj);

                bookingSlotID=appointmentItem3.getSlotID();
                user_id=appointmentItem3.getPatient_id();

            }catch(Exception e){e.printStackTrace();}



            String token= NetworkIOConstant.CS_Token.TOKEN;
            //http://brandhypedigital.in/demo/medixfora/restapi/custom_cart/add_to_cart/
            String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/add_to_cart";

            Log.d("@ hitting at ", url);
            Log.d("@ uploading file:", params[0] );

            String charset = "UTF-8";

            if(!file_name.equals("") && !file_name.equals("Attach document") && !file_name.equals("null") && !file_name.equals(null)){
                uploadFile1 = new File(params[0]);
            }

            String requestURL = url;

            MultipartUtility2 multipart2 = new MultipartUtility2(requestURL, charset);

            multipart2.addHeaderField("Content-Type", "application/json");
            //multipart.addHeaderField("Test-Header", "Header-Value");


            multipart2.addFormField("apitoken", token);
            multipart2.addFormField("device_type", NetworkIOConstant.CS_Type.device_type);

            String slot_id=bookingSlotID;
            multipart2.addFormField("item_id", slot_id);
            multipart2.addFormField("item_qty", "1");
            multipart2.addFormField("itemtype", "doct");
            multipart2.addFormField("user_id", user_id);


            multipart2.addFormField("appt_date", appointmentItem3.getAppt_date());
            multipart2.addFormField("doctor_id", appointmentItem3.getDoctor_id());

            multipart2.addFormField("doctor_fees", appointmentItem3.getFees());

            multipart2.addFormField("patient_contact", appointmentItem3.getPatient_contact());
            multipart2.addFormField("patient_email", appointmentItem3.getPatient_email());
            multipart2.addFormField("patient_name", appointmentItem3.getPatient_name());
            multipart2.addFormField("patient_condition", appointmentItem3.getPatient_health());


            if(!file_name.equals("") && !file_name.equals("Attach document") && !file_name.equals("null") && !file_name.equals(null))
            {
                Log.d("@$ Upload proceed ","uploading WITH file attachement " + file_name );
                multipart2.addFilePart("fileToUpload", uploadFile1);
            }
            else
            {
                Log.d("@$ Upload proceed ","uploading WITHOUT file" + file_name );
            }


            String responseJson = multipart2.finishPost();
            Log.d("@ SERVER REPLIED:",responseJson.toString());

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