package com.brandhype.medixfora.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.SpecialityItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientEditprofileFragment extends Fragment {


    Context context;
    String Filepath="";


    EditText patient_eitdprof_firstname,patient_editprof_address,patient_editprof_country;
    EditText patient_editprof_citytown,patient_editprof_postcode,patient_editprof_contact,patient_editprof_about,patient_editprof_email;
    //EditText patient_editprof_title,patient_editprof_lastname;

    Button patient_editprof_button;
    ImageView patient_editprof_image,patient_profile_pic_edit;
    TextView patient_image_filename_tv;

    private static final String IMAGE_DIRECTORY = "/medixfora";
    private int GALLERY = 1, CAMERA = 2;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private final static int ALL_PERMISSIONS_RESULT = 107;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    List<String> speciality_stringlist = new ArrayList<String>();
    List<String> hospital_stringlist = new ArrayList<>();

    List<SpecialityItem> speciality_list = new ArrayList<>();
    List<SpecialityItem> hospital_list = new ArrayList<>();


    public PatientEditprofileFragment() {
        // Required empty public constructor
        speciality_list = new ArrayList<>();
        speciality_stringlist = new ArrayList<String>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment_patient_editprofile, container, false);;
        context=getActivity();

        patient_image_filename_tv = (TextView) rootView.findViewById(R.id.patient_image_filename_tv);

        patient_editprof_image = (ImageView) rootView.findViewById(R.id.patient_editprof_image);
        patient_profile_pic_edit = (ImageView) rootView.findViewById(R.id.patient_profile_pic_edit);
        //patient_profile_pic_edit.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        patient_profile_pic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });

        //patient_editprof_title = (EditText) rootView.findViewById(R.id.patient_editprof_tile);
        patient_eitdprof_firstname = (EditText) rootView.findViewById(R.id.patient_editprof_firstname);
        //patient_editprof_lastname = (EditText) rootView.findViewById(R.id.patient_editprof_lastname);
        patient_editprof_address = (EditText) rootView.findViewById(R.id.patient_editprof_address);
        patient_editprof_country = (EditText) rootView.findViewById(R.id.patient_editprof_country);
        patient_editprof_citytown = (EditText) rootView.findViewById(R.id.patient_editprof_citytown);
        patient_editprof_postcode = (EditText) rootView.findViewById(R.id.patient_editprof_postcode);
        patient_editprof_contact = (EditText) rootView.findViewById(R.id.patient_editprof_contact);
        patient_editprof_contact.setKeyListener(null);
        patient_editprof_email = (EditText) rootView.findViewById(R.id.patient_editprof_email);
        //patient_editprof_email.setKeyListener(null);


        patient_editprof_button = (Button) rootView.findViewById(R.id.patient_editprof_button);
        patient_editprof_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 updateProfileDataToServer();
            }
        });

        //**********permissions for marshmallow******
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findAndAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPatientFromServer();
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_edit_docprofile);
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Source");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == GALLERY)
            {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);

                        String path = saveImage(bitmap);
                        patient_editprof_image.setImageBitmap(bitmap);

                        //Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                        Log.d("@ Pic ", "path=>"+path);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else if (requestCode == CAMERA)
            {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] byteArray = bytes.toByteArray();

                //imageview.setImageBitmap(thumbnail);
                //saveImage(thumbnail);

                Bitmap scaledBitmap=decodeSampledBitmapFromResource(byteArray,200,200);
                patient_editprof_image.setImageBitmap(scaledBitmap);
                saveImage(scaledBitmap);

                //Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    //public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,  int reqWidth, int reqHeight) {
    public static Bitmap decodeSampledBitmapFromResource(final byte[] data, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeByteArray(data, 0, data.length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeByteArray(data, 0, data.length,options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        //byte[] byteArray = bytes.toByteArray();
        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            Log.d("@ Doc prof ", "Image File Saved::--->" + f.getAbsolutePath());
            patient_image_filename_tv.setText("" + f.getAbsolutePath());
            Filepath=f.getAbsolutePath();

            return f.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    private ArrayList<String> findAndAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        Log.d("@ Prescription Fragment", "onActivityResult");
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {
                    } else {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }//switch
    }//onRequestPermissionsResult

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    private void getPatientFromServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");

        }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/patient/get_patient_details/apitoken/1edc0ae98198866510bce219d5115b72/patientID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_details/apitoken/"+token+"/patientID/"+patient_id;
        Log.d("@ doc server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(context, 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ server"," response "+result.toString());

                if (result.equals("")) {

                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error","Patient data not found");
                        }
                    });

                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                    return;
                }

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {

                        JSONObject patient = resultJson.getJSONObject("result");

                        String title_prefix,patient_firstname,patient_lastname,address,contact_number,email;
                        String country_name,city_name,postcode;
                        String profile_image_path;

                        //title_prefix=patient.getString("prefix_title");
                        patient_firstname=patient.getString("first_name");
                        //patient_lastname=patient.getString("last_name");
                        address=patient.getString("address");
                        city_name=patient.getString("city_name");
                        country_name=patient.getString("country_name");
                        contact_number=patient.getString("contact_number");
                        postcode=patient.getString("postcode");
                        email=patient.getString("email");
                        profile_image_path=replaceString(patient.getString("profile_image_path"));

                        Log.d("@ Patient info:"," name :"+ patient_firstname+" image url:  "+profile_image_path);

                        //patient_editprof_title.setText(title_prefix);
                        patient_eitdprof_firstname.setText(patient_firstname);
                        //patient_editprof_lastname.setText(patient_lastname);
                        patient_editprof_address.setText(address);
                        patient_editprof_citytown.setText(city_name);
                        patient_editprof_country.setText(country_name);
                        patient_editprof_contact.setText(contact_number);
                        patient_editprof_postcode.setText(postcode);
                        patient_editprof_email.setText(email);


                        try {
                            Picasso.with(context)
                                    .load(profile_image_path)
                                    .placeholder(R.drawable.profilepic1)
                                    .error(R.drawable.default_avatar)
                                    .into(patient_editprof_image);
                        }catch(Exception e){ e.printStackTrace();}

                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Error:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Patient details fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                finally {
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



    private void updateProfileDataToServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        String spatient_editprof_title,spatient_eitdprof_firstname,spatient_editprof_lastname,spatient_editprof_address,spatient_editprof_country,spatient_editprof_citytown;
        String spatient_editprof_postcode,spatient_editprof_contact,spatient_editprof_email;

        //spatient_editprof_title = patient_editprof_title.getText().toString().trim();
        spatient_eitdprof_firstname = patient_eitdprof_firstname.getText().toString().trim();
        //spatient_editprof_lastname = patient_editprof_lastname.getText().toString().trim();
        spatient_editprof_address = patient_editprof_address.getText().toString().trim();
        spatient_editprof_country = patient_editprof_country.getText().toString().trim();
        spatient_editprof_citytown = patient_editprof_citytown.getText().toString().trim();
        spatient_editprof_postcode = patient_editprof_postcode.getText().toString().trim();
        spatient_editprof_contact = patient_editprof_contact.getText().toString().trim();
        spatient_editprof_email = patient_editprof_email.getText().toString().trim();

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/update_patient_profile_data";
        Log.d("@ patient prof url : ",url);

        String errormsg="";
        Integer errorcount=0;

       /* if(spatient_editprof_title.equals("")) {
            errormsg+=" Title Cannot be blank.\n";
            errorcount++;
        }*/
        if(spatient_eitdprof_firstname.equals("")) {
            errormsg+=" First Name Cannot be blank.\n";
            errorcount++;
        }
       /* if(spatient_editprof_lastname.equals("")) {
            errormsg+=" Last Name Cannot be blank.\n";
            errorcount++;
        }*/
        if(spatient_editprof_address.equals("")) {
            errormsg+=" Address Cannot be blank.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            Toast.makeText(context, ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("patient_id", patient_id);
        //postDataParams.put("title_prefix", spatient_editprof_title);
        postDataParams.put("first_name", spatient_eitdprof_firstname);
        //postDataParams.put("last_name", spatient_editprof_lastname);
        postDataParams.put("address", spatient_editprof_address);
        postDataParams.put("country_name", spatient_editprof_country);
        postDataParams.put("city_name", spatient_editprof_citytown);
        postDataParams.put("postcode", spatient_editprof_postcode);
        postDataParams.put("contact_number", spatient_editprof_contact);
        postDataParams.put("email", spatient_editprof_email);

        ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> files = new HashMap<String, String>();
        if(!Filepath.equals("")){
            files.put("Fileupload", Filepath);//"key,value
        }
        fileList.add(files);

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","update patient prof: "+ url);
        Log.d("@ uploaddoc params ", "update patient prof: "+ postJson.toString());

        UploadMultipartWithImageAsynctask uploadDocProfileAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, fileList, url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  S ", "server response "+ result);
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
                    String is_validate = obj.getString("is_validate");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") && is_validate.equals("1"))
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
                                Log.d("Updating", "Update Success");
                                //---------------------------------------------------------------------
                            }
                        });//runonui

                        Filepath="";
                        ((MainActivity)context).onBackPressed();
                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Updation Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Updation Failed: ");
                            }
                        });

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally{

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
                uploadDocProfileAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadDocProfileAsynctask.execute();//serial
            }
        }


    }

}
