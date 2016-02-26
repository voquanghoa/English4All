package voon.truongvan.english_for_all_level;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;

import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.control.BaseActivity;
import voon.truongvan.english_for_all_level.controller.AssetDataController;
import voon.truongvan.english_for_all_level.controller.HttpDownloadController;
import voon.truongvan.english_for_all_level.controller.OnlineDataController;
import voon.truongvan.english_for_all_level.util.Utils;

public class MainActivity extends BaseActivity implements HttpDownloadController.IDownload, AppConstant {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        AssetDataController.getInstance().loadDataItem(this);

        loadFullAds();

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                finish();
            }
        });
    }

    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Utils.Log("DID NOT LOAD FULL SCREEN ADS");
            super.onBackPressed();
        }
    }

    public void onGrammarClicked(View view){
        if(OnlineDataController.getInstance().getGrammarDataItem() == null) {
            showLoadingDialog();
            HttpDownloadController.getInstance().startDownload(GRAMMAR_JSON_PATH, this);
        }else{
            startActivity(new Intent(this, GrammarActivity.class));
        }
    }

    public void onStudyOfflineClick(View view){
        startActivity(new Intent(this, StudyOfflineActivity.class));
    }

    public void onExaminationClick(View view){
        if(OnlineDataController.getInstance().getExaminationDataItem() == null) {
            showLoadingDialog();
            HttpDownloadController.getInstance().startDownload(EXAMINATION_JSON_PATH, this);
        }else{
            startActivity(new Intent(this, ExaminationActivity.class));
        }
    }

    public void onMoreAppClick(View view){
        showMessage("Not implemented yet !");
    }

    public void onDownloadDone(String downloadUrl, byte[] data) {
        closeLoadingDialog();
        if(downloadUrl.equals(EXAMINATION_JSON_PATH)){
            OnlineDataController.getInstance().loadExamination(data);
            startActivity(new Intent(this, ExaminationActivity.class));
        }else{
            OnlineDataController.getInstance().loadGrammar(data);
            startActivity(new Intent(this, GrammarActivity.class));
        }
    }

    public void onListenExerciseClick(View view) {
        String shareUrl = AppConstant.GPLUS_URL_SHARE;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
        startActivity(intent);
    }

    public void onDownloadFail(String message) {
        closeLoadingDialog();
        showMessage(R.string.download_fail_message);
    }

    public void onDownloadProgress(int done, int total) {
        setProgressMessage("Download " + (done/1024)+" Kb/" + (total/1024)+" Kb.");
    }



    public void onDestroy() {
        super.onDestroy();
    }
}
