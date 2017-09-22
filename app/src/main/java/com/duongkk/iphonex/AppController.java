package com.duongkk.iphonex;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by DuongKK on 9/11/2017.
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Utils.init(this);
    }
}
