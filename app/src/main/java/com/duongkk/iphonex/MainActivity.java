package com.duongkk.iphonex;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 3000;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.sw)
    Switch sw;
    @BindView(R.id.on)
    LinearLayout on;
    @BindView(R.id.faq)
    LinearLayout faq;
    @BindView(R.id.ll_rate)
    LinearLayout llRate;
    @BindView(R.id.ll_infor)
    LinearLayout llInfor;
    private SPUtils spUtils;
    private WindowManager windowManager;
    private AdView mAdView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        checkDrawOverlayPermission();
        spUtils = new SPUtils("setting");
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Build.VERSION.SDK_INT < 23) {
                        MainActivity.this.start();
                    } else if (Settings.canDrawOverlays(MainActivity.this.getApplicationContext())) {
                        MainActivity.this.start();
                    } else {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "Please permit drawing over apps.", Toast.LENGTH_SHORT).show();
                        checkDrawOverlayPermission();
                    }
                } else {
                    stop();
                }
                spUtils.put("on", b);
            }

        });
        boolean on = spUtils.getBoolean("on", false);
        sw.setChecked(on);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //todo Add ADS
        MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.id_admob_app));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_ad_unit_id2));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // showInterstitial();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadInterstitial();
            }
        });
        loadInterstitial();
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("1C8067A9CD67109A760B8802C99C0F4D")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void start() {
        if (!isMyServiceRunning(OverlayService.class)) {
            startService(new Intent(this, OverlayService.class));
            showInterstitial();
        }
    }

    public void stop() {
        if (isMyServiceRunning(OverlayService.class)) {
            stopService(new Intent(this, OverlayService.class));
            OverlayService.removeView();
            showInterstitial();
        }
    }

    @RequiresApi(api = 23)
    public void checkDrawOverlayPermission() {
        try {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 3000);
        } catch (ActivityNotFoundException e) {
        }
    }

    @RequiresApi(api = 23)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3000) {
            if (Settings.canDrawOverlays(this)) {
                start();
            } else {
                sw.setChecked(false);
            }
        }
    }

    protected void onDestroy() {

        super.onDestroy();
    }

    protected void onPause() {

        super.onPause();
    }

    protected void onResume() {
        super.onResume();

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        for (ActivityManager.RunningServiceInfo service : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.on, R.id.faq, R.id.ll_rate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.on:
                sw.performClick();
                break;
            case R.id.faq:
                CommonUtils.shareEmail("duongkk.dev@gmail.com", this);
                break;
            case R.id.ll_rate:
                CommonUtils.launchMarket(this, getPackageName());
                break;

        }
    }
}
