package voon.truongvan.english_for_all_level.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.model.UserResult;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/26/2015.
 */
public class UserResultController implements AppConstant{
    private static UserResultController instance;
    public static synchronized UserResultController getInstance(){
        if(instance==null){
            instance = new UserResultController();
        }
        return instance;
    }
    private ArrayList<UserResult> userResults;
    private UserResultController(){
        userResults = new ArrayList<UserResult>();
    }

    private boolean isUpdated = false;
    public boolean isRequestUpdate(){
        boolean needUpdate = isUpdated;
        isUpdated = false;
        return needUpdate;
    }

    public void setResult(String fileName, int correct, int total){
        UserResult userResult = getResult(fileName);

        if(userResult==null){
            userResult = new UserResult();
            userResult.setFileName(fileName);
            userResults.add(userResult);
        }

        userResult.setCorrect(correct);
        userResult.setTotal(total);
        save();
        isUpdated = true;
    }

    public UserResult getResult(String fileName){
        for (UserResult userResult : userResults){
            if(userResult.getFileName().equals(fileName)){
                return userResult;
            }
        }
        return null;
    }

    public synchronized void load(){
        try {
            String fileData = FileStoreController.getInstance().readFile(USER_RESULT_FILE);
            Type listType = new TypeToken<ArrayList<UserResult>>(){}.getType();
            userResults = new Gson().fromJson(fileData, listType);
        } catch (Exception e) {
            Utils.Log(e);
        }finally {
            if(userResults==null){
                userResults = new ArrayList<UserResult>();
            }
        }
    }

    public synchronized void save(){
        try {
            String fileData = new Gson().toJson(userResults);
            FileStoreController.getInstance().writeFile(USER_RESULT_FILE, fileData);
        } catch (Exception e) {
            Utils.Log(e);
        }
    }
}
