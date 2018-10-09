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
 * A simple {@link Fragment} subclass.
 */
public class TermsConditionFragment extends Fragment {

    TextView tc_title,tc;

    Context context;

    public TermsConditionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_terms_condition, container, false);
        context=getActivity();

        tc_title = (TextView) rootView.findViewById(R.id.tc_title);
        tc_title.setText("");
        tc= (TextView) rootView.findViewById(R.id.tc);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_terms);
        getContentFromServer();
    }


    private void getContentFromServer()
    {

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://medixfora.com.md-in-64.webhostbox.net/restapi/static_pages/details/?apitoken=1edc0ae98198866510bce219d5115b72&id=6
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "static_pages/details/?apitoken="+token+"&id="+10;
        Log.d("@ cart server url : ",url);

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
                            tc_title.setText(Html.fromHtml(page_title,Html.FROM_HTML_MODE_COMPACT));
                            tc.setText(Html.fromHtml(page_content, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tc_title.setText(Html.fromHtml(page_title));
                            tc.setText(Html.fromHtml(page_content));
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
