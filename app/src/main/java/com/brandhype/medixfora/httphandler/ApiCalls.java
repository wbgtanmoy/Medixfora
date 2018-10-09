package com.brandhype.medixfora.httphandler;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ApiCalls {

	public ApiCalls() {
	}

	/**
	 * Purpose to convert the inputstream to string
	 * @param is
	 * @return
	 */
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = null ;
		try {
			if (Locale.getDefault().getLanguage().equals("ar"))
				reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"), 8);


			else 
				reader = new BufferedReader(new InputStreamReader(is));
		} catch (Exception e) {
			// TODO: handle exception
		}


		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}



	/**
	 * Getting JSON from URL making HTTP request
	 * @param url string
	 * @throws Exception
	 * */
	public String getJsonFromUrl(String url) throws Exception {
		HttpURLConnection connection = null;

		try {
			URL url1 = new URL(url);
			connection = (HttpURLConnection) url1.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(30000);
			connection.connect();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return getServerResponse(connection);
	}

	/**
	 * Purpose: Read and return server response
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public String getServerResponse(HttpURLConnection connection)throws Exception {
		BufferedReader reader = null;

		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}

		reader.close();
		connection.disconnect();
		return stringBuilder.toString().trim();
	}


	/**
	 * Send the date to server and get the response
	 * @param requestURL
	 * @param postDataParams
	 * @return
	 */
	public String getDataFromAPI(String requestURL, HashMap<String, String> postDataParams) {

		URL url;
		String response = "";
		try {
			url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));

			if (postDataParams != null)
				writer.write(getPostDataString(postDataParams));

			writer.flush();
			writer.close();
			os.close();
			int responseCode=conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}
			}
			else
				response="";

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Making the filed url.
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry : params.entrySet()){
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	/**
	 * Send data to server with image file.
	 * @param upLoadServerUri
	 * @param sourceFileUri
	 * @param postDataParams
	 * @return
	 */
	public String uploadDataWithImageFile(String upLoadServerUri,
                                          String sourceFileUri, HashMap<String, String> postDataParams) {

		int serverResponseCode = 0;
		String response = "";
		String fileName = sourceFileUri;
		String serverResponseMessage = "";
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return "";
		}
		try { // open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			//conn.setRequestProperty("Content-Type", "image/png");
			conn.setRequestProperty("prof_pic", fileName);

			dos = new DataOutputStream(conn.getOutputStream());

			for(Map.Entry<String, String> entry : postDataParams.entrySet()){

				// add parameters
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""+entry.getKey()+"\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// assign value
				dos.writeBytes(URLEncoder.encode(entry.getValue(), "UTF-8"));
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + lineEnd);

			}

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"prof_pic\";filename=\""+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			serverResponseMessage = conn.getResponseMessage();

			if (serverResponseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}
			}
			else
				response="";

			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
			if(serverResponseCode == 200){
				//File Upload Complete.
			}

			//close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;
	}

	/**
	 * Post data with multiple images.
	 * @param upLoadServerUri
	 * @param postDataParams
	 * @param multipleImageSourceFileUri
	 * @return
	 */
	public String uploadDataWithMultipleImageFile(String upLoadServerUri,
                                                  HashMap<String, String> postDataParams,
                                                  HashMap<String, String> multipleImageSourceFileUri) {

		int serverResponseCode = 0;
		String response = "";
		//String fileName = sourceFileUri;
		String serverResponseMessage = "";
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		//File sourceFile = new File(sourceFileUri);
		/*if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return "";
		}*/
		try { // open a URL connection to the Servlet
			FileInputStream fileInputStream = null;
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			//conn.setRequestProperty("Content-Type", "image");
			//conn.setRequestProperty("prof_pic", fileName);=========================
			//conn.setRequestProperty(fileName, entry.getValue());

			dos = new DataOutputStream(conn.getOutputStream());

			for(Map.Entry<String, String> entry : postDataParams.entrySet()){
				Log.i("uploadData", "key. :-"+entry.getKey());
				Log.e("uploadData", "Value(). :-"+entry.getValue());
				// add parameters
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""+entry.getKey()+"\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// assign value
				dos.writeBytes(entry.getValue());
				//dos.writeBytes(URLEncoder.encode(entry.getValue(), "UTF-8"));
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
			}

			for(Map.Entry<String, String> entry : multipleImageSourceFileUri.entrySet()){
				Log.i("uploadFile", "fileName. :-"+entry.getKey());
				Log.e("uploadFile", "entry.getValue(). :-"+entry.getValue());

				//String fileName = entry.getKey();
				File sourceFile = new File(entry.getValue());
				fileInputStream = new FileInputStream(sourceFile);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				//name="prof_pic";


				dos.writeBytes("Content-Disposition: form-data; name=\""+entry.getKey()+"\"; filename=\""+ entry.getValue() + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			}
			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			serverResponseMessage = conn.getResponseMessage();

			if (serverResponseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}
			}
			else {
				response = "";
			}

			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
			if(serverResponseCode == 200){
				//File Upload Complete.
				Log.i("uploadFile", "File Upload Complete. ");
			}

			try {
				//close the streams //
				fileInputStream.close();
			}catch (Exception e){
				e.printStackTrace();
			}

			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
		}
		return response;
	}

}
