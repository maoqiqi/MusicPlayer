package com.software.march.appcommonlibrary;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description Helper class for screen
 * @date 2016/11/11
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 得到屏幕的宽和高
    // 过时的方式
    // screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    // screenHeight = getWindowManager().getDefaultDisplay().getHeight();

    // 得到屏幕的宽和高最新方式
    // DisplayMetrics displayMetrics = new DisplayMetrics();
    // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    // screenWidth = displayMetrics.widthPixels;
    // screenHeight = displayMetrics.heightPixels;

    // Get screen width
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return -1;
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    // Get screen height
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return -1;
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}