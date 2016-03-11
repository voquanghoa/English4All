package voon.truongvan.english_for_all_level.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import voon.truongvan.english_for_all_level.R;
import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.leaderboard.BaseGameActivity;

/**
 * Created by voqua on 12/20/2015.
 */
public class BaseActivity extends BaseGameActivity implements DialogInterface.OnCancelListener, AppConstant {
    protected AppTitleControl appTitle;
    private ProgressDialog progressDialog;
    private AdView adView;
    private static AdRequest adRequest;

    protected InterstitialAd mInterstitialAd;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(adRequest == null){
            adRequest = createAdsRequest();
        }
    }

    protected void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EF966C3E6FD639F322B1250C72187DF5")
                .addTestDevice("E62072DEC66B8E891FC23264834F5CCA")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    protected void loadFullAds(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_id_full_screen));
        requestNewInterstitial();
    }

    protected AdRequest createAdsRequest(){
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EF966C3E6FD639F322B1250C72187DF5")
                .addTestDevice("E62072DEC66B8E891FC23264834F5CCA")
                .build();
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        appTitle = (AppTitleControl) findViewById(R.id.app_title);
        RelativeLayout adsLayout = (RelativeLayout)findViewById(R.id.adView);
        if(adsLayout!=null){
            String AD_UNIT_ID = getString(R.string.admob_id);
            adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            adsLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }


    protected synchronized void showLoadingDialog(){
        if(progressDialog==null){
            progressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
            progressDialog.setOnCancelListener(this);
        }
        progressDialog.show();
    }

    protected synchronized void closeLoadingDialog(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }
        });
    }

    protected synchronized void setProgressMessage(final int messageId){
        this.setProgressMessage(getString(messageId));
    }

    protected synchronized void setProgressMessage(final String message){
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
            }
        });
    }

    public void onCancel(DialogInterface dialog) {

    }

    protected void showMessage(final String content){
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(BaseActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void showMessage(final int contentId) {
        showMessage(getString(contentId));
    }

    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    protected void enableSharedPreferences(){
        sharedPreferences = getSharedPreferences("English4All", Context.MODE_PRIVATE);
    }

    protected boolean getSharedPreferencesBoolean(String preferencesKey, boolean defaultValue){
        return sharedPreferences.getBoolean(preferencesKey, defaultValue);
    }

    protected void setSharedPreferencesBoolean(String preferencesKey, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferencesKey, value);
        editor.commit();
    }

}