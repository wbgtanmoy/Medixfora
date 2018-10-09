package com.brandhype.medixfora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeatureActivity extends Activity {


    Button feature_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        feature_button = (Button) findViewById(R.id.feature_button);
        feature_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getApplicationContext(), BannerActivity.class);
                startActivity(login_intent);
                finish();
            }
        });
    }
}
