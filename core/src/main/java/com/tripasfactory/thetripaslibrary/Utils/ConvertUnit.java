
package com.tripasfactory.thetripaslibrary.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUnit {

    public static final String DATE_FORMAT = "dd-MM-yyyy";

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display
     *                metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent
     * pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display
     *                metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /*
    * dateAsString must me in the format dd-MM-yyyyy
    * */
    public static Date convertStringToDate(String dateAsString) {


        Date result = new Date();
        if (!TextUtils.isEmpty(dateAsString)) {
            dateAsString = dateAsString.replace("/", "-");
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            try {
                result = format.parse(dateAsString);
            } catch (Exception e) {
                return null;
            }
            return result;
        }
        return null;

    }

    public static String getDelayDifferenceInDays(Date olderDate, Date youngerDate) {

        String result = "";
        long millisInADay = 24 * 60 * 60 * 1000;

        if (olderDate.before(youngerDate)) {

            try {
                result = String.valueOf((youngerDate.getTime() - olderDate.getTime()) / millisInADay);
            } catch (Exception e) {
                result = "";
            }
        }

        return result;

    }

    /**
     *  Formatted date with time conversion: String -> DateTime
     * */
    public static DateTime convertStringToDateTime(String dateTimeAsString) {
        DateTimeFormatter dtfr = DateTimeFormat.fullDateTime();
        DateTime dateTime = dtfr.parseDateTime(dateTimeAsString);
        return dateTime;
    }

    /**
     *  Formatted date with time conversion: DateTime -> String
     * */
    public static String convertDateTimeToString(DateTime dateTime) {
        DateTimeFormatter dtfr = DateTimeFormat.fullDateTime();
        String dateTimeStr = dateTime.toString(dtfr);;
        return dateTimeStr;
    }

}
