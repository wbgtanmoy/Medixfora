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

import com.brandhype.medixfora.MainActivityDoctor;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.customviews.MultiSelectionSpinner;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.SpecialityItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
public class DoctorEditprofileFragment extends Fragment {


    Context context;
    MultiSelectionSpinner sp,hosp;
    String specialities[]={"",""};
    String hospitals[]={"",""};
    String Filepath="";


    EditText doc_editprof_name,doc_editprof_degre,doc_editprof_designation,doc_editprof_address,doc_editprof_country;
    EditText doc_editprof_citytown,doc_editprof_postcode,doc_editprof_contact,doc_editprof_about,doc_editprof_website,doc_editprof_email;

    Button doc_editprof_button;
    ImageView doctor_editprof_image,doc_profile_pic_edit;
    TextView doctor_image_filename_tv;

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


    public DoctorEditprofileFragment() {
        // Required empty public constructor
        speciality_list = new ArrayList<>();
        speciality_stringlist = new ArrayList<String>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment_doctor_editprofile, container, false);;
        context=getActivity();

        doctor_image_filename_tv = (TextView) rootView.findViewById(R.id.doctor_image_filename_tv);

        doctor_editprof_image = (ImageView) rootView.findViewById(R.id.doctor_editprof_image);
        doc_profile_pic_edit = (ImageView) rootView.findViewById(R.id.doc_profile_pic_edit);
        //doc_profile_pic_edit.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        doc_profile_pic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });

        doc_editprof_name = (EditText) rootView.findViewById(R.id.doc_editprof_name);
        doc_editprof_degre = (EditText) rootView.findViewById(R.id.doc_editprof_degre);
        doc_editprof_designation = (EditText) rootView.findViewById(R.id.doc_editprof_designation);
        doc_editprof_address = (EditText) rootView.findViewById(R.id.doc_editprof_address);
        doc_editprof_country = (EditText) rootView.findViewById(R.id.doc_editprof_country);
        doc_editprof_citytown = (EditText) rootView.findViewById(R.id.doc_editprof_citytown);
        doc_editprof_postcode = (EditText) rootView.findViewById(R.id.doc_editprof_postcode);
        doc_editprof_contact = (EditText) rootView.findViewById(R.id.doc_editprof_contact);
        doc_editprof_contact.setKeyListener(null);
        doc_editprof_about = (EditText) rootView.findViewById(R.id.doc_editprof_about);
        doc_editprof_website = (EditText) rootView.findViewById(R.id.doc_editprof_website);
        doc_editprof_email = (EditText) rootView.findViewById(R.id.doc_editprof_email);
        //doc_editprof_email.setKeyListener(null);

        //speciality_stringlist = Arrays.asList(getActivity().getResources().getStringArray(R.array.speciality));
        //hospital_stringlist = Arrays.asList(getActivity().getResources().getStringArray(R.array.hospitals));

        specialities=getActivity().getResources().getStringArray(R.array.speciality);
        sp= (MultiSelectionSpinner) rootView.findViewById(R.id.doc_editprof_speciality);
        sp.setItems(speciality_stringlist);

        hospitals=getActivity().getResources().getStringArray(R.array.hospitals);
        hosp= (MultiSelectionSpinner) rootView.findViewById(R.id.doc_editprof_hospital);
        hosp.setItems(hospital_stringlist);


