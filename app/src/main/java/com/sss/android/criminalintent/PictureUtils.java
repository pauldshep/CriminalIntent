package com.sss.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by Paul Shepherd on 12/4/2015.
 */
public class PictureUtils
{
    private final static String TAG = "PictureUtils";


    //==========================================================================
    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x / 2, size.y / 2);
    }

    //==========================================================================
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight)
    {
        Log.d(TAG, "getScaledBitmap(path = " + path +
                ", destWidth = " + destWidth +
                ", destHeight = " + destHeight + ")");

        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds    = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth  = options.outWidth;
        float srcHeight = options.outHeight;

        // figure out how much to scale down by
        int inSampleSize = 1;
        if((srcHeight > destHeight) || (srcWidth > destWidth))
        {

            if(srcWidth > srcHeight)
            {
                inSampleSize = Math.round(srcHeight / destHeight);
            }
            else
            {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        // rotate the scaled bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotated;
    }   // end public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight)
}   // end public class PictureUtils
