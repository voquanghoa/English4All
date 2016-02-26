package voon.truongvan.english_for_all_level.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import voon.truongvan.english_for_all_level.api.IDataController;
import voon.truongvan.english_for_all_level.model.DataItem;
import voon.truongvan.english_for_all_level.model.TestContent;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/21/2015.
 */
public class OnlineDataController implements IDataController {
    private static OnlineDataController instance;
    public static OnlineDataController getInstance(){
        if(instance==null){
            instance = new OnlineDataController();
        }
        return instance;
    }
    private DataItem grammarDataItem;
    private DataItem examinationDataItem;

    private OnlineDataController(){

    }

    private DataItem readDataItem(byte[] data) throws UnsupportedEncodingException {
        Type listType = new TypeToken<DataItem>(){}.getType();
        String strData = new String(data, "UTF-8");
        return new Gson().fromJson(strData, listType);
    }

    public void loadGrammar(byte[] data){
        try{
            grammarDataItem = readDataItem(data);
        }catch (Exception ex){
            Utils.Log(ex);
        }
    }

    public void loadExamination(byte[] data){
        try{
            examinationDataItem = readDataItem(data);
        }catch (Exception ex){
            Utils.Log(ex);
        }
    }

    public TestContent loadTestFile(String fileData) throws UnsupportedEncodingException {
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(fileData.split("\n")));
        return QuestionHelper.readQuestion(lines);
    }

    public DataItem getGrammarDataItem() {
        return grammarDataItem;
    }

    public DataItem getExaminationDataItem(){
        return examinationDataItem;
    }
}
