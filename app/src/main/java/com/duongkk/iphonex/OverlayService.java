package com.duongkk.iphonex;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class OverlayService extends Service {
    static View view;
    static WindowManager windowManager;
    final double hParam = 0.24d;
    final double wParam = 2.0d;

    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        drawPortait();
    }

    public void drawPortait() {
        int i;
        view = View.inflate(getApplicationContext(), R.layout.bump, null);
        int yppi = (int) (2.0d * ((double) getYPPI()));
        int xppi = (int) (0.24d * ((double) getXPPI()));
        if (VERSION.SDK_INT < 26) {
            i = 2006;
        } else {
            i = 2038;
        }
        LayoutParams params = new LayoutParams(yppi, xppi, i, 296, -3);
        params.gravity = 49;
        windowManager.addView(view, params);
    }

    public void drawLandscape() {
        view = View.inflate(getApplicationContext(), R.layout.bump_l, null);
        LayoutParams params = new LayoutParams((int) (0.24d * ((double) getYPPI())), (int) (2.0d * ((double) getXPPI())), VERSION.SDK_INT < 26 ? 2006 : 2038, 8, -3);
        params.gravity = GravityCompat.START;
        windowManager.addView(view, params);
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return START_STICKY;
    }

    public static void removeView() {
        if (view != null && windowManager != null) {
            windowManager.removeView(view);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == 2) {
            removeView();
            drawLandscape();
        } else if (newConfig.orientation == 1) {
            removeView();
            drawPortait();
        }
    }

    public float getXPPI() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.w("XDPI", metrics.xdpi + "");
        return metrics.xdpi;
    }

    public float getYPPI() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.w("YDPI", metrics.xdpi + "");
        return metrics.ydpi;
    }
}
