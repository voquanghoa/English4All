package voon.truongvan.english_for_all_level.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import voon.truongvan.english_for_all_level.R;
import voon.truongvan.english_for_all_level.adapter.QuestionAnswerAdapter;
import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.controller.AssetDataController;
import voon.truongvan.english_for_all_level.controller.FileCachingController;
import voon.truongvan.english_for_all_level.controller.HttpDownloadController;
import voon.truongvan.english_for_all_level.controller.OnlineDataController;
import voon.truongvan.english_for_all_level.controller.UserResultController;
import voon.truongvan.english_for_all_level.model.TestContent;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class QuestionActivityBase extends BaseActivity implements Runnable, HttpDownloadController.IDownload {
    private static final int BACK_KEY_DELAY_TIME = 2000;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private int timeDuration = 0;
    private boolean isStopTimer = false;
    private DecimalFormat decimalFormat;
    private EffectImageView submitButton;
    private Runnable setTitleText = new Runnable() {
        public void run() {
            QuestionActivityBase.this.appTitle.setText(getDurationTime());
        }
    };
    private boolean isDelayFinish = true;
    private String currentFileName;
    private String currentFolder;

    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_content_layout);

        appTitle.setText("");
        listView = (ListView) findViewById(R.id.question_list_view);
        submitButton = (EffectImageView) findViewById(R.id.submit_button);
        submitButton.setActivated(false);

        Bundle bundle = getIntent().getExtras();
        currentFileName = bundle.getString(AppConstant.MESSAGE_FILE_NAME);
        currentFolder = bundle.getString(AppConstant.MESSAGE_FOLDER);

        decimalFormat = new DecimalFormat(getString(R.string.digital_format));
        timeDuration = 0;

        loadFileData();
    }

    private void showTestContent(TestContent testContent) {
        try {
            questionAnswerAdapter = new QuestionAnswerAdapter(this, testContent);
            new Thread(this).start();
            runOnUiThread(new Runnable() {
                public void run() {
                    synchronized (questionAnswerAdapter) {
                        listView.setAdapter(questionAnswerAdapter);
                    }
                }
            });
        } catch (Exception e) {
            showLoadFileError();
            Utils.Log(e);
        }
    }

    private void loadFileData(){
        try{
            if(currentFolder.equals(ASSET_FOLDER)){
                showTestContent(AssetDataController.getInstance().loadTestFile(this, currentFileName));
            }else{
                String cacheContent = FileCachingController.getInstance().get(currentFolder + currentFileName);

                if(cacheContent!=null && cacheContent.length()>0){
                    showTestContent(OnlineDataController.getInstance().loadTestFile(cacheContent));
                }else{
                    startDownloadFile();
                }
            }
        }catch (IOException e){
            showLoadFileError();
            Utils.Log(e);
        }

    }

    private void startDownloadFile() {
        showLoadingDialog();
        setProgressMessage(R.string.downloading_file);
        String url = SERVER_BASE_PATH + currentFolder + currentFileName;
        HttpDownloadController.getInstance().startDownload(url, this);
    }

    private void showLoadFileError(){
        showMessage(R.string.can_not_read_file);
        Utils.Log("Can not load file " + currentFileName);
    }

    public void onSubmit(View view){
        if (questionAnswerAdapter ==null || questionAnswerAdapter.isShowAnswer()) {
            this.userRequestFinishActivity();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(R.string.dialog_result_title)
                    .setMessage(questionAnswerAdapter.getResultAsString(getDurationTime()))
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_result_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            questionAnswerAdapter.setShowAnswer(true);
                            submitButton.setActivated(true);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            isStopTimer = true;
            alertDialog.show();
            UserResultController.getInstance().setResult(currentFileName,
                    questionAnswerAdapter.getCorrects(), questionAnswerAdapter.getTotal());
        }
    }

    public void onBackPressed() {
        if (isDelayFinish && questionAnswerAdapter!=null && !questionAnswerAdapter.isShowAnswer()) {
            showMessage(R.string.delay_backkey_message);
            isDelayFinish = false;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isDelayFinish = true;
                }
            }, BACK_KEY_DELAY_TIME);
        } else {
            this.userRequestFinishActivity();
        }
    }

    private void userRequestFinishActivity(){
        if(Utils.checkAds()){
            loadFullAds();

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }
                public void onAdClosed(){
                   QuestionActivityBase.this.finish();
                }
            });
        }else{
            this.finish();
        }
    }

    public void run() {
        while(!isStopTimer){
            try{
                Thread.sleep(1000);
            }catch (Exception ex){
                return;
            }
            timeDuration ++;
            this.runOnUiThread(setTitleText);
        }
    }

    private String getDurationTime() {
        return decimalFormat.format(timeDuration / 60) + ":" + decimalFormat.format(timeDuration % 60);
    }

    public void onDownloadDone(String url, byte[] data) {
        try {
            String fileContent = new String(data, AppConstant.CHARSET);
            FileCachingController.getInstance().add(currentFolder+currentFileName, fileContent);
            showTestContent(OnlineDataController.getInstance().loadTestFile(fileContent));
        } catch (UnsupportedEncodingException e) {
            showLoadFileError();
        }finally {
            closeLoadingDialog();
        }
    }

    public void onDownloadFail(String message) {
        closeLoadingDialog();
        runOnUiThread(new Runnable() {
            public void run() {
                submitButton.setActivated(true);
            }
        });

        showMessage(R.string.download_fail_message);
    }

    public void onDownloadProgress(int done, int total) {
        setProgressMessage(getString(R.string.download_message_format, done / 1024, total / 1024));
    }
}
