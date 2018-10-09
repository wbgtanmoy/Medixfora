package com.brandhype.medixfora.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandhype.medixfora.R;

/**
 * Created by Tanmoy Banerjee on 22-08-2017.
 */


public class CustomToastAlertDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button yes, no;
    TextView msgtxt,msgtitle;
    RelativeLayout rl;
    public String msg,title;

    public static final int TOAST_DISPLAY_TIME = 4000; //4Sec

    public CustomToastAlertDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.title="title";
        this.msg="message text";
    }

    public CustomToastAlertDialog(Activity activity, String title, String msg) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.title=title;
        this.msg=msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_toast_alertdialog);

        msgtitle = (TextView) findViewById(R.id.msg_title);
        msgtitle.setText(title);

        msgtxt = (TextView) findViewById(R.id.msg_txt);
        msgtxt.setText(msg);

        rl = (RelativeLayout) findViewById(R.id.custom_toast_dialog_rl);
        rl.setOnClickListener(this);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
               // we return True if the listener has consumed the event
               return true;
            }
        });

       getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dismiss();
            }
        }, TOAST_DISPLAY_TIME);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_toast_dialog_rl:
                //--do something--
                break;
            default:
                break;
        }
        dismiss();
    }
}