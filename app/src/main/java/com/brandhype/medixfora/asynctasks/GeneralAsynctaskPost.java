package com.brandhype.medixfora.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brandhype.medixfora.httphandler.ApiCalls;

import java.util.HashMap;

//import com.codopoliz.doorman.httphandler.ApiCalls;

public class GeneralAsynctaskPost extends AsyncTask<String, Integer, String> {

	String TAG = "GeneralAsynctaskPost";
    private Context context;
	protected ProgressDialog pdspinnerGeneral;
	//public JSONObject jObj;
	protected int noProgressDialog = 1;
	private HashMap<String, String> map;
	//private boolean withImage = false;
	private HashMap<String, String> multipleImageMap;
	private String imagePath="";


	/**
	 * Default Progress as active
	 * @param context
	 * @param map
	 * @param noProgressDialog
	 */
	public GeneralAsynctaskPost(Context context, HashMap<String, String> map, int noProgressDialog) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.noProgressDialog = noProgressDialog;
		this.map  = map;
	}

	/**
	 * Post data and image.
	 * @param context
	 * @param noProgressDialog
	 * @param map
	 * @param imagePath
	 */
	public GeneralAsynctaskPost(Context context,
								HashMap<String, String> map,
								String imagePath,
								int noProgressDialog) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.noProgressDialog = noProgressDialog;
		this.map  = map;
		this.imagePath = imagePath;
	}

	/**
	 * Post data and more the two image.
	 * @param context
	 * @param noProgressDialog
	 * @param map
	 * @param multipleImageMap
	 */
	public GeneralAsynctaskPost(Context context,
								HashMap<String, String> map,
								HashMap<String, String> multipleImageMap,
								int noProgressDialog) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.noProgressDialog = noProgressDialog;
		this.map  = map;
		this.multipleImageMap = multipleImageMap;
	}
		
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(noProgressDialog == 1) {
			pdspinnerGeneral = new ProgressDialog(context);
			pdspinnerGeneral.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdspinnerGeneral.setMessage("Please wait...");
            pdspinnerGeneral.setIndeterminate(true);
			pdspinnerGeneral.setCancelable(false);
			pdspinnerGeneral.show();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ApiCalls json = new ApiCalls();
		String result = "";
		if (imagePath.equals("") && multipleImageMap == null){
			Log.e("uploadFile", "Without image.");
			result = json.getDataFromAPI(params[0], map);
		}else if (imagePath.equals("") && multipleImageMap != null && multipleImageMap.size() > 0){
			Log.e("uploadFile", "multiple imgae .");
			result = json.uploadDataWithMultipleImageFile(params[0], map, multipleImageMap);
		}else{
			Log.e("uploadFile", "singele  image.");
			result = json.uploadDataWithImageFile(params[0], imagePath, map);
		}

		Log.i(TAG, "result:" + result);
		int i =  0;
		if(this.isCancelled())
    	{
    		return null;
    	}
						
		return result;
	}
	
	

}
