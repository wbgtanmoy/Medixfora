package com.brandhype.medixfora.fragments;


import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.UploadFileAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.interfaces.FragmentLifecycle;
import com.brandhype.medixfora.models.AppointmentItem;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig;
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile;
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedFragment extends Fragment implements FragmentLifecycle  {


    Context context;
    View v;

    AppPreferences pref2;
    AppointmentItem appointmentItem2;

    TextView finished_pname_name_txt,finished_Doctorname_txt,finished_timing_txt,finished_attachdoc_txt,finished_condition_txt;
    Button doctor_book_final,doctor_attachdoc_btn;
    ImageView doctor_attach_image;

    private static final String TAG = FinishedFragment.class.getSimpleName();

    String bookingSlotID="0";

    private static final int SDCARD_PERMISSION_FILE_DEF=321;
    private static final int ACTIVITY_CHOOSE_FILE = 3;


    private static final String IMAGE_DIRECTORY = "/medixfora";
    private int GALLERY = 1, CAMERA = 2;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private final static int ALL_PERMISSIONS_RESULT = 107;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();


    DialogConfig dialogConfig;

    String FilePath="";

    public FinishedFragment() {
        // Required empty public constructor
        appointmentItem2=new AppointmentItem();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context=getActivity();

        v = inflater.inflate(R.layout.fragment_finished, container, false);

        dialogConfig = new DialogConfig.Builder()
                //.enableMultipleSelect(true) // default is false
                //.enableFolderSelect(true) // default is false
                //.initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android") // default is sdcard
                .initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() ) // default is sdcard
                .supportFiles(new SupportFile(".png", R.drawable.image_icon), new SupportFile(".jpg", R.drawable.image_icon), new SupportFile(".pdf", R.drawable.pdf_icon)) // default is showing all file types.
                .build();

        finished_pname_name_txt = (TextView) v.findViewById(R.id.finished_pname_name_txt);
        finished_Doctorname_txt = (TextView) v.findViewById(R.id.finished_Doctorname_txt);
        finished_timing_txt = (TextView) v.findViewById(R.id.finished_timing_txt);
        finished_condition_txt = (TextView) v.findViewById(R.id.finished_condition_txt);

        finished_attachdoc_txt = (TextView) v.findViewById(R.id.finished_attachdoc_txt);
        doctor_attach_image = (ImageView) v.findViewById(R.id.doctor_attch_image);
        doctor_attach_image.setVisibility(View.GONE);


        doctor_attachdoc_btn = (Button) v.findViewById(R.id.doctor_attachdoc_btn);
        doctor_attachdoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePath="";
                //performFilePick();//check permission is inbuilt
                //checkPermission();//FilePickerDialogFragment have inbuild check permission feature

                showPictureDialog();
            }
        });

        doctor_book_final = (Button) v.findViewById(R.id.doctor_book_final);
        doctor_book_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //addDoctorApptToCart();
                addDoctorApptToCartNew();
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

        return v;
    }


    public void checkPermission()
    {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SDCARD_PERMISSION_FILE_DEF);
        } else {
            performFilePick();
        }


    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SDCARD_PERMISSION_FILE_DEF) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Finishing : File Permission granted", Toast.LENGTH_SHORT).show();

                *//*
                Failure delivering result ResultInfo{who=@android:requestPermissions:, request=65857, result=-1, data=Intent { act=android.content.pm.action.REQUEST_PERMISSIONS (has extras) }} to activity {com.codopoliz.medixfora/com.codopoliz.medixfora.MainActivity}:
                java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                 *//*
                //performFilePick();
            }
        }
    }*/

    private void performFilePick()
    {
        pickFileDialog();
    }

    private void performFilePick_old()
    {
        // ----problem with getting real file path from external SD , but ok with internal storage ----
        Intent chooseFile;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        //String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        String[] mimetypes = {"image/*", "application/pdf|text/*","video/*"}; //audio/*"
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        // Only get openable files
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);

        Intent intent;
        intent = Intent.createChooser(chooseFile, "Choose a file");

        try {
            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
            //getActivity().setResult(RESULT_OK, intent);
            //getActivity().startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
         } catch (android.content.ActivityNotFoundException ex) {
             Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
             //pickFolderOrFile(false); //single file selection , to check permission in marshmallow
             pickFileDialog();  //mutifile selection , with auto permission for marshmallow
        }
    }


    void pickFileDialog()
    {
        new FilePickerDialogFragment.Builder()
                .configs(dialogConfig)
                .onFilesSelected(new FilePickerDialogFragment.OnFilesSelectedListener() {
                    @Override
                    public void onFileSelected(List<File> list) {
                        Log.e("", "total Selected file: " + list.size());
                        String path="";
                        for (File file : list) {
                            Log.e("@ ", "Selected file: " + file.getAbsolutePath());
                            path= file.getAbsolutePath();
                        }

                        finished_attachdoc_txt.setText(path);
                        FilePath=path;
                    }
                })
                .build()
                .show(getActivity().getSupportFragmentManager(), null);
    }



    /*public void setFileTextView(String fname){
        finished_attachdoc_txt.setText(fname);
    }*/


    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
    }

    @Override
    public void onPauseFragment(Context context) {
        Log.i(TAG, "onPauseFragment()");
        //Toast.makeText(context, "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment(Context context) {
        Log.i(TAG, "onResumeFragment()");



        try {

            pref2 = new AppPreferences(context);
            String docName=pref2.LoadPreferences(AppPreferences.Storage.DOCTOR_NAME.name());
            String book_datetime2=pref2.LoadPreferences(AppPreferences.Storage.SLOT_DATETIME.name());

            String jsonStr=pref2.LoadPreferences(AppPreferences.Storage.APPOINTMENTDATA.name());
            Gson gson = new Gson();
            appointmentItem2 = gson.fromJson(jsonStr,AppointmentItem.class);

            //Toast.makeText(context, "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
            Log.d("@$ appointment -Pname",appointmentItem2.getPatient_name()+":Phn:"+appointmentItem2.getPatient_contact()+":App date:"+appointmentItem2.getAppt_date()+":Slot id:"+appointmentItem2.getSlotID()+":docid:"+appointmentItem2.getDoctor_id());
            Log.d("@ finish item ",appointmentItem2.toString());

            bookingSlotID=appointmentItem2.getSlotID();
            Activity v=(Activity)context;

            TextView finished_pname_name_txt2 = (TextView) v.findViewById(R.id.finished_pname_name_txt);
            TextView finished_Doctorname_txt2 = (TextView) v.findViewById(R.id.finished_Doctorname_txt);
            TextView finished_timing_txt2 = (TextView) v.findViewById(R.id.finished_timing_txt);
            TextView finished_condition_txt2 = (TextView) v.findViewById(R.id.finished_condition_txt);

            ImageView doctor_attach_image2 = (ImageView) v.findViewById(R.id.doctor_attch_image);
            doctor_attach_image2.setVisibility(View.GONE);

            finished_pname_name_txt2.setText(""+ appointmentItem2.getPatient_name());
            finished_Doctorname_txt2.setText(""+ docName);
            finished_timing_txt2.setText(""+ book_datetime2);
            finished_condition_txt2.setText(""+appointmentItem2.getPatient_health());

        }catch(Exception e){ e.printStackTrace();}
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {



       // ----problem with getting real file path from external SD , but ok with internal storage ----

       Log.d("@ FinishedFragment", "onActivityResult$"+RESULT_OK+"$"+resultCode+"$"+ACTIVITY_CHOOSE_FILE+"$"+requestCode);
        String path = "";
        if(resultCode == RESULT_OK && requestCode == ACTIVITY_CHOOSE_FILE)
        {
            Uri uri = intent.getData();
            Log.d("@ Build version","SDK INT "+Build.VERSION.SDK_INT+ "uri: "+uri);

            FilePath = ""+ RealPathUtils.getRealPath(context,uri);
            Toast.makeText(context, "File:" + FilePath, Toast.LENGTH_SHORT).show();

            //SDK 21 returning filepath as null -- to check
            try {

                finished_attachdoc_txt.setText(FilePath);
            }catch(Exception e){e.printStackTrace();}

            Log.d("@ FinishedFragment", FilePath);

    }*/


    private void addDoctorApptToCartNew()
    {
        System.out.println("@ uploading file :"+ finished_attachdoc_txt.getText().toString() );
        String file=FilePath;//""+finished_attachdoc_txt.getText().toString();

        UploadFileAsynctask uplodImageAndData=new UploadFileAsynctask(context,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                 //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                 Log.d("@ result_for upload", result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( getActivity().getBaseContext(), "Blank Response From server", Toast.LENGTH_SHORT).show();
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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getBaseContext(), "Appointment Added to Cart", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Appointment Added to Cart");

                                finished_attachdoc_txt.setText("");

                                Fragment fragment = new CartFragment();
                                if (fragment != null) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    //fragmentTransaction.addToBackStack(null); //**
                                    fragmentTransaction.commit();
                                   // set the toolbar title
                                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.cart);
                                }
                            }
                        });

                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getBaseContext(), "Add To Cart Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Registration Failed:"+err);
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
                uplodImageAndData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, file);//parallel
            } else {
                uplodImageAndData.execute(file);//serial
            }
        }
    }//perfomupload


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
                        doctor_attach_image.setImageBitmap(bitmap);
                        doctor_attach_image.setVisibility(View.VISIBLE);

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
                //patient_editprof_image.setImageBitmap(scaledBitmap);
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
            //patient_image_filename_tv.setText("" + f.getAbsolutePath());
            finished_attachdoc_txt.setText(f.getAbsolutePath());
            FilePath=f.getAbsolutePath();

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
                        //showPictureDialog();
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




}
