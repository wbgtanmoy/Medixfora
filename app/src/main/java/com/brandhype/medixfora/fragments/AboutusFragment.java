package com.brandhype.medixfora.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;

import org.json.JSONObject;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * Created by Ravi on 29/07/15.
 */
public class AboutusFragment extends Fragment {

    TextView about_us,about_us_title;

    Context context;

    String aboutus="MedixFora, a new high-end Solutions and Services Company for the Healthcare Industry, aimed at catering to Doctors, Paramedics, Hospitals, Patients and the Pharmaceutical Industry at large. MedixFora is skillfully crafted and incorporated to cater exclusively to the future needs of the Healthcare Industry and Medical Fraternity.\n" +
            "\n" +
            "The name MedixFora was born out of our wish to bring all healthcare solutions under one umbrella and f ulfill your needs on a single platform in the near future. Medix relates itself with Healthcare and Fora, the Latin plural of Forum, used to be a gathering place of great significance and varied activities. \n" +
            "\n" +
            "Medixfora has envisioned and charted a road map that will unravel itself as we move from one milestone to another and set new trends for the Healthcare Industry. Over the course of this journey, we intend to finally become a “One-Stop-Destination” for the Healthcare community.\n" +
            "\n" +
            "Our editorial board, which comprises of a team of eminent and dedicated super-specialist doctors in their respective domains or fields, will disseminate the latest technical and medical information from time to time.\n" +
            "\n" +
            "Our more that two decades of experience will help you relax in comfort and help you to utilize you time and energy towards achieving your own vision and mission, while we work for you.";

    public AboutusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
        context=getActivity();

        about_us = (TextView) rootView.findViewById(R.id.about_us);
        about_us.setText("");
        about_us_title = (TextView) rootView.findViewById(R.id.about_us_title);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.aboutus);
        getContentFromServer();
    }


    private void getContentFromServer()
    {

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://medixfora.com.md-in-64.webhostbox.net/restapi/static_pages/details/?apitoken=1edc0ae98198866510bce219d5115b72&id=6
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "static_pages/details/?apitoken="+token+"&id="+6;
        Log.d("@  server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(context, 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ NUMBER"," OF CART LIST response "+result.toString());

                if (result.equals("")) {

                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error","Cart not found");
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

                    JSONObject data_obj = resultJson.getJSONObject("result");
                    String page_title;
                    String page_content;

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1"))
                    {
                        page_title=data_obj.getString("page_title");
                        page_content=data_obj.getString("page_content");


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            about_us_title.setText(Html.fromHtml(page_title,Html.FROM_HTML_MODE_COMPACT));
                            about_us.setText(Html.fromHtml(page_content, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            about_us_title.setText(Html.fromHtml(page_title));
                            about_us.setText(Html.fromHtml(page_content));
                        }
                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Content fetching failed: "+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Content fetching failed "+err);
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



}
