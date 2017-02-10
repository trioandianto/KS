package com.kliksembuh.ks.models;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Ucu Nurul Ulum on 10/02/2017.
 */

public class Utils {
    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}
