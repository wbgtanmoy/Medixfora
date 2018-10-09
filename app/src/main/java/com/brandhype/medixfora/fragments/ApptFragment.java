package com.brandhype.medixfora.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
//import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.ColorUtils;
import com.brandhype.medixfora.Utils.OnSwipeTouchListener;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.calenderUtils.CommonMethod;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.FragmentLifecycle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApptFragment extends Fragment implements FragmentLifecycle {


    Context context;

    //EditText edt;
    private static final String TAG = ApptFragment.class.getSimpleName();

    public String[] NextPreWeekday;
    public String firstDayOfWeek;
    public String lastDayOfWeek;

    TextView dateOnSunTV;
    TextView dateOnMonTV;
    TextView dateOnTueTV;
    TextView dateOnWedTV;
    TextView dateOnThuTV;
    TextView dateOnFriTV;
    TextView dateOnSatTV;

    TextView monthOnSunTV;
    TextView monthOnMonTV;
    TextView monthOnTueTV;
    TextView monthOnWedTV;
    TextView monthOnThuTV;
    TextView monthOnFriTV;
    TextView monthOnSatTV;

    TextView tv_timing,tv_slotid;
    TextView tv_date,tv_date_actdate,tv_starttime;

    public int weekDaysCount = 0;

    Button gotoMainActBtn;
    Button nextWeekBtn;
    Button prevWeekBtn;
    Button doctor_book_nxt1;

    LinearLayout llweekdates;
    Animation lefttoright;
    Animation righttoleft;

    LinearLayout dateOnSunLL;
    LinearLayout dateOnMonLL;
    LinearLayout dateOnTueLL;
    LinearLayout dateOnWedLL;
    LinearLayout dateOnThuLL;
    LinearLayout dateOnFriLL;
    LinearLayout dateOnSatLL;

    View v;

    ListView todaysAbs_LV;

    View eventSun;
    View eventMon;
    View eventTue;
    View eventWed;
    View eventThu;
    View eventFri;
    View eventSat;

    ImageView nxt,prev;



    public ApptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_appt, container, false);

        context=getActivity();
        v = inflater.inflate(R.layout.fragment_appt, container, false);

        //edt = (EditText) v.findViewById(R.id.edt1);

        tv_timing = (TextView) v.findViewById(R.id.tv_timing);
        tv_slotid = (TextView) v.findViewById(R.id.tv_slotid);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_date_actdate = (TextView) v.findViewById(R.id.tv_date_actdate);
        tv_starttime = (TextView) v.findViewById(R.id.tv_starttime);

        tv_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = (Activity) context;
                if(!tv_timing.getText().equals("Slot Unavailable")) {

                    ViewPager viewPager = (ViewPager) act.findViewById(R.id.pager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
                else
                {
                    Toast.makeText(context, "Please select correct timing", Toast.LENGTH_SHORT).show();
                }

            }
        });


        doctor_book_nxt1 = (Button) v.findViewById(R.id.doctor_book_nxt1);
        doctor_book_nxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = (Activity) context;
                if(!tv_timing.getText().equals("Slot Unavailable")) {

                    ViewPager viewPager = (ViewPager) act.findViewById(R.id.pager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
                else
                {
                    Toast.makeText(context, "Please select correct slot", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nxt = (ImageView) v.findViewById(R.id.nxt);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeLeftClick();
            }
        });
        prev = (ImageView) v.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeRightClick();
            }
        });

        context=getActivity();
        //Action bar title text view
        //actionBarTitleTV = (TextView) v.findViewById(R.id.actionBartxt);

        /*
        * TextView of dates of week
        */
        dateOnSunTV = (TextView) v.findViewById(R.id.tv_date_on_sun);
        dateOnMonTV = (TextView) v.findViewById(R.id.tv_date_on_mon);
        dateOnTueTV = (TextView) v.findViewById(R.id.tv_date_on_tue);
        dateOnWedTV = (TextView) v.findViewById(R.id.tv_date_on_wed);
        dateOnThuTV = (TextView) v.findViewById(R.id.tv_date_on_thu);
        dateOnFriTV = (TextView) v.findViewById(R.id.tv_date_on_fri);
        dateOnSatTV = (TextView) v.findViewById(R.id.tv_date_on_sat);


        monthOnSunTV = (TextView) v.findViewById(R.id.tv_month_on_sun);
        monthOnMonTV = (TextView) v.findViewById(R.id.tv_month_on_mon);
        monthOnTueTV = (TextView) v.findViewById(R.id.tv_month_on_tue);
        monthOnWedTV = (TextView) v.findViewById(R.id.tv_month_on_wed);
        monthOnThuTV = (TextView) v.findViewById(R.id.tv_month_on_thu);
        monthOnFriTV = (TextView) v.findViewById(R.id.tv_month_on_fri);
        monthOnSatTV = (TextView) v.findViewById(R.id.tv_month_on_sat);
        /*
        * View of event
        */

        eventSun = (View) v.findViewById(R.id.event_circle_sun);
        eventMon = (View) v.findViewById(R.id.event_circle_mon);
        eventTue = (View) v.findViewById(R.id.event_circle_tue);
        eventWed = (View) v.findViewById(R.id.event_circle_wed);
        eventThu = (View) v.findViewById(R.id.event_circle_thu);
        eventFri = (View) v.findViewById(R.id.event_circle_fri);
        eventSat = (View) v.findViewById(R.id.event_circle_sat);

        /*
        * LinearLayout wrapping the dates of week
        */

        dateOnSunLL = (LinearLayout) v.findViewById(R.id.ll_date_on_sun);
        dateOnMonLL = (LinearLayout) v.findViewById(R.id.ll_date_on_mon);
        dateOnTueLL = (LinearLayout) v.findViewById(R.id.ll_date_on_tue);
        dateOnWedLL = (LinearLayout) v.findViewById(R.id.ll_date_on_wed);
        dateOnThuLL = (LinearLayout) v.findViewById(R.id.ll_date_on_thu);
        dateOnFriLL = (LinearLayout) v.findViewById(R.id.ll_date_on_fri);
        dateOnSatLL = (LinearLayout) v.findViewById(R.id.ll_date_on_sat);

        //OnClickEvents

        dateOnSunTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnSunLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnSunTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnSunTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[0]);


            }
        });

        dateOnMonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnMonLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnMonTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnMonTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[1]);

            }
        });

        dateOnTueTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnTueLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnTueTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnTueTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[2]);

            }
        });

        dateOnWedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnWedLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnWedTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnWedTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[3]);

            }
        });

        dateOnThuTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnThuLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnThuTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnThuTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[4]);

            }
        });

        dateOnFriTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnFriLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnFriTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnFriTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[5]);

            }
        });

        dateOnSatTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultOfDateText();
                dateOnSatLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnSatTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnSatTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[6]);

            }
        });


        //LinearLayout contains dates TextViews
        llweekdates = (LinearLayout) v.findViewById(R.id.llweekdates);

        //Code for swipe animation begins here
        lefttoright = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        righttoleft = AnimationUtils.loadAnimation(getActivity(), R.anim.in_from_right);

        llweekdates.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                //Toast.makeText(TestActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {


                NextPreWeekday = getWeekDayPrev();
                //getEventsInWeek();
                /*Log.i("Events in week : ", eventsInWeek[0] + " " + eventsInWeek[1] + " " +
                        eventsInWeek[2] + " " + eventsInWeek[3] + " " + eventsInWeek[4] + " " +
                        eventsInWeek[5] + " " + eventsInWeek[6]);*/
                firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
                lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);



                dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));

                dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));

                dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));

                dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));

                dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));

                dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));

                dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));

                llweekdates.startAnimation(lefttoright);

                //function call to clear the background
                setDefaultOfDateText();

                //function call to clear the list view items
                //setListTodaysAbs(defaultEmpName, defaultEmpDesig , new String[0]);
            }

            public void onSwipeLeft() {


                //Toast.makeText(TestActivity.this, "left", Toast.LENGTH_SHORT).show();
                NextPreWeekday = getWeekDayNext();

                //getEventsInWeek();

                firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
                lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);

                dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));

                dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));

                dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));

                dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));

                dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));

                dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));

                dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));

                llweekdates.startAnimation(righttoleft);

                //function call to clear the background
                setDefaultOfDateText();

                //function call to clear the list view items
                //setListTodaysAbs(defaultEmpName, defaultEmpDesig,new String[0]);
            }

            public void onSwipeBottom() {
                //Toast.makeText(TestActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });




        /*dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));
        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));
        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));
        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));
        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));
        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));
        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));*/

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        setCurrentDate();

    }


    public void setCurrentDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        Log.d("Today's day", "onResume: "+dayOfTheWeek);



        //Code for current week begins here

        NextPreWeekday = getWeekDay();
        //getEventsInWeek();

        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);


        dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));
        monthOnSunTV.setText(CommonMethod.convertMonth(NextPreWeekday[0]));

        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));
        monthOnMonTV.setText(CommonMethod.convertMonth(NextPreWeekday[1]));

        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));
        monthOnTueTV.setText(CommonMethod.convertMonth(NextPreWeekday[2]));

        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));
        monthOnWedTV.setText(CommonMethod.convertMonth(NextPreWeekday[3]));

        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));
        monthOnThuTV.setText(CommonMethod.convertMonth(NextPreWeekday[4]));

        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));
        monthOnFriTV.setText(CommonMethod.convertMonth(NextPreWeekday[5]));

        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));
        monthOnSatTV.setText(CommonMethod.convertMonth(NextPreWeekday[6]));

        /*dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));

        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));

        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));

        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));

        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));

        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));

        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));*/


        //....fixed cal ....

        switch (dayOfTheWeek) {


            case "Sun": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);


                setDefaultOfDateText();

                dateOnSunLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnSunTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnSunTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[0]);

                break;
            }

            case "Mon": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);


                setDefaultOfDateText();
                dateOnMonLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnMonTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnMonTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[1]);

                break;
            }

            case "Tue": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);

                setDefaultOfDateText();
                dateOnTueLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnTueTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnTueTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[2]);

                break;
            }

            case "Wed": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);

                setDefaultOfDateText();

                dateOnWedLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnWedTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnWedTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[3]);

                break;
            }

            case "Thu": {

                Log.d("Today's day", "onResume: "+dayOfTheWeek);

                setDefaultOfDateText();

                dateOnThuLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnThuTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnThuTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[4]);

                break;
            }

            case "Fri": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);

                setDefaultOfDateText();

                dateOnFriLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnFriTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnFriTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[5]);
                break;
            }

            case "Sat": {
                Log.d("Today's day", "onResume: "+dayOfTheWeek);

                setDefaultOfDateText();

                dateOnSatLL.setBackgroundResource(R.drawable.date_bg_circle);
                //dateOnSatTV.setBackgroundResource(R.drawable.date_bg_circle);

                dateOnSatTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.cyan));

                getDataFromDatabase(NextPreWeekday[6]);
                break;
            }


        }

    }

    public String[] getWeekDay() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayNext() {

        weekDaysCount++;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayPrev() {

        weekDaysCount--;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }


    //Change text background
    public void setDefaultOfDateText() {


        dateOnSunLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnMonLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnTueLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnWedLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnThuLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnFriLL.setBackgroundResource(R.drawable.date_bg_circle_white);
        dateOnSatLL.setBackgroundResource(R.drawable.date_bg_circle_white);

        dateOnSunTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnMonTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnTueTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnWedTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnThuTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnFriTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));
        dateOnSatTV.setBackgroundColor(ColorUtils.getColor(getActivity(), R.color.white));

        dateOnSunTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnMonTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnTueTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnWedTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnThuTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnFriTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));
        dateOnSatTV.setTextColor(ColorUtils.getColor(getActivity(), R.color.dates_default_text_color));

    }

    public void removeEventCircle() {
        eventSun.setVisibility(View.INVISIBLE);
        eventMon.setVisibility(View.INVISIBLE);
        eventTue.setVisibility(View.INVISIBLE);
        eventWed.setVisibility(View.INVISIBLE);
        eventThu.setVisibility(View.INVISIBLE);
        eventFri.setVisibility(View.INVISIBLE);
        eventSat.setVisibility(View.INVISIBLE);
    }


    public void onSwipeRightClick() {


        NextPreWeekday = getWeekDayPrev();
        //getEventsInWeek();
                /*Log.i("Events in week : ", eventsInWeek[0] + " " + eventsInWeek[1] + " " +
                        eventsInWeek[2] + " " + eventsInWeek[3] + " " + eventsInWeek[4] + " " +
                        eventsInWeek[5] + " " + eventsInWeek[6]);*/
        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);



        dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));
        monthOnSunTV.setText(CommonMethod.convertMonth(NextPreWeekday[0]));

        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));
        monthOnMonTV.setText(CommonMethod.convertMonth(NextPreWeekday[1]));

        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));
        monthOnTueTV.setText(CommonMethod.convertMonth(NextPreWeekday[2]));

        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));
        monthOnWedTV.setText(CommonMethod.convertMonth(NextPreWeekday[3]));

        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));
        monthOnThuTV.setText(CommonMethod.convertMonth(NextPreWeekday[4]));

        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));
        monthOnFriTV.setText(CommonMethod.convertMonth(NextPreWeekday[5]));

        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));
        monthOnSatTV.setText(CommonMethod.convertMonth(NextPreWeekday[6]));

        llweekdates.startAnimation(lefttoright);

        //function call to clear the background
        setDefaultOfDateText();


        //function call to clear the list view items
        //setListTodaysAbs(defaultEmpName, defaultEmpDesig , new String[0]);
    }

    public void onSwipeLeftClick() {


        //Toast.makeText(TestActivity.this, "left", Toast.LENGTH_SHORT).show();
        NextPreWeekday = getWeekDayNext();

        //getEventsInWeek();

        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);

        dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));
        monthOnSunTV.setText(CommonMethod.convertMonth(NextPreWeekday[0]));

        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));
        monthOnMonTV.setText(CommonMethod.convertMonth(NextPreWeekday[1]));

        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));
        monthOnTueTV.setText(CommonMethod.convertMonth(NextPreWeekday[2]));

        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));
        monthOnWedTV.setText(CommonMethod.convertMonth(NextPreWeekday[3]));

        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));
        monthOnThuTV.setText(CommonMethod.convertMonth(NextPreWeekday[4]));

        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));
        monthOnFriTV.setText(CommonMethod.convertMonth(NextPreWeekday[5]));

        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));
        monthOnSatTV.setText(CommonMethod.convertMonth(NextPreWeekday[6]));

        /*dateOnSunTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[0]));

        dateOnMonTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[1]));

        dateOnTueTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[2]));

        dateOnWedTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[3]));

        dateOnThuTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[4]));

        dateOnFriTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[5]));

        dateOnSatTV.setText(CommonMethod.convertWeekDays(NextPreWeekday[6]));*/

        llweekdates.startAnimation(righttoleft);

        //function call to clear the background
        setDefaultOfDateText();

        //function call to clear the list view items
        //setListTodaysAbs(defaultEmpName, defaultEmpDesig,new String[0]);
    }



    private void getDataFromDatabase(String selectedDate) {

        String date;
        int dayOfWeek=0;


        try {

            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat iPformat = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date strDate = sdf.parse(selectedDate);

            Date cDate=new Date();
            String today=new SimpleDateFormat("yyyy-MM-dd").format(cDate);

            if(!today.equals(selectedDate)) {
                if (new Date().after(strDate)) {
                    Toast.makeText(context, "Please select a date on or after current date.", Toast.LENGTH_SHORT).show();
                    //Log.d("@ date",strDate+"..."+today+ "... "+selectedDate);
                    tv_date.setText("");
                    tv_date_actdate.setText("");
                    tv_timing.setText("Slot Unavailable");
                    return;
                }
            }


            Date ipDate = iPformat.parse(selectedDate);
            date = format.format(ipDate);
            tv_date.setText(date);
            tv_date_actdate.setText(selectedDate);
            //Toast.makeText(context, "Date:" + selectedDate, Toast.LENGTH_SHORT).show();

            Calendar c = Calendar.getInstance();
            c.setTime(ipDate);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            //Toast.makeText(context, "Date:" + date+ " Day>" + dayOfWeek, Toast.LENGTH_SHORT).show();

            //--server call--
            getDaySlotFromServer(dayOfWeek,selectedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause ");
        super.onPause();
        //Toast.makeText(context,"edt value"+edt.getText(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPauseFragment(Context context) {
        Log.i(TAG, "onPauseFragment()");

        try {
            Activity v = (Activity) context;

             //EditText editText = (EditText) v.findViewById(R.id.edt1);

            TextView tv_timing2 = (TextView) v.findViewById(R.id.tv_timing);//display timing
            TextView tv_date2 = (TextView) v.findViewById(R.id.tv_date);//display date
            TextView tv_slotid2 = (TextView) v.findViewById(R.id.tv_slotid);//hidden slot
            TextView tv_date_actdate2 = (TextView) v.findViewById(R.id.tv_date_actdate);//actual hidden dt
            TextView tv_starttime2 = (TextView) v.findViewById(R.id.tv_starttime);//hidden start time

            String start_time = "" + tv_starttime2.getText();
            String datetime = tv_date2.getText() + " at " + start_time;


            AppPreferences pref = new AppPreferences(context);

            String doctor_id = pref.LoadPreferences(AppPreferences.Storage.DOCTOR_ID.name());
            String book_date = "" + tv_date_actdate2.getText(); //actual date
            String slot_id = "" + tv_slotid2.getText(); // slot id

            //Toast.makeText(context, "onPauseFragment:" + TAG +"-"+doctor_id+":"+book_date+":"+slot_id, Toast.LENGTH_SHORT).show();
            Log.d("@ Appt : ", "onPauseFragment:" + TAG + "-" + doctor_id + ":" + datetime + ":" + slot_id);


            //--storing the booking date,slot etc  in shared--
            //AppPreferences pref = new AppPreferences(context);
            pref.SavePreferences(AppPreferences.Storage.BOOKING_DATE.name(), book_date);

            pref.SavePreferences(AppPreferences.Storage.SLOT_ID.name(), slot_id);

            pref.SavePreferences(AppPreferences.Storage.SLOT_DATETIME.name(), datetime);

        }catch(Exception e){ e.printStackTrace();}

    }

    @Override
    public void onResumeFragment(Context context) {
        Log.d("@ Appt : ", "onResumeFragment");
        //Toast.makeText(context, "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
        try {
            Activity v = (Activity) context;
            TextView tv_timing2 = (TextView) v.findViewById(R.id.tv_timing);//display timing
            TextView tv_date2 = (TextView) v.findViewById(R.id.tv_date);//display date
            TextView tv_slotid2 = (TextView) v.findViewById(R.id.tv_slotid);//hidden
            TextView tv_date_actdate2 = (TextView) v.findViewById(R.id.tv_date_actdate);//actual hidden dt
            TextView tv_starttime2 = (TextView) v.findViewById(R.id.tv_starttime);//hidden start time

            /*tv_timing2.setText("");
            tv_date2.setText("");
            tv_slotid2.setText("0");
            tv_date_actdate2.setText("");
            tv_starttime2.setText("");*/

        }catch(Exception e){ e.printStackTrace();}
    }



    private void getDaySlotFromServer(int dayOfWeek, String selectedDate)
    {

        AppPreferences pref = new AppPreferences(context);
        String doctor_id = pref.LoadPreferences(AppPreferences.Storage.DOCTOR_ID.name());

        String docID=doctor_id;
        String dayID=""+(dayOfWeek-1);
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_schedule/dID/2179/dayId/1/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_schedule/dID/"+docID+"/dayId/"+dayID+"/apitoken/"+ token+"/sDay/"+selectedDate;

        Log.d("@ doctor slot url:",url);



        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ server response ",result.toString());

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


                /*
                {"id":"15",
                "doctor_id":"2179",
                "week_day":"2",
                "checkup_start_time":"7:30 PM",
                "checkup_end_time":"10:00 PM",
                "appoint_number_of_patient":"5"}
                 */

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    //final String err = commonutils.replaceString(resultJson.getString("api_action_message"));

                    String slot_id,appoint_number_of_patient,checkup_start_time,checkup_end_time,dtxt;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject json=data_arr.getJSONObject(i);

                            slot_id = json.getString("id");
                            appoint_number_of_patient = json.getString("appoint_number_of_patient");
                            checkup_start_time = json.getString("checkup_start_time");
                            checkup_end_time = json.getString("checkup_end_time");

                            dtxt="Timing: "+checkup_start_time+"-"+checkup_end_time;
                            Log.d("@number of patient:",appoint_number_of_patient);

                            tv_timing.setText(dtxt);
                            tv_slotid.setText(slot_id);
                            tv_starttime.setText(checkup_start_time);

                        }



                    }
                    else
                    {
                        tv_timing.setText("Slot Unavailable");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Sorry,Slot Unavailable", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context, "Slot fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Slot fetching failed");
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
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            } else {
                submitAsync.execute(url);
            }
        }

    }
}
