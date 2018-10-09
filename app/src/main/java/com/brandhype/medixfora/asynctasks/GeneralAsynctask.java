package com.brandhype.medixfora.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.httphandler.ApiCalls;


public class GeneralAsynctask extends AsyncTask<String, Integer, String> {
	String TAG = "GeneralAsynctask";
	private Context context;
	protected ProgressDialog pdspinnerGeneral;
	//public JSONObject jObj;
	protected int noProgressDialog = 1;
	private String method;


	public GeneralAsynctask(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public GeneralAsynctask(Context context, int noProgressDialog) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.noProgressDialog = noProgressDialog;
		//this.method = method;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		try {
			if(noProgressDialog == 1) {
				//pdspinnerGeneral=new CustomProgressDialog(context);
				pdspinnerGeneral=new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
				pdspinnerGeneral.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pdspinnerGeneral.setMessage("Please wait...");
				pdspinnerGeneral.setCancelable(false);
				pdspinnerGeneral.setIndeterminate(true);
				pdspinnerGeneral.show();
			}

		}catch (Exception e){

		}

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//Log.v("GeneralAsynctask url ------ >", params[0]);
		ApiCalls json = new ApiCalls();
		String result = "";
		try {
			result = json.getJsonFromUrl(params[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