        doc_editprof_button = (Button) rootView.findViewById(R.id.doc_editprof_button);
        doc_editprof_button.setOnClickListener(new View.OnClickListener() {
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
        speciality_list.clear();
        hospital_list.clear();
        speciality_stringlist.clear();
        hospital_stringlist.clear();

        //getSpecialityFromServer();
        //getHospitalsFromServer();
        getDoctorFromServer();
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
                        doctor_editprof_image.setImageBitmap(bitmap);

                        //---------to check -----------
                        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte[] byteArray = bytes.toByteArray();
                        Bitmap scaledBitmap=decodeSampledBitmapFromResource(byteArray,200,200);
                        imageview.setImageBitmap(scaledBitmap);
                        String path=saveImage(scaledBitmap);*/

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
                doctor_editprof_image.setImageBitmap(scaledBitmap);
                saveImage(scaledBitmap);

                //Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
            //imageview.setVisibility(View.VISIBLE);
            //btn_upload.setVisibility(View.VISIBLE);
            //image_filename_ll.setVisibility(View.VISIBLE);

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
            doctor_image_filename_tv.setText("" + f.getAbsolutePath());
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

    private void getDoctorFromServer()
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_details/apitoken/1edc0ae98198866510bce219d5115b72/doctorID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_details/apitoken/"+token+"/doctorID/"+doctor_id;
        Log.d("@ doc server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(context, 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ server"," response "+result.toString());

                if (result.equals("")) {

                    ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error","Doctor data not found");
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
                    //String is_validate = resultJson.getString("is_validate");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {

                        JSONObject doctor = resultJson.getJSONObject("result");


                        String doctor_id,doctor_name,doctor_email,designation,doctor_fee,degree,address,contact_number;
                        String country_name,city_name,postcode,website_url;
                        String about_doctor,doctor_profile_image_path,sp_name,h_name,sp_id,h_id;

                        doctor_id=doctor.getString("id");
                        doctor_name=doctor.getString("doctor_name");
                        doctor_email=doctor.getString("email");
                        designation=doctor.getString("designation");
                        degree=doctor.getString("degree");
                        doctor_fee=doctor.getString("doctor_fee");

                        address=doctor.getString("address");
                        contact_number=doctor.getString("contact_number");
                        country_name=doctor.getString("country_name");
                        city_name=doctor.getString("city_name");
                        postcode=doctor.getString("postcode");
                        website_url=replaceString(doctor.getString("website_url"));
                        about_doctor=doctor.getString("about_doctor");

                        doctor_profile_image_path=replaceString(doctor.getString("doctor_profile_image_path"));
                        sp_id=doctor.getString("sp_id");
                        sp_name=doctor.getString("sp_name");
                        h_id=doctor.getString("h_id");
                        h_name=doctor.getString("h_name");

                        Log.d("@ Doctor info:"," name :"+ doctor_name+" image url:  "+doctor_profile_image_path);

                        doc_editprof_name.setText(doctor_name);
                        doc_editprof_degre.setText(degree);
                        doc_editprof_designation.setText(designation);
                        doc_editprof_address.setText(address);
                        doc_editprof_country.setText(country_name);
                        doc_editprof_citytown.setText(city_name);
                        doc_editprof_postcode.setText(postcode);
                        doc_editprof_contact.setText(contact_number);
                        doc_editprof_about.setText(about_doctor);
                        doc_editprof_website.setText(website_url);
                        doc_editprof_email.setText(doctor_email);

                        try {
                            Picasso.with(context)
                                    .load(doctor_profile_image_path)
                                    .placeholder(R.drawable.profilepic1)
                                    .error(R.drawable.default_avatar)
                                    .into(doctor_editprof_image);
                        }catch(Exception e){ e.printStackTrace();}

                    }
                    else
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor fetching failed"+err);
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



    private void getSpecialityFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/speciality/get_speciality_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "speciality/get_speciality_list/apitoken/"+ token;

        Log.d("@ server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ server response ",result.toString());

                if (result.equals("")) {

                    getActivity().runOnUiThread(new Runnable() {
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


                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String speciality_name,id,is_active,speciality_image,image_file_name;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    Log.d("@ doctor  length ",""+data_arr.length());
                    if(data_arr.length()<= 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    }

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            speciality_name = data_arr.getJSONObject(i).getString("speciality_name");
                            speciality_image = data_arr.getJSONObject(i).getString("image_path");
                            image_file_name = data_arr.getJSONObject(i).getString("image_file_name");
                            is_active = data_arr.getJSONObject(i).getString("is_active");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                SpecialityItem navItem = new SpecialityItem();
                                navItem.setTitle(speciality_name);
                                navItem.setId(id);
                                navItem.setSpeciality_image(speciality_image);
                                navItem.setImage_name(image_file_name);
                                Log.d("@ speciality name ",speciality_name);

                                speciality_list.add(navItem);
                                speciality_stringlist.add(speciality_name);
                            }

                        }

                        sp.setItems(speciality_stringlist);
                        //**********************
                        //getHospitalsFromServer();

                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Specility list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Specility list fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    pdspinnerGeneral.dismiss();
                }

            }

        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            submitAsync.execute(url);
        }

    }


    private void getHospitalsFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/hospital/get_hospital_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "hospital/get_hospital_list/apitoken/"+ token;

        Log.d("@ server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ server response ",result.toString());

                if (result.equals("")) {

                    getActivity().runOnUiThread(new Runnable() {
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


                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String hospital_name,id,is_active,logo_image_path,logo_image;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        Log.d("@ doctor  length ",""+data_arr.length());
                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            hospital_name = data_arr.getJSONObject(i).getString("hospital_name");
                            logo_image = data_arr.getJSONObject(i).getString("logo_image");
                            logo_image_path = data_arr.getJSONObject(i).getString("logo_image_path");
                            is_active = data_arr.getJSONObject(i).getString("is_active");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                SpecialityItem navItem = new SpecialityItem();
                                navItem.setTitle(hospital_name);
                                navItem.setId(id);
                                navItem.setSpeciality_image(logo_image_path);
                                navItem.setImage_name(logo_image);
                                Log.d("@ speciality name ",hospital_name);

                                hospital_list.add(navItem);
                                hospital_stringlist.add(hospital_name);
                            }

                        }

                        hosp.setItems(hospital_stringlist);
                        //******************
                        //getDoctorFromServer();

                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Specility list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Specility list fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    pdspinnerGeneral.dismiss();
                }

            }

        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            submitAsync.execute(url);
        }

    }


    private void updateProfileDataToServer()
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}


        //ssignu_username=signu_username.getText().toString();
        String sdoc_editprof_name,sdoc_editprof_degre,sdoc_editprof_designation,sdoc_editprof_address,sdoc_editprof_country,sdoc_editprof_citytown;
        String sdoc_editprof_postcode,sdoc_editprof_contact,sdoc_editprof_about,sdoc_editprof_website,sdoc_editprof_email;

        sdoc_editprof_name = doc_editprof_name.getText().toString().trim();
        sdoc_editprof_degre = doc_editprof_degre.getText().toString().trim();
        sdoc_editprof_designation = doc_editprof_designation.getText().toString().trim();
        sdoc_editprof_address = doc_editprof_address.getText().toString().trim();
        sdoc_editprof_country = doc_editprof_country.getText().toString().trim();
        sdoc_editprof_citytown = doc_editprof_citytown.getText().toString().trim();
        sdoc_editprof_postcode = doc_editprof_postcode.getText().toString().trim();
        sdoc_editprof_contact = doc_editprof_contact.getText().toString().trim();
        sdoc_editprof_about = doc_editprof_about.getText().toString().trim();
        sdoc_editprof_website = doc_editprof_website.getText().toString().trim();
        sdoc_editprof_email = doc_editprof_email.getText().toString().trim();


        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_details/apitoken/1edc0ae98198866510bce219d5115b72/doctorID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/update_doctor_details";
        Log.d("@ doc prof url : ",url);


        String errormsg="";
        Integer errorcount=0;

        if(sdoc_editprof_name.equals("")) {
            errormsg+=" Name Cannot be blank.\n";
            errorcount++;
        }
        if(sdoc_editprof_degre.equals("")) {
            errormsg+=" Degree Cannot be blank.\n";
            errorcount++;
        }
        if(sdoc_editprof_designation.equals("")) {
            errormsg+=" Designation Cannot be blank.\n";
            errorcount++;
        }
        if(sdoc_editprof_address.equals("")) {
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


        postDataParams.put("doctor_id", doctor_id);
        postDataParams.put("doctor_name", sdoc_editprof_name);
        postDataParams.put("email", sdoc_editprof_email);
        postDataParams.put("address", sdoc_editprof_address);
        postDataParams.put("country_name", sdoc_editprof_country);
        postDataParams.put("city_name", sdoc_editprof_citytown);
        postDataParams.put("postcode", sdoc_editprof_postcode);
        //postDataParams.put("contact_number", sdoc_editprof_contact);
        postDataParams.put("about_doctor", sdoc_editprof_about);
        postDataParams.put("degree", sdoc_editprof_degre);
        postDataParams.put("designation", sdoc_editprof_designation);
        postDataParams.put("website_url", sdoc_editprof_website);

        ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> files = new HashMap<String, String>();
        if(!Filepath.equals("")){
            files.put("Fileupload", Filepath);//"key,value
        }
        fileList.add(files);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","update doc prof: "+ url);
        Log.d("@ uploaddoc params ", "update doc prof: "+ postJson.toString());


        UploadMultipartWithImageAsynctask uploadDocProfileAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, fileList, url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  S ", "server response "+ result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
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
                    //String is_validate = obj.getString("is_validate");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
                                Log.d("Updating", "Update Success");
                                //---------------------------------------------------------------------
                            }
                        });//runonui

                        Filepath="";
                        ((MainActivityDoctor)context).onBackPressed();
                    }
                    else
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
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
                uploadDocProfileAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadDocProfileAsynctask.execute();//serial
            }
        }


    }

}
