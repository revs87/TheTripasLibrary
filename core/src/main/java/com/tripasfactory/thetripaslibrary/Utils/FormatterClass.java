package com.tripasfactory.thetripaslibrary.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.tripasfactory.thetripaslibrary.Base.ApplicationClass;
import com.tripasfactory.thetripaslibrary.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatterClass {

    private static String TAG = "FormatterClass";

    public static String unAccent(String s) {
        if (s != null && !s.equals("")) {
            //
            // JDK1.5
            // use sun.text.Normalizer.normalize(s, Normalizer.DECOMP, 0);
            //
            String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern
                    .compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } else {
            return "Error not defined";
        }

    }

    /**
     * Formats number to groups of N separated by space
     *
     * @param unformattedValue
     * @return
     */
    public static String formatNumberToGroupBy3(String unformattedValue) {
        if (unformattedValue != null && !unformattedValue.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            int size = unformattedValue.length();
            if (size > 0) {
                stringBuilder.append(unformattedValue.charAt(0));
            }
            for (int i = 1; i < size; i++) {
                if (i % 3 == 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            return stringBuilder.toString();
        }
        return null;

        // } else {
        // return ApplicationClass.getAppContext().getResources()
        // .getString(R.string.error_not_defined);
        // }
    }

    /**
     * @param cardNumber
     * @param mask, p.e., "#### XXXX XXXX ####"
     * @return card number masked with the mask defined as parameter
     */
    public static String maskCard = "#### XXXX XXXX ####";

    public static String formatStringToGroupBy4WithMask(String cardNumber, String mask) {

        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(cardNumber.charAt(index));
                index++;
            } else if (c == 'X') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        // return the masked number
        return maskedNumber.toString();
    }

    /**
     * Formats number to groups of N separated by space
     *
     * @param unformattedValue
     * @return
     */
    public static String formatStringToGroupBy4(String unformattedValue) {
        return unformattedValue;
/* DISABLED FOR BIM*/
//        if (unformattedValue != null && !unformattedValue.equals("")) {
//
//            StringBuilder stringBuilder = new StringBuilder();
//            int size = unformattedValue.length();
//            if (size > 0) {
//                stringBuilder.append(unformattedValue.charAt(0));
//            }
//            for (int i = 1; i < size; i++) {
//                if (i % 4 == 0) {
//                    stringBuilder.append(" ");
//                }
//                stringBuilder.append(unformattedValue.charAt(i));
//            }
//            return stringBuilder.toString();
//        } else {
//            return "";
//        }
    }

    public static String formatStringToGroupBy4Default(String unformattedValue) {
        if (unformattedValue != null
                && !unformattedValue.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            int size = unformattedValue.length();
            if (size > 0) {
                stringBuilder.append(unformattedValue.charAt(0));
            }
            for (int i = 1; i < size; i++) {
                if (i % 4 == 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * Formats number to XXXX-XXXXXX-XXX separated by '-'
     *
     * @param unformattedValue
     * @return
     */
    public static String formatStringToBankAccount(String unformattedValue) {
        if (unformattedValue != null && !unformattedValue.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            int size = unformattedValue.length();
            if (size > 0) {
                stringBuilder.append(unformattedValue.charAt(0));
            }
            for (int i = 1; i < size; i++) {
                if (i % 4 == 0) {
                    stringBuilder.append("-");
                    break;
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            for (int i = 4; i < size; i++) {
                if (i % 10 == 0) {
                    stringBuilder.append("-");
                    break;
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            for (int i = 10; i < size; i++) {
                if (i % 13 == 0) {
                    break;
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * Formats number to XXXX-XXXXXX-XXX separated by '-'
     *
     * @param unformattedValue
     * @return
     */
    public static String formatStringWithCharactersLimit(String unformattedValue, int limit) {
        if (unformattedValue != null && !unformattedValue.equals("")) {

            if (unformattedValue.length() < limit + 1) {
                return unformattedValue;
            } else {
                return unformattedValue.substring(0, unformattedValue.length() - 1);
            }

            // StringBuilder stringBuilder = new StringBuilder();
            // int size = unformattedValue.length();
            // if (size > limit) {
            // stringBuilder.append(unformattedValue.charAt(0));
            // }
            // for (int i = 0; i < limit; i++) {
            // stringBuilder.append(unformattedValue.charAt(i));
            // }
            //
            // return stringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * Formats number to display as a Nib
     *
     * @param unformattedValue
     * @return
     */
    public static String formatNumberToNib(String unformattedValue) {

        StringBuilder stringBuilder = new StringBuilder();
        if (unformattedValue == null) {
            return "";
        }
        int size = unformattedValue.length();
        if (size > 21) {
            size = 21;
        }

        // Arrange By 4
        for (int i = 0; i < size; i++) {
            if (i == 4 || i == 8 || i == 12 || i == 16 || i == 20) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(unformattedValue.charAt(i));
        }

        // Arrange 4 + 4 + rest + 2
//        for (int i = 0; i < size; i++) {
//            if (i == 4 || i == 8) {
//                stringBuilder.append(" ");
//            }
//            stringBuilder.append(unformattedValue.charAt(i));
//        }
//        if (stringBuilder.length() > 12) {
//            stringBuilder.insert(stringBuilder.length() - 2, " ");
//        }

        return stringBuilder.toString();
    }

    public static String formatNumberToIban(String unformattedValue) {

        StringBuilder stringBuilder = new StringBuilder();
        if (unformattedValue == null) {
            return "";
        }
        int size = unformattedValue.length();
        if (size > 25) {
            size = 25;
        }

        // Arrange By 4
        for (int i = 0; i < size; i++) {
            if (i == 4 || i == 8 || i == 12 || i == 16 || i == 20 || i == 24) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(unformattedValue.charAt(i));
        }

        // Arrange 4 + 4 + rest + 2
//        for (int i = 0; i < size; i++) {
//            if (i == 4 || i == 8) {
//                stringBuilder.append(" ");
//            }
//            stringBuilder.append(unformattedValue.charAt(i));
//        }
//        if (stringBuilder.length() > 12) {
//            stringBuilder.insert(stringBuilder.length() - 2, " ");
//        }

        return stringBuilder.toString();
    }

    /**
     * Formats number to display in the phone locale
     *
     * @param unformattedValue
     * @return
     */
//    public static String formatNumberToDisplay(BigDecimal unformattedValue) {
//
//        if (unformattedValue == null)
//            return null;
//
//        unformattedValue = unformattedValue.setScale(2, RoundingMode.HALF_UP);
//
//        Context context = ApplicationClass.getAppContext();
//        Locale currentLocale = context.getResources().getConfiguration().locale;
//
//        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
//        formatter.setMaximumFractionDigits(2);
//        formatter.setMinimumFractionDigits(2);
//
//        return formatter.format(unformattedValue);
//
//    }
    public static String formatNumberToDisplay(String unformattedValue) {

        if (unformattedValue == null || unformattedValue.isEmpty()) {
            return null;
        }

        unformattedValue = unformattedValue.replaceAll(",", ".");

        BigDecimal unformattedValueBig = new BigDecimal(unformattedValue);

        unformattedValueBig = unformattedValueBig.setScale(2, RoundingMode.HALF_UP);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        return formatter.format(unformattedValueBig);

    }

    public static String formatNumberToDisplay4Decimals(String unformattedValue) {

        if (unformattedValue == null || unformattedValue.isEmpty())
            return null;

        unformattedValue = unformattedValue.replaceAll(",", ".");

        BigDecimal unformattedValueBig = new BigDecimal(unformattedValue);

        unformattedValueBig = unformattedValueBig.setScale(2,
                RoundingMode.HALF_UP);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
        formatter.setMaximumFractionDigits(4);
        formatter.setMinimumFractionDigits(4);

        return formatter.format(unformattedValueBig);

    }

    /**
     * Formats number to display
     *
     * @param unformattedValue
     * @return
     */
    public static String formatNumberToDisplay(Float unformattedValue) {

        if (unformattedValue == null)
            return null;

        BigDecimal unformattedValueBD = new BigDecimal(unformattedValue);

        unformattedValueBD = unformattedValueBD.setScale(2, RoundingMode.HALF_UP);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        return formatter.format(unformattedValueBD);
    }

    public static String formatBigDecimalToDisplay(BigDecimal unformattedValue) {

        if (unformattedValue == null)
            return null;

        unformattedValue = unformattedValue.setScale(2, RoundingMode.HALF_UP);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        return formatter.format(unformattedValue);
    }

    /**
     * Formats number to display in the phone locale, with colors. Light Blue in positive case,
     * red in negative case
     *
     * @param unformattedValue
     * @return
     */
    public static void formatNumberWithColorsToDisplay(TextView amountTextView, Float unformattedValue) {

        if (unformattedValue == null)
            return;

        Context context = ApplicationClass.getAppContext();
        String toFormat = formatNumberToDisplay(unformattedValue);

        if (unformattedValue < 0) {
//            toFormat = "- " + toFormat;
            amountTextView.setTextColor(context.getResources().getColor(R.color.red));
            amountTextView.setText(toFormat);
        } else if (unformattedValue >= 0) {
//            toFormat = "+ " + toFormat;
            amountTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            amountTextView.setText(toFormat);
        }

        return;
    }

    public static void formatNumberChangeColorBalanceToDisplay(TextView textView, BigDecimal product) {
        Context context = ApplicationClass.getAppContext();
        if (product.compareTo(BigDecimal.ZERO) < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        textView.setText(FormatterClass.formatBigDecimalToDisplay(product));
    }

    /**
     * Formats a number to be ready to send to the server
     *
     * @param unformattedValue
     * @return
     */
    public static BigDecimal formatNumberToServer(String unformattedValue) {

        BigDecimal bd1 = null;

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat nf = NumberFormat.getInstance(currentLocale);
        try {
            bd1 = new BigDecimal(nf.parse(unformattedValue).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bd1;

    }

    public static String formatNumberToDisplayXDecimals(String unformattedValue, int numDecimals) {

        if (unformattedValue == null || unformattedValue.isEmpty())
            return null;

        unformattedValue = unformattedValue.replaceAll(",", ".");

        BigDecimal unformattedValueBig = new BigDecimal(unformattedValue);

        unformattedValueBig = unformattedValueBig.setScale(numDecimals,
                RoundingMode.HALF_UP);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat formatter = NumberFormat.getInstance(currentLocale);
        formatter.setMaximumFractionDigits(numDecimals);
        formatter.setMinimumFractionDigits(numDecimals);

        return formatter.format(unformattedValueBig);

    }

    public static String formatDateToDisplay(String dateString) {

        if (dateString == null) {
            return null;
        }

        DateTime dt = getDateTimeObject(dateString);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");

        String str = formatter.print(dt);

        return str;
    }

    public static String formatMaturityDateToDisplay(String dateString) {

        if (dateString == null) {
            return null;
        }

        DateTime dt = getDateTimeObject(dateString);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-yyyy");

        String str = formatter.print(dt);

        return str;
    }

    public static String formatTimeToDisplay(String updateDate) {
        DateTime dt = getDateTimeObject(updateDate);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm zzz");

        String str = formatter.print(dt);

        return str;
    }

    public static String formatDateToDisplayOnPicker(String dateString) {

        if (TextUtils.isEmpty(dateString)) {
            return null;
        }

        String[] date = formatDateToDisplay(dateString).split("-");

        Locale loc = ApplicationClass.getAppContext().getResources().getConfiguration().locale;

        Calendar cal = new GregorianCalendar(Integer.valueOf(date[2]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[0]));

        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, loc);

        String str = cal.get(Calendar.DAY_OF_MONTH) + " " + month + " " + cal.get(Calendar.YEAR);

        return str;
    }

    public static String formatDateToDisplayEU(String dateString) {

        if (dateString == null) {
            return null;
        }

        DateTimeParser[] parsers = {
                DateTimeFormat.forPattern("dd-MM-yyyy")
                        .getParser()
        };
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(
                null, parsers).toFormatter();

        DateTime dt = new DateTime(dateString);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        String str = formatter.withLocale(currentLocale).print(dt);

        return str;
    }

    //

    /**
     * Formats a date/time to phone locale
     *
     * @param dateString
     * @return
     */
    public static String formatDateTimeToDisplay(String dateString) {

        dateString = dateString.split("\\+")[0];

        DateTimeParser[] parsers = {
                DateTimeFormat.forPattern(
                        "dd-MM-yyyy | HH:mm").getParser()  // Standard bank date format
        };
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(
                null, parsers).toFormatter();

        DateTime dt = new DateTime(dateString);

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        String str = formatter.withLocale(currentLocale).print(dt);

        return str;
    }

    /**
     * Formats a date/time to phone locale
     *
     * @param dateString
     * @return
     */
    public static String formatYearMonthToDisplay(String dateString) {
        String str = "";
        if (dateString == null) {
            return null;
        }
        if (dateString.length() == 6) {
            str = dateString.substring(0, 4) + "/" + dateString.substring(4);
        }

        return str;
    }

    /**
     * Formats a date to be ready to send to the server
     *
     * @param dateString
     * @return
     */
    public static String formatDateToServer(String dateString) {

        DateTime dateTime = getDateTimeObject(dateString);

        return dateTime.toString();
    }

    public static String formatDateTimeToServer(String dateString) {
        String aaa = null;
        String bbb = null;
        try {
            aaa = dateString.substring(0, dateString.indexOf("."));
            return aaa;
        } catch (StringIndexOutOfBoundsException e) {
            try {
                bbb = dateString.substring(0, 19);
                return bbb;
            } catch (StringIndexOutOfBoundsException ee) {
                //
            }
        }
        DateTime dateTime = getDateTimeObject(dateString);
        return dateTime.toString();
    }


    public static String formatNumberToMonth(int month, boolean fullName) {

        Context context = ApplicationClass.getAppContext();
        Locale currentLocale = context.getResources().getConfiguration().locale;

        DateTime dt = DateTime.now().withMonthOfYear(month);// .toString("MMMM");

        DateTimeFormatter fmt;

        if (fullName) {
            fmt = DateTimeFormat.forPattern("MMMM");
        } else {
            fmt = DateTimeFormat.forPattern("MMM");
        }
        String monthName = fmt.withLocale(currentLocale).print(dt);

        return monthName;
    }

    public static void formatBalanceTextColor(TextView balanceTextView,
                                              String balance, TextView currencyTextView, String currency) {

        if (balanceTextView != null && balance != null
                && currencyTextView != null && currency != null) {
            Context context = ApplicationClass.getAppContext();

            try {
                balance = formatNumberToDisplay(balance);
            } catch (Exception e) {
                L.e(TAG, "formatnumber error");
            }

            balanceTextView.setText(balance);
            currencyTextView.setText(currency);

            if (balance.contains("-")) {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.red));
                currencyTextView.setTextColor(context.getResources().getColor(
                        R.color.red));
            } else if (balance.equalsIgnoreCase("0,00")
                    || balance.equalsIgnoreCase("0.00")
                    || balance.equalsIgnoreCase("0,0")
                    || balance.equalsIgnoreCase("0.0")
                    || balance.equalsIgnoreCase("0")) {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.text));
                currencyTextView.setTextColor(context.getResources().getColor(
                        R.color.text));
            } else {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.colorPrimary));
                currencyTextView.setTextColor(context.getResources().getColor(
                        R.color.colorPrimary));
            }

        } else if (balanceTextView != null && balance != null) {
            Context context = ApplicationClass.getAppContext();

            balanceTextView.setText(formatNumberToDisplay(balance));

            if (balance.contains("-")) {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.red));
            } else if (balance.equalsIgnoreCase("0,00")
                    || balance.equalsIgnoreCase("0.00")) {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.colorPrimary));
            } else {
                balanceTextView.setTextColor(context.getResources().getColor(
                        R.color.green));
            }

        }

    }

    public static void formatBigDecimalTextColor(TextView balanceTextView,
                                                 BigDecimal balanceBd, TextView currencyTextView, String currency) {

        String balance = null;
        if (balanceTextView != null && balanceBd != null
                && currencyTextView != null && currency != null) {
            Context context = ApplicationClass.getAppContext();

            try {
                balance = formatBigDecimalToDisplay(balanceBd);
            } catch (Exception e) {
                L.e(TAG, "formatnumber error");
            }

            balanceTextView.setText(balance);
            currencyTextView.setText(currency);

            switch (balanceBd.signum()) {
                case -1:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.red));
                    currencyTextView.setTextColor(context.getResources().getColor(
                            R.color.red));
                    break;
                case 0:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.text));
                    currencyTextView.setTextColor(context.getResources().getColor(
                            R.color.text));
                    break;
                case 1:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.colorPrimary));
                    currencyTextView.setTextColor(context.getResources().getColor(
                            R.color.colorPrimary));
                    break;
            }

        } else if (balanceTextView != null && balance != null) {
            Context context = ApplicationClass.getAppContext();

            try {
                balance = formatBigDecimalToDisplay(balanceBd);
            } catch (Exception e) {
                L.e(TAG, "formatnumber error");
            }

            balanceTextView.setText(balance);

            switch (balanceBd.signum()) {
                case -1:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.red));
                    break;
                case 0:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.text));
                    break;
                case 1:
                    balanceTextView.setTextColor(context.getResources().getColor(
                            R.color.colorPrimary));
                    break;
            }

        }

    }

    public static final DateTime getDateTimeObject(String dateString) {
        DateTime dateTime;

        try {
            dateTime = new DateTime(dateString);
        } catch (IllegalArgumentException e) {
            DateTimeFormatter inputFormatter = DateTimeFormat.forPattern("dd-MM-yyyy");
            dateTime = inputFormatter.parseDateTime(dateString);
        }

        return dateTime;

    }

    public static String formatStringToNIB(String unformattedValue) {
        if (unformattedValue != null && !unformattedValue.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            int size = unformattedValue.length();
            for (int i = 0; i < size; i++) {
                if (i == 21) {
                    break;
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static String formatStringToContractNumber(String unformattedValue) {
        if (unformattedValue != null && !unformattedValue.equals("")) {

            StringBuilder stringBuilder = new StringBuilder();
            int size = unformattedValue.length();
            for (int i = 0; i < size; i++) {
                if (i == 15) {
                    break;
                }
                stringBuilder.append(unformattedValue.charAt(i));
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static String formatBooleanToString(boolean flag) {
        if (flag == true) {
            return ApplicationClass.getAppContext().getResources().getString(R.string.yes);
        } else {
            return ApplicationClass.getAppContext().getResources().getString(R.string.no);
        }
    }

}
