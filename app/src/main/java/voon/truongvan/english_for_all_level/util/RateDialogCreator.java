package voon.truongvan.english_for_all_level.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import voon.truongvan.english_for_all_level.R;

/**
 * Created by voqua on 3/16/2016.
 */
public class RateDialogCreator {
    private final static String MARKET_URL_PATTERN = "market://details?id=%s";
    private final static String MARKET_WEB_URL_PATTERN = "https://play.google.com/store/apps/details?id=%s";
    private final static int DAYS_UNTIL_PROMPT = 1;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;
    private final static int MINUTE_MILLISECOND = 60 * 1000;
    private final static int DAY_MILLISECOND = 24 * 60 * MINUTE_MILLISECOND;

    private final static String DONT_SHOW_AGAIN = "dont_show_rate_again";
    private final static String LAST_SHOW_DATE = "last_show_date";
    private final static String SHARE_PREFERENCE_KEY = "app_rater";
    private final static String APP_LOADED_TIME = "app_load_time";

    public static void init(Context context) {
        saveLongValue(context, APP_LOADED_TIME, System.currentTimeMillis());
    }

    public static void resume(Context context) {
        if (checkWillShow(context)) {
            show(context);
        }
    }

    public static boolean checkWillShow(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCE_KEY, 0);
        boolean dontShow = prefs.getBoolean(DONT_SHOW_AGAIN, false);

        if (!dontShow) {
            long lastShowDate = prefs.getLong(LAST_SHOW_DATE, 0);
            if (lastShowDate == 0) {
                long appLoadTime = prefs.getLong(APP_LOADED_TIME, 0);
                return System.currentTimeMillis() - appLoadTime > MINUTE_MILLISECOND * LAUNCHES_UNTIL_PROMPT;
            } else {
                return System.currentTimeMillis() - lastShowDate > DAYS_UNTIL_PROMPT * DAY_MILLISECOND;
            }
        }
        return false;
    }

    public static void show(final Context context) {
        View rateDialogContent = LayoutInflater.from(context).inflate(R.layout.rate_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rateDialogContent);
        final AlertDialog dialog = builder.create();
        dialog.show();
        clearBackground(rateDialogContent);

        saveLongValue(context, LAST_SHOW_DATE, System.currentTimeMillis());

        rateDialogContent.findViewById(R.id.bt_rate_yes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveBoolValue(context, DONT_SHOW_AGAIN, true);
                dialog.dismiss();
                rateApp(context);
            }
        });

        rateDialogContent.findViewById(R.id.bt_rate_no).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveBoolValue(context, DONT_SHOW_AGAIN, true);
                dialog.dismiss();
            }
        });

        rateDialogContent.findViewById(R.id.bt_rate_later).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                saveLongValue(context, LAST_SHOW_DATE, System.currentTimeMillis());
            }
        });
    }

    private static void saveLongValue(Context context, String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCE_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static void saveBoolValue(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARE_PREFERENCE_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void rateApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String packageName = context.getPackageName();

        String marketLink = String.format(MARKET_URL_PATTERN, packageName);

        intent.setData(Uri.parse(marketLink));
        if (!SafeStartActivity(context, intent)) {
            String marketWebLink = String.format(MARKET_WEB_URL_PATTERN, packageName);
            intent.setData(Uri.parse(marketWebLink));
            if (!SafeStartActivity(context, intent)) {
                Toast.makeText(context, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean SafeStartActivity(Context context, Intent aIntent) {
        try {
            context.startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private static void clearBackground(View rateDialogContent) {
        do {
            if (rateDialogContent.getParent() instanceof View) {
                rateDialogContent = (View) rateDialogContent.getParent();
                rateDialogContent.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            } else {
                break;
            }
        } while (true);
    }

}
