package com.tripasfactory.thetripaslibrary.Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.tripasfactory.thetripaslibrary.R;


/**
 * Created by Rui Vieira on 25/02/2015.
 */
public class PhotoRounder {
    public static final int DP_BORDER_MENU_SIZE = 4;
    public static final int DP_BORDER_PROFILE_PHOTO_SIZE = 4;
    public static final int DP_CORNER_SIZE = 6;
    public static final int DP_CORNER_OPERATIONS_SIZE = 8;

    public static final int PHOTO_DEFAULT_WIDTH = 120;
    public static final int PHOTO_DEFAULT_HEIGHT = 120;

    public PhotoRounder() {
        super();
    }

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap, int cornerDips) {
        int width = PHOTO_DEFAULT_WIDTH;
        int height = PHOTO_DEFAULT_HEIGHT;
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        // adjust bitmap size
        bitmap = Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false);

        Bitmap output = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornerBitmapWithBorder(Context context, Bitmap bitmap, int borderDips, int cornerDips) {
        int width = PHOTO_DEFAULT_WIDTH;
        int height = PHOTO_DEFAULT_HEIGHT;
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        // adjust bitmap size
        bitmap = Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false);

        Bitmap output = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderDips,
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        return output;
    }

    public static Bitmap getRoundedCornerBitmapFromDrawable(Context context, Drawable drawable, int cornerDips) {
        if (drawable instanceof BitmapDrawable) {
            return getRoundedCornerBitmap(context, ((BitmapDrawable) drawable).getBitmap(), cornerDips);
        }

        int width = PHOTO_DEFAULT_WIDTH;
        int height = PHOTO_DEFAULT_HEIGHT;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // adjust bitmap size
        bitmap = Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return getRoundedCornerBitmap(context, bitmap, cornerDips);
    }

    public static Bitmap getRoundedCornerBitmapFromDrawableWithBorder(Context context, Drawable drawable, int borderDips, int cornerDips) {
        if (drawable instanceof BitmapDrawable) {
            return getRoundedCornerBitmapWithBorder(context, ((BitmapDrawable) drawable).getBitmap(), borderDips, cornerDips);
        }


        int width = PHOTO_DEFAULT_WIDTH;
        int height = PHOTO_DEFAULT_HEIGHT;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // adjust bitmap size
        bitmap = Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return getRoundedCornerBitmapWithBorder(context, bitmap, borderDips, cornerDips);
    }

}
