package voon.truongvan.english_for_all_level.control;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import voon.truongvan.english_for_all_level.R;

/**
 * Created by voqua on 3/7/2016.
 */
public class AppTitleControl extends RelativeLayout {
    private TextView titleText;
    private EffectImageView appTitleBackButton;
    public AppTitleControl(Context context) {
        super(context);
        initView();
    }

    public AppTitleControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AppTitleControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.app_title_layout_new, this, true);
        appTitleBackButton = (EffectImageView)findViewById(R.id.app_title_back);
        appTitleBackButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                ((Activity)getContext()).onBackPressed();
            }
        });
        titleText = (TextView) findViewById(R.id.txt_title);
    }

    public void setText(String text) {
        titleText.setText(text);
    }
}
