package com.brandhype.medixfora.Utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by tanmoy on 13/7/16.
 */
public class ColorUtils {
    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
