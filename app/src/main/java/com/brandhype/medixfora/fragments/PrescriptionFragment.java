package com.brandhype.medixfora.fragments;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.BuildConfig;
import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.adpaters.CustomPrescriptionListAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.UploadCartItemAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.PrescriptionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrescriptionFragment extends Fragment {


    Context context;
    View rootView;

    private TextView prescription_filename_tv;
    public EditText prescription_title;
    //private Button btn_capture,btn_gallaery,btn_selectoldfile;
    private Button btn_upload;
    private ImageView imageview;
    private LinearLayout image_filename_ll;
    private LinearLayout capture_img_ll,gallary_img_ll,presc_img_ll;

    private static final String IMAGE_DIRECTORY = "/medixfora";
    private int GALLERY = 1, CAMERA = 2;
    private Uri imageUri;
    String mCurrentPhotoPath;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    AppPreferences pref;
    JSONObject patient;

    List<PrescriptionItem> dataS = new ArrayList<>();
    AlertDialog alertDialog ;

    private String requestType="";
    int position_preslist=0;
    PrescriptionItem oldPrescriptionitem;

    TextView presc_search_view;
    CustomPrescriptionListAdapter adapter;

    public PrescriptionItem selected_prescriptionitem=null;

    ProgressDialog dialog;

    public PrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_prescription, container, false);
        context=getActivity();
        rootView = inflater.inflate(R.layout.fragment_prescription_new, container, false);


       /* btn_capture = (Button) rootView.findViewById(R.id.btn_capture);
        btn_gallaery = (Button) rootView.findViewById(R.id.btn_gallaery);
        btn_selectoldfile = (Button) rootView.findViewById(R.id.btn_selectoldfile);*/

        capture_img_ll = (LinearLayout) rootView.findViewById(R.id.capture_img_ll);
        gallary_img_ll = (LinearLayout) rootView.findViewById(R.id.gallary_img_ll);
        presc_img_ll = (LinearLayout) rootView.findViewById(R.id.presc_img_ll);


        btn_upload = (Button) rootView.findViewById(R.id.btn_upload);
        prescription_filename_tv=(TextView) rootView.findViewById(R.id.prescription_filename_tv);
        prescription_title=(EditText) rootView.findViewById(R.id.prescription_title);
        imageview = (ImageView) rootView.findViewById(R.id.iv);

        image_filename_ll = (LinearLayout) rootView.findViewById(R.id.image_filename_ll);

        capture_img_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPictureDialog();
                requestType="new";
                prescription_title.setText("");
                takePhotoFromCamera();
            }
        });

        gallary_img_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPictureDialog();
                requestType="new";
                prescription_title.setText("");
                choosePhotoFromGallary();
            }
        });

        presc_img_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestType="old";
                prescription_title.setText("");
                selected_prescriptionitem=null;
                chooseOldPrescription();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ptitle=""+prescription_title.getText();
                Log.d("@ prescription ","Title:"+ptitle);

                if(ptitle.trim().equals(""))
                {
                    Toast.makeText(context, "Please provide more info for the prescription", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(requestType.trim().equals(""))
                {
                    Toast.makeText(context, "Please Select Image or Old prescription", Toast.LENGTH_SHORT).show();
                    return;
                }

                //------------------------Dialog---------------------------------------------
                final Dialog alertDialog = new Dialog(context);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(false);
                alertDialog.setContentView(R.layout.custom_alertdialog);

                TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);

                txt_main.setText("Sure to send  this prescription to admin ?.");

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
                        alertDialog.dismiss();
                        //**************perform cart add operation************
                        checkOldNew(requestType);
                        //****************************************************
                    }
                });

                if (alertDialog != null && !alertDialog.isShowing())
                    alertDialog.show();
                //----------------------------------------------------------------------

            }
        });

        imageview.setVisibility(View.VISIBLE);
        prescription_filename_tv.setVisibility(View.VISIBLE);

        image_filename_ll.setVisibility(View.GONE);


        //checkStoragePermission();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findAndAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("@ DEBUG", "onResume of prescription");
        super.onResume();
        dataS.clear();
        //----gets cartcount , method of activity
        ((MainActivity)context).getCartCountFromServer();

    }

    /*public void checkStoragePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            Log.d("@ Permission:","Write permission is already granted");
        }
    }*/


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select From gallery",
                "Capture Photo" };
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
        imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", getFile());// return content:///..
        //imageUri=Uri.fromFile(getFile()); // returns file:///...
        Log.d("@ file uri :", imageUri.toString());
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //API >24
        startActivityForResult(intent, CAMERA);
    }

    //this method will create and return the path to the image file
    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/medixfora");// the file path

        //if it doesn't exist the folder will be created
        if(!folder.exists())
        {folder.mkdir();}

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Medixfora_"+ timeStamp + "_";
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
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
                        imageview.setImageBitmap(bitmap);
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

                dialog = new ProgressDialog(context);
                try {

                    dialog.setMessage("Processing image. Please wait...");
                    dialog.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                Bitmap fullImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                fullImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                                byte[] byteArray = bytes.toByteArray();
                                Bitmap scaledBitmap=decodeSampledBitmapFromResource(byteArray,400,400);
                                saveImage(scaledBitmap);
                                imageview.setImageBitmap(scaledBitmap);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Failed saving!", Toast.LENGTH_SHORT).show();
                            }
                            finally{
                                dialog.dismiss();
                            }
                        }
                    }, 7000);  // 7 sec to allow processing of image captured


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed camera!", Toast.LENGTH_SHORT).show();
                }

                try {

                    //-----------Not OK, for prescription you need clear image ---------
                    /*Bitmap fullImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                    String path = saveImage(fullImage);
                    imageview.setImageBitmap(fullImage);*/

                    //---saving thumnail image---
                    /*Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imageview.setImageBitmap(thumbnail);
                    saveImage(thumbnail);*/

                    //---------image scaling & saving---------------
                    /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    byte[] byteArray = bytes.toByteArray();
                    Bitmap scaledBitmap=decodeSampledBitmapFromResource(byteArray,400,400);
                    imageview.setImageBitmap(scaledBitmap);
                    saveImage(scaledBitmap);*/


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            //imageview.setVisibility(View.VISIBLE);
            //btn_upload.setVisibility(View.VISIBLE);
            image_filename_ll.setVisibility(View.VISIBLE);
        }
    }



    public static Bitmap decodeSampledBitmapFromResource(final byte[] data, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length,options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length,options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
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
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
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

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            prescription_filename_tv.setText("" + f.getAbsolutePath());
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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { //lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //marshmallow
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



    public void chooseOldPrescription()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/patient/get_patient_uploaded_prescription/patientID/22/apitoken/1edc0ae98198866510bce219d5115b72

        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            patient_id=patient.getString("id");
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}


        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_uploaded_prescription/patientID/"+user_id+"/apitoken/"+token;

        Log.d("@ old pres url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ presc response ",result.toString());

                ///result=loadJSONFromAsset();//---test purpose, loading from asset*********
                // Log.d("@ asset json ",result.toString());

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

                    String id="0",title="",patient_id="",upload_filename="",prescription_file_path="";

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    //norecordfound_cart.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        String names[] = new String[data_arr.length()];

                        for (int i = 0; i < data_arr.length(); i++)
                        {
                            JSONObject doc=data_arr.getJSONObject(i);
                            id = doc.getString("id");
                            title = doc.getString("prescription_title");
                            patient_id = doc.getString("patient_id");
                            upload_filename = doc.getString("upload_filename");
                            prescription_file_path = replaceString(doc.getString("prescription_file_path"));

                            Log.d("@ pres name:",id+ " title: "+title+ "file:"+prescription_file_path);

                            PrescriptionItem navItem = new PrescriptionItem();
                            navItem.setId(id);
                            navItem.setTitle(title);
                            navItem.setPatient_id(patient_id);
                            navItem.setUpload_filename(upload_filename);
                            navItem.setPrescription_file_path(prescription_file_path);

                            dataS.add(navItem);
                            names[i]=title;
                        }



                        alertDialog = new AlertDialog.Builder(context).create();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.custom_prespricption_list, null);


                        alertDialog.setView(convertView);
                        alertDialog.setTitle("Saved Prescriptions");
                        ListView lv = (ListView) convertView.findViewById(R.id.oldPrescription);

                        presc_search_view = (EditText) convertView.findViewById(R.id.presc_search_view);


                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,names);
                        //lv.setAdapter(adapter);
                        adapter=new CustomPrescriptionListAdapter(context,PrescriptionFragment.this, names,dataS,alertDialog );
                        lv.setAdapter( adapter );

                        presc_search_view.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                                String text = presc_search_view.getText().toString().toLowerCase();
                                Log.i("@ filter", " filtering" );
                                try {
                                    adapter.filter(text);
                                }catch(Exception e){e.printStackTrace();}
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1,
                                                          int arg2, int arg3) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                                // TODO Auto-generated method stub

                            }
                        });

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(context, "choosed "+dataS.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                                /*position_preslist=position;
                                addOldPrescription(dataS.get(position_preslist));
                                alertDialog.dismiss();*/

                            }
                        });

                        alertDialog.show();
                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Prescription fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Prescription fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                pdspinnerGeneral.dismiss();
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



    /*private void addOldPrescription(PrescriptionItem prescriptionItem)
    {
        Log.d("@ old pres itm",prescriptionItem.toString());
        imageview.setVisibility(View.GONE);
        prescription_filename_tv.setVisibility(View.GONE);
        image_filename_ll.setVisibility(View.VISIBLE);
        PrescriptionItem pitem=prescriptionItem;
        oldPrescriptionitem=prescriptionItem;
        prescription_title.setText(pitem.getTitle());
    }*/


    private void checkOldNew(String requestType)
    {
        PrescriptionItem prescriptionItem;
        String filepath=prescription_filename_tv.getText().toString().trim();
        AppPreferences pref;
        JSONObject patient;
        String user_id="";

        try{
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            user_id=patient.getString("id");

        }catch(Exception e){e.printStackTrace();}

        if(requestType.equals("new")){
            prescriptionItem=new PrescriptionItem();
            prescriptionItem.setTitle(prescription_title.getText().toString());
            prescriptionItem.setPatient_id(user_id);
            addPrescriptionToCart(prescriptionItem, true, filepath);
        }
        else  if(requestType.equals("old")){
            //prescriptionItem=oldPrescriptionitem;
            if(selected_prescriptionitem !=null) {
                prescriptionItem = selected_prescriptionitem;
                addPrescriptionToCart(prescriptionItem, false, "");
            }
        }
        Log.d("@ old/new ","prescription: "+requestType);
    }



    private void addPrescriptionToCart(PrescriptionItem prescriptionItem, Boolean isNew, String filepath)
    {
        Log.d("@ old pres itm",prescriptionItem.toString()+"isNew:"+isNew+"File:"+filepath);

        //-------------add to cart-------------
        PrescriptionItem pitem=prescriptionItem;
        String filename="";
        String user_id="0";

        //http://brandhypedigital.in/demo/medixfora/restapi/medicine/save_prescription_request/
        //String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/add_to_cart/";

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "medicine/save_prescription_request/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        if (!isNew ){   //old saved prescription
            postDataParams.put("item_id", pitem.getId());
            filename="";
            postDataParams.put("priscription_title", prescription_title.getText().toString());
        }
        else {          //new prescription
            postDataParams.put("item_id", "999999");
            postDataParams.put("priscription_title", prescription_title.getText().toString());
            filename=filepath;
            Log.d("@ New ","pres filename: "+filename);
        }

        postDataParams.put("item_qty", "1");
        //postDataParams.put("itemtype", "MP"); // required only when adding to cart.
        postDataParams.put("user_id", pitem.getPatient_id());

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","prescription : "+ url);
        Log.d("@ prescription params ", "prescription: "+ postJson.toString());


        UploadCartItemAsynctask uploadcartitemAsynctask=new UploadCartItemAsynctask(context,postDataParams,filename,url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  prescription ", result);
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
                                //Toast.makeText(context, "Prescription Sent", Toast.LENGTH_SHORT).show();
                                Log.d("@ Success","Prescription Sent");

                                new commonutils().showMessageDialog(context,"Prescription sent to admin. Our expert will contact you shortly.");
                                ((MainActivity)context).onBackPressed();
                                /*Fragment fragment = new CartFragment();
                                if (fragment != null) {
                                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                    // set the toolbar title
                                    ((AppCompatActivity)context).getSupportActionBar().setTitle(R.string.cart);
                                }*/
                            }
                        });

                        prescription_filename_tv.setText("");
                        prescription_title.setText("");
                        requestType="";
                        oldPrescriptionitem=null;

                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Failed: "+err);
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

}
