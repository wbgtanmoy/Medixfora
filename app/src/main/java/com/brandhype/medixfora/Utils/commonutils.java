package com.brandhype.medixfora.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.brandhype.medixfora.R;

/**
 * Created by Tanmoy Banerjee on 03-08-2017.
 */

public class commonutils {

    public static boolean isNullOrBlank(String str) {
        boolean flag = false;
        if (str == null || (str != null && str.equalsIgnoreCase("null"))
                || (str != null && str.isEmpty())
                || (str != null && str.trim().length() == 0))
            flag = true;

        return flag;
    }

    public static String replaceString(String rtstr) {

        //rtstr = rtstr.replace("[", "");
        //rtstr = rtstr.replace("{", "");
        //rtstr = rtstr.replace("}", "");
        rtstr = rtstr.replace("\"", "");
        rtstr = rtstr.replace("\\/", "/");
        //rtstr = rtstr.replace("]", "");
        //rtstr = rtstr.replace(" ", "%20");

        return rtstr;

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void hideKeyboard2(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = ((Activity) context).getCurrentFocus();
            if (imm != null && currentFocus != null) {
                IBinder windowToken = currentFocus.getWindowToken();
                if (windowToken != null) {
                    imm.hideSoftInputFromWindow(windowToken, 0);
                }
            }
        } catch (Exception e) {
            Log.e("@", "Can't even hide keyboard " + e.getMessage());
        }
    }

    /*public static String toTitleCase(String givenString) {
        if(givenString.length()>0) {
            String toTitlecase=givenString.trim();
            String[] arr = toTitlecase.split(" ");
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < arr.length; i++) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }

            return sb.toString().trim();
        }
        return "";
    }*/

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


    public void showMessageDialog(Context context, String title)
    {
        //------------------------Dialog---------------------------------------------
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog_ok);

        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);
        txt_main.setText(""+title);

        Button _yes = (Button) dialog.findViewById(R.id.btn_yes);
        _yes.setEnabled(true);
        _yes.setText("OK");

        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog != null && !dialog.isShowing())
            dialog.show();
        //----------------------------------------------------------------------
    }

    public void showOKDialog(Context context, String title)
    {
        //------------------------Dialog---------------------------------------------
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);

        txt_main.setText(""+title);

        Button _cancel = (Button) dialog.findViewById(R.id.btn_no);
        Button _yes = (Button) dialog.findViewById(R.id.btn_yes);
        _yes.setEnabled(true);

        _cancel.setVisibility(View.GONE);
        _yes.setText("OK");

        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //--Do nothing
            }
        });

        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog != null && !dialog.isShowing())
            dialog.show();
        //----------------------------------------------------------------------
    }


    public static void setFragment(Activity activity ,Fragment fragment, Bundle bundle, int title, Boolean adtobackstack)
    {

        if (fragment != null) {
            if(bundle !=null)
            {
                fragment.setArguments(bundle);
            }
            FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body_doctor, fragment);
            if(adtobackstack)
            { fragmentTransaction.addToBackStack(null);}
            fragmentTransaction.commit();
            ((AppCompatActivity)activity).getSupportActionBar().setTitle(title);

        }
    }


    public static void setFragmentPatient(Activity activity ,Fragment fragment, Bundle bundle, int title, Boolean adtobackstack)
    {

        if (fragment != null) {
            if(bundle !=null)
            {
                fragment.setArguments(bundle);
            }
            FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            if(adtobackstack)
            { fragmentTransaction.addToBackStack(null);}
            fragmentTransaction.commit();
            ((AppCompatActivity)activity).getSupportActionBar().setTitle(title);

        }
    }


    public static boolean isNullOrWhitespace(String s) {
        if (s == null)
            return true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

}
