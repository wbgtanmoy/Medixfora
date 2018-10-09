package com.brandhype.medixfora.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * Created by Tanmoy on 22-09-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "@ MyFirebaseMsgService";
    String msgbody="";
    boolean isHandledThrough_onMessageReceived=false;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        //******* will NOT be called when app in background with "notification" key on
        //****** will be called with data payload if "notification" key is not used

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        isHandledThrough_onMessageReceived=false;
        try {

            msgbody=remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Key Body: " +msgbody );//body
            isHandledThrough_onMessageReceived=true;

        }catch (Exception e){isHandledThrough_onMessageReceived=false;}

        //---data payload------------
        Log.d(TAG, "fcm data payload " + remoteMessage.getData().toString());//"data" payload
        Log.d(TAG, "fcm_title " + remoteMessage.getData().get("post_title")); //post_title key of data
        Log.d(TAG, "fcm_id " + remoteMessage.getData().get("post_id"));
        Log.d(TAG, "fcm_msg " + remoteMessage.getData().get("post_msg"));

        //Calling method to generate notification
        if(!isAppIsInBackground(this)) {
            Log.d(TAG, "onMessageReceived : App is in Foreground");
            msgbody=remoteMessage.getData().get("post_msg");
            sendNotification(msgbody);
        }
        else
        {
            Log.d(TAG, "onMessageReceived : App is in Background");
            msgbody=remoteMessage.getData().get("post_msg");
            sendNotification(msgbody);
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {

        /*
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);
        expandedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
        expandedView.setTextViewText(R.id.notification_message, messageBody);
        // adding action to left button
        Intent leftIntent = new Intent(this, NotificationIntentService.class);
        leftIntent.setAction("left");
        expandedView.setOnClickPendingIntent(R.id.left_button, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        // adding action to right button
        Intent rightIntent = new Intent(this, NotificationIntentService.class);
        rightIntent.setAction("right");
        expandedView.setOnClickPendingIntent(R.id.right_button, PendingIntent.getService(this, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));


        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        collapsedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
        collapsedView.setTextViewText(R.id.content_title, "MedixFora");
        collapsedView.setTextViewText(R.id.content_text, messageBody);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // these are the three things a NotificationCompat.Builder object requires at a minimum
                .setSmallIcon(R.drawable.navbar_image)
                .setContentTitle("Medixfora")
                .setContentText("This is a text")
                // notification will be dismissed when tapped
                .setAutoCancel(true)
                // tapping notification will open MainActivity
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                //.setContent(collapsedView)
                // setting the custom collapsed and expanded views
                .setCustomContentView(collapsedView)
                //.setCustomBigContentView(expandedView)
                // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
                .setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle());

        // retrieves android.app.NotificationManager
        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        */

        Intent intent = new Intent(this, MainActivity.class);
        Bundle b=new Bundle();
        b.putString("query_reply_text",messageBody);
        intent.putExtras(b);
        intent.setAction("showmessage");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("Medixfora: Admin Replied")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    /*available from com.google.firebase:firebase-messaging:10.2.1
    override method this method is called every time , no matter what app is in foreground or in background or killed
    but this method is available with this firebase api version */
    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        // calls always irrespective of app is in foreground or background you can get ur data here
        //intent.getExtras().get("your_data_key")
        if(isHandledThrough_onMessageReceived==false) {
            if (isAppIsInBackground(this)) {
                try {
                    Log.d(TAG, "handle intent : App is in Background");
                    Log.d(TAG, "handleIntent fcm_title " + intent.getExtras().get("post_title"));
                    Log.d(TAG, "handleIntent fcm_id " + intent.getExtras().get("post_id"));
                    Log.d(TAG, "handleIntent fcm_msg " + intent.getExtras().get("post_msg"));

                    //sendNotification(intent.getExtras().get("post_msg").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}