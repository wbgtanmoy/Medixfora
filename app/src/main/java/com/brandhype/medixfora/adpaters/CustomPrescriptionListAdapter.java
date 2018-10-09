package com.brandhype.medixfora.adpaters;

/**
 * Created by Tanmoy Banerjee on 28-08-2017.
 */



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.asynctasks.UploadCartItemAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.fragments.PrescriptionFragment;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.AppointmentItem;
import com.brandhype.medixfora.models.PrescriptionItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

public class CustomPrescriptionListAdapter extends BaseAdapter{

    //List<PrescriptionItem> data = Collections.emptyList();
    private ArrayList<PrescriptionItem> arraylist;

    String [] result;
    Context context;
    List<PrescriptionItem> data=Collections.emptyList();
    AlertDialog alertDialogMain ;
    PrescriptionItem pitem;
    PrescriptionFragment frag_context;

    private static LayoutInflater inflater=null;


    public CustomPrescriptionListAdapter(Context context,PrescriptionFragment pf, String[] prgmNameList, List<PrescriptionItem> dataS, AlertDialog alertDialog ) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.context=context;
        this.data=dataS;
        this.alertDialogMain=alertDialog;
        this.frag_context=pf;

        this.arraylist = new ArrayList<PrescriptionItem>();
        this.arraylist.addAll(dataS);

        inflater = ( LayoutInflater )context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        Button btn;
        ImageView presc_addcart,presc_remove,presc_download;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView=convertView;

        if(convertView==null) {
            rowView = inflater.inflate(R.layout.oldprescription_row, null);
            holder.tv = (TextView) rowView.findViewById(R.id.prescr_title);
            //holder.btn = (Button) rowView.findViewById(R.id.presc_addcart_button);
            holder.presc_remove = (ImageView) rowView.findViewById(R.id.presc_remove);
            holder.presc_addcart = (ImageView) rowView.findViewById(R.id.presc_addcart);
            holder.presc_download = (ImageView) rowView.findViewById(R.id.presc_download);

            rowView.setTag( holder );
        }
        else
        {
            holder=(Holder)rowView.getTag();
        }


        /*try {
            PrescriptionItem xpitem = data.get(position);
            holder.tv.setText("" + xpitem.getTitle());
        }catch(Exception e){ e.printStackTrace();}*/

        holder.tv.setText(result[position]);


        holder.presc_remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pitem=data.get(position);
                Log.d("@ Saved pres",pitem.toString());


                //------------------------Dialog---------------------------------------------
                final Dialog alertDialog = new Dialog(context);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                alertDialog.setContentView(R.layout.custom_alertdialog);

                TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);

                txt_main.setText("Sure to delete this Saved prescription ?");

                Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
                Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

                _yes.setEnabled(true);
                _cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //alertDialogMain.dismiss();
                        alertDialog.dismiss();
                        //--Do nothing
                    }
                });
                _yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialogMain.dismiss();
                        alertDialog.dismiss();
                        //**************perform cart add operation************
                        deleteOldPrescription(pitem);
                        //****************************************************
                    }
                });

                if (alertDialog != null && !alertDialog.isShowing())
                    alertDialog.show();
                //----------------------------------------------------------------------

            }
        });

        holder.presc_addcart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "Prescription "+result[position], Toast.LENGTH_LONG).show();
                pitem=data.get(position);
                Log.d("@ Saved pres",pitem.toString());


                //------------------------Dialog---------------------------------------------
                final Dialog alertDialog = new Dialog(context);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                alertDialog.setContentView(R.layout.custom_alertdialog);

                TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);

                txt_main.setText("Sure to send  this prescription to admin ?");

                Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
                Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

                _yes.setEnabled(true);
                _cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //--Do nothing
                    }
                });
                _yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialogMain.dismiss();
                        alertDialog.dismiss();

                        //------passing selected prescription object to fragment--
                        frag_context.selected_prescriptionitem=pitem;
                        frag_context.prescription_title.setText(pitem.getTitle().toString());
                        //**************perform cart add operation************
                        //addOldPrescriptionToCart(pitem, false, "");
                        //****************************************************
                    }
                });

                if (alertDialog != null && !alertDialog.isShowing())
                    alertDialog.show();
                //----------------------------------------------------------------------


            }
        });

        holder.presc_download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "Prescription "+result[position], Toast.LENGTH_LONG).show();
                pitem=data.get(position);
                Log.d("@ Saved pres",pitem.toString());

                String prescription_file_path=pitem.getPrescription_file_path();
                viewFile(prescription_file_path);
                alertDialogMain.dismiss();

            }
        });


        return rowView;
    }

    private void addOldPrescriptionToCart(PrescriptionItem prescriptionItem, Boolean isNew, String filepath)
    {

        Log.d("@ Saved pres itm",prescriptionItem.toString()+"isNew:"+isNew+"File:"+filepath);
        //-------------add to cart-------------
        PrescriptionItem pitem=prescriptionItem;
        String filename="";
        String user_id="0";
        AppPreferences pref3 = new AppPreferences(context);
        AppointmentItem appointmentItem3=new AppointmentItem();

        AppPreferences pref;
        JSONObject patient;

        try{
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            user_id=patient.getString("id");

        }catch(Exception e){e.printStackTrace();}


        //http://brandhypedigital.in/demo/medixfora/restapi/custom_cart/add_to_cart/
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/add_to_cart/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        if (!isNew ){   //Saved prescription
            postDataParams.put("item_id", pitem.getId());
            filename=filepath;
        }
        else {          //new prescription
            postDataParams.put("item_id", "999999");
        }

        //postDataParams.put("prescription_id", pitem.getId());
        postDataParams.put("item_qty", "1");
        postDataParams.put("itemtype", "MP"); // itm for others
        postDataParams.put("user_id", pitem.getPatient_id());


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","add to cart : "+ url);
        Log.d("@ cart params ", "add to cart: "+ postJson.toString());



        UploadCartItemAsynctask uploadcartitemAsynctask=new UploadCartItemAsynctask(context,postDataParams,filename,url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  add to cart", result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = commonutils.replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1")  && api_syntax_success.equals("1"))
                    {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Prescription Added to Cart", Toast.LENGTH_SHORT).show();
                                Log.d("@ Success","Prescription Added to Cart");
                                ((MainActivity)context).getCartCountFromServer();

                            }
                        });


                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Add To Cart Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Add To Cart Failed: "+err);
                            }
                        });

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }

                //-----------------------------------

            }
        });


        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                uploadcartitemAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadcartitemAsynctask.execute();//serial
            }
        }
        //-------------------------------------

    }

    private void deleteOldPrescription(PrescriptionItem prescriptionItem)
    {

        Log.d("@ Saved pres itm",prescriptionItem.toString());
        //-------------add to cart-------------
        PrescriptionItem pitem=prescriptionItem;
        String filename="";
        String user_id="0";
        AppPreferences pref3 = new AppPreferences(context);
        AppointmentItem appointmentItem3=new AppointmentItem();

        AppPreferences pref;
        JSONObject patient;

        try{
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            user_id=patient.getString("id");

        }catch(Exception e){e.printStackTrace();}


        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/delete_prescription/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("prescriptionID", pitem.getId());
        postDataParams.put("patientID", pitem.getPatient_id());


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","Saved prescription  : "+ url);
        Log.d("@  params ", "Saved prescription: "+ postJson.toString());

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (noProgressDialog == 1) {
                            pdspinnerGeneral.dismiss();
                        }
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Saved prescription Deleted ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Saved prescription Deleted");

                                notifyDataSetChanged();

                                frag_context.chooseOldPrescription();

                            }
                        });

                    } else {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Cannot delete, "+err, Toast.LENGTH_LONG).show();
                                Log.e("Error","Saved prescription  Deletion failed:"+err);
                                frag_context.chooseOldPrescription();
                            }
                        });

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                }
            }
        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }



    }


    public void viewFile(String url)
    {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(url));
        Log.d("@ FILE attachment","File extension: "+fileExt(url) + "MIMETYPE: "+ mimeType +"Url: "+url);
        newIntent.setDataAndType(Uri.parse(url),mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (Exception e) {
            Toast.makeText(context, "No Application installed for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }

            return ext.toLowerCase();

        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
            Log.i("@ filter", "No text ");
        }
        else
        {
            //Log.i("@ filter", "arraylist size >> "+arraylist.size());
            for (int i=0;i<arraylist.size();i++)
            {
                PrescriptionItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getTitle().toLowerCase().contains(charText))
                {
                    Log.i("@ filter", " matched:" + wp.getTitle());
                    data.add(wp);
                    Log.i("@ filter size ", ">"+data.size());
                }
            }
        }
        this.notifyDataSetChanged();
    }




}
