package com.ebankit.android.core.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * Created by E380 on 19/04/2016.
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();


    /**
     * returns the bytesize of the give bitmap
     */
    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    /**
     * SCALING Bitmap
     * calculateInSampleSize
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * SCALING Bitmap
     * Bitmap memory size is adjusted depending on input dimensions
     * decodeSampledBitmapFromResource
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * bitmaps.set(
     * position,
     * BitmapUtils.decodeSampledBitmapFromByteArray(
     * imageBytes,
     * context.getResources().getDimensionPixelSize(R.dimen.width),
     * context.getResources().getDimensionPixelSize(R.dimen.height)));
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] byteArray,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }


    /**
     * DETERIORATE QUALITY Bitmap
     * Bitmap memory size is adjusted depending on input dimensions
     * Bitmap memory size is cut to half
     * decodePixelDeteriorationBitmapFromResource
     */
    public static Bitmap decodePixelDeteriorationBitmapFromResource(Resources res, int resId,
                                                                    int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565; // 2 bytes per pixel, not 4
        options.inDither = true;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodePixelDeteriorationBitmapFromByteArray(byte[] byteArray,
                                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565; // 2 bytes per pixel, not 4
        options.inDither = true;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }


    /**
     * Cut Bitmap
     * Bitmap memory size is adjusted depending on input dimensions
     * decodeCropBitmapFromResource
     */
    public static Bitmap decodeCropBitmapFromResource(Resources res, int resId,
                                                      int x, int y,
                                                      int width, int height) {

        Bitmap bitmap;
        Bitmap croppedBitmap;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // Enables bitmap re-usage
        options.inMutable = true;

        /* EFFICIENT Crop ? - Creates new Bitmap */ // <- Best option?
        bitmap = BitmapFactory.decodeResource(res, resId, options);
        croppedBitmap = Bitmap.createBitmap(bitmap,
                x, y,
                width, height);
        bitmap.recycle();

        /* EFFICIENT Crop ? - Reuses bitmap, but it is slow */
//        croppedBitmap = BitmapFactory.decodeResource(res, resId, options);
//        int[] pixels = getPixelsFromBitmap(croppedBitmap,
//                x, y,
//                width, height);
//        croppedBitmap.setPixels(pixels, 0, width,
//                0, 0,
//                width, height);

        return croppedBitmap;
    }

    public static Bitmap decodeCropBitmapFromByteArray(byte[] byteArray,
                                                       int x, int y,
                                                       int width, int height) {

        Bitmap bitmap;
        Bitmap croppedBitmap;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // Enables bitmap re-usage
        options.inMutable = true;

        /* EFFICIENT Crop ? - Creates new Bitmap */ // <- Best option?
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        croppedBitmap = Bitmap.createBitmap(bitmap,
                x, y,
                width, height);
        bitmap.recycle();

        /* EFFICIENT Crop ? - Reuses bitmap, but it is slow */
//        croppedBitmap = BitmapFactory.decodeResource(res, resId, options);
//        int[] pixels = getPixelsFromBitmap(croppedBitmap,
//                x, y,
//                width, height);
//        croppedBitmap.setPixels(pixels, 0, width,
//                0, 0,
//                width, height);

        return croppedBitmap;
    }

    public static int[] getPixelsFromBitmap(Bitmap bm, int x, int y, int width, int height) {
        int[] pixels = new int[width * height];

        int tSize = byteSizeOf(bm);
        int size = width * height;
        int tWidth = bm.getWidth();
        int tHeight = bm.getHeight();
        int a = x;
        int b = y;
        int j = 0;
        for (int i = a + b * tWidth; i < size; i++) {
            if (a == x + width) {
                a = x;
                b++;
            }
            pixels[j++] = bm.getPixel(a, b);
            a++;
        }

        return pixels;
    }
}

