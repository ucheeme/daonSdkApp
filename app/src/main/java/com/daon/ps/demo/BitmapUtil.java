package com.daon.ps.demo;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {

    /**
     * Rotate bitmap.
     *
     * @param bitmap  the source bitmap.
     * @param degrees the number of degrees to rotate.
     * @param mirror  if true, mirror image after rotation.
     * @return the rotated bitmap.
     */
    public static Bitmap rotate(Bitmap bitmap, float degrees, boolean mirror) {
        if (bitmap == null) return null;

        Matrix mtx = new Matrix();

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        mtx.postRotate(degrees);

        if (mirror) mtx.postScale(-1.0f, 1.0f);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
