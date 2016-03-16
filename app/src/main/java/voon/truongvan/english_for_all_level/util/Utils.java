package voon.truongvan.english_for_all_level.util;

import android.util.Log;

import java.util.Random;

/**
 * Created by Vo Quang Hoa on 20/09/2015.
 */
public class Utils {
    private static final int ADS_SHOW_RATIO = 3;
    private static String TAG = "ENGLISH_PUZZLE";
    private static String TAG_ERROR = "ENGLISH_PUZZLE_ERROR";
    private static int adsCheck = 0;

    private static void Log(String content, String tag){
        if(content==null){
            content = "(Empty log)";
        }
        Log.i(tag, content);
    }

    public  static void Log(String content){
        Log(content, TAG);
    }

    public static void Log(Exception exception){
        Log(exception.getMessage(),TAG_ERROR);
        exception.printStackTrace();
    }

    public static boolean checkAds(){
        return  ++adsCheck % ADS_SHOW_RATIO == 0;
    }

    public static boolean isArrayContainsElement(int[] array, int element, int fromIndex, int toIndex){
        for(int i=fromIndex;i<=toIndex;i++){
            if(array[i]==element){
                return true;
            }
        }
        return false;
    }
    public static int[] getRandomArray(int size){
        int[] result = new int[size];
        Random random = new Random();
        final int MAX_NUMBER = 22590 - 1;

        for(int i=0;i<result.length;i++){
            do{
                result[i] = random.nextInt(MAX_NUMBER);
            }while (isArrayContainsElement(result,result[i],0,i-1));
        }
        return result;
    }
}
