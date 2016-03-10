package voon.truongvan.english_for_all_level.controller;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import voon.truongvan.english_for_all_level.api.IDataController;
import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.model.DataItem;
import voon.truongvan.english_for_all_level.model.TestContent;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by voqua on 12/20/2015.
 */
public class AssetDataController implements IDataController, AppConstant {

    private static AssetDataController instance;
    public static synchronized AssetDataController getInstance(){
        if(instance == null){
            instance = new AssetDataController();
        }
        return instance;
    }
    private AssetDataController(){}

    private DataItem dataItem;

    public void loadDataItem(Context context) {
        try {
            String fileData = readFile(context, JSON_DATA_FILE);
            Type listType = new TypeToken<DataItem>(){}.getType();
            dataItem = new Gson().fromJson(fileData, listType);
        } catch (IOException e) {
            Utils.Log(e);
        }
    }

    public TestContent loadTestFile(Context context, String path) throws IOException {
        ArrayList<String> lines = readFileAsArray(context, path);
        return QuestionHelper.readQuestion(lines);
    }

    public DataItem getGrammarDataItem() {
        return dataItem;
    }

    private String readFile(Context context, String path) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path)));
            StringBuffer stringBuffer = new StringBuffer();

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                stringBuffer.append(mLine+"\n");
            }
            return stringBuffer.toString();
        } finally {
            if(reader!=null) {
                reader.close();
            }
        }
    }

    private ArrayList<String> readFileAsArray(Context context, String path) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path)));
            ArrayList<String> lines = new ArrayList<String>();
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if(mLine.trim().length()==0){
                    mLine = mLine.trim();
                }
                lines.add(mLine);
            }
            return lines;
        } finally {
            if(reader!=null) {
                reader.close();
            }
        }
    }
}
