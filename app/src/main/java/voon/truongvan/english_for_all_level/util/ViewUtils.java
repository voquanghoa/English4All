package voon.truongvan.english_for_all_level.util;

import android.view.View;

/**
 * Created by Vo Quang Hoa on 12/23/2015.
 */
public class ViewUtils {
    public static void setViewVisibility(View view, boolean isShow){
        view.setVisibility(isShow? View.VISIBLE: View.GONE);
    }
}
