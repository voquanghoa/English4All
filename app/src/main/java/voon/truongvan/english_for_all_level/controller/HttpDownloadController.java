package voon.truongvan.english_for_all_level.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/21/2015.
 */
public class HttpDownloadController implements AppConstant{
    public interface IDownload{
        void onDownloadDone(String url, byte[] data);
        void onDownloadFail(String message);
        void onDownloadProgress(int done, int total);
    }

    private boolean isDownloading;
    private boolean isStopped;

    private static HttpDownloadController instance;

    public synchronized static HttpDownloadController getInstance(){
        if(instance==null){
            instance = new HttpDownloadController();
        }
        return instance;
    }

    private HttpDownloadController(){}

    public void startDownload(final String url,final IDownload downloadHandler){
        new Thread(new Runnable() {
            public void run() {
                downloadFile(url,downloadHandler);
            }
        }).start();
    }

    public synchronized void stopDownload(){
        if(isDownloading){
            isStopped = true;
        }
    }

    private void downloadFile(String downloadUrl, IDownload downloadHandler) {
        isDownloading = true;
        isStopped = false;
        try {
            URL url = new URL(downloadUrl);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if(isStopped){
                isStopped = false;
                downloadHandler.onDownloadFail("Download cancelled !");
                return;
            }

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    downloadHandler.onDownloadProgress(downloadedSize, totalSize);

                    if(isStopped){
                        isStopped = false;
                        downloadHandler.onDownloadFail("Download cancelled !");
                        inputStream.close();
                        byteArrayOutputStream.close();
                        return;
                    }
                }

                inputStream.close();
                byteArrayOutputStream.close();
                downloadHandler.onDownloadDone(downloadUrl, byteArrayOutputStream.toByteArray());
            }else{
                downloadHandler.onDownloadFail("Download error. Can not download this file.");
                Utils.Log(new Exception("Can not download file " + downloadUrl));
            }
        } catch (Exception e) {
            downloadHandler.onDownloadFail(e.getMessage());
            e.printStackTrace();
        } finally {

        }
    }
}

