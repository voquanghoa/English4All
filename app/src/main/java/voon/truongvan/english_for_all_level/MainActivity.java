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
import voon.truongvan.english_for_all_level.util.RateDialogCreator;
import voon.truongvan.english_for_all_level.util.Utils;

public class MainActivity extends BaseActivity implements HttpDownloadController.IDownload, AppConstant {
    private final String SHOW_PUZZLE_NEW = "ShowPuzzleNew";
    private final String SHOW_RANKING_NEW = "ShowRankingNew";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        AssetDataController.getInstance().loadDataItem(this);

        loadFullAds();

        enableSharedPreferences();
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                finish();
            }
        });
        findViewById(R.id.button_puzzle).setActivated(getSharedPreferencesBoolean(SHOW_PUZZLE_NEW, true));
        findViewById(R.id.button_ranking).setActivated(getSharedPreferencesBoolean(SHOW_RANKING_NEW, true));
    }

    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception ex) {
            Utils.Log(ex);
        }
    }

    public void onPuzzleClick(View view) {
        findViewById(R.id.button_puzzle).setActivated(false);
        setSharedPreferencesBoolean(SHOW_PUZZLE_NEW, false);
        findViewById(R.id.button_puzzle).setActivated(false);
    }

    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Utils.Log("DID NOT LOAD FULL SCREEN ADS");
            super.onBackPressed();
        }
    }

    public void showRanking(View view) {
        startActivity(new Intent(this, RankingActivity.class));
        setSharedPreferencesBoolean(SHOW_RANKING_NEW, false);
        findViewById(R.id.button_ranking).setActivated(false);
    }

    public void onGrammarClicked(View view) {
        if (OnlineDataController.getInstance().getGrammarDataItem() == null) {
            showLoadingDialog();
            HttpDownloadController.getInstance().startDownload(GRAMMAR_JSON_PATH, this);
        } else {
            startActivity(new Intent(this, GrammarActivity.class));
        }
    }

    public void onStudyOfflineClick(View view) {
        startActivity(new Intent(this, StudyOfflineActivity.class));
    }

    public void onExaminationClick(View view) {
        if (OnlineDataController.getInstance().getExaminationDataItem() == null) {
            showLoadingDialog();
            HttpDownloadController.getInstance().startDownload(EXAMINATION_JSON_PATH, this);
        } else {
            startActivity(new Intent(this, ExaminationActivity.class));
        }
    }

    public void onDownloadDone(String downloadUrl, byte[] data) {
        closeLoadingDialog();
        if (downloadUrl.equals(EXAMINATION_JSON_PATH)) {
            OnlineDataController.getInstance().loadExamination(data);
            startActivity(new Intent(this, ExaminationActivity.class));
        } else {
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
        setProgressMessage(getString(R.string.download_message_format, done / 1024, total / 1024));
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        RateDialogCreator.resume(this);
        super.onResume();
    }
}
