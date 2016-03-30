
package com.tripasfactory.thetripaslibrary.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Class responsible for converting graphic units like px, dp and sp into others
 *
 * @author ricardo
 */
public class UnitConverterClass {

    private static String TAG = UnitConverterClass.class.getSimpleName();

    /**
     * Converts dp into px
     *
     * @param dp
     * @param context
     * @return px
     */
    public static int convertDpToPx(float dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);

        return px;
    }

    /**
     * Converts px in to dp
     *
     * @param px
     * @param context
     * @return
     */
    public static float convertPxToDp(int px, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        float dp = (float) ((px - 0.5f) / scale);

        return dp;
    }

    public static float pixelsToSp(Context context, Float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static float SpToPixels(Context context, Float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    public static Drawable base64toDrawable(Resources resources,
                                            String base64image) {
        Drawable drawableToReturn = null;
        if (base64image != null) {
            drawableToReturn = new BitmapDrawable(resources, base64toBitmap(base64image));
        }
        return drawableToReturn;

    }

    public static Bitmap base64toBitmap(String base64image) {
        if (base64image != null) {
            try {
                byte[] imageData = Base64.decode(base64image, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(imageData, 0,
                        imageData.length);
            } catch (Exception e) {
                L.e(TAG, "base64 corrupted");
                return null;
            }
        }
        return null;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }

    public static String drawableToBase64(Drawable d) {
        return bitmapToBase64(((BitmapDrawable) d).getBitmap());
    }

}
