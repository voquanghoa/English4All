package voon.truongvan.english_for_all_level.control;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import voon.truongvan.english_for_all_level.R;

/**
 * Created by voqua on 3/15/2016.
 */
public class AnswerRadioButton extends RelativeLayout {
    private final int CORRECT_ANSWER_COLOR = 0xffff00;
    private final int INCORRECT_ANSWER_COLOR = 0xff0000;
    private final int NORMAL_ANSWER_COLOR = 0xffffff;
    private ImageView radioImageView;
    private TextView answerTextView;
    private boolean isTouchDown = false;
    private boolean isCorrectAnswer = false;
    private boolean checked = false;
    private OnCheckedChangeListener listener;

    public AnswerRadioButton(Context context) {
        super(context);
        initView();
    }

    public AnswerRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AnswerRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.answer_radio_button_layout, this);
        radioImageView = (ImageView) findViewById(R.id.radio_icon);
        answerTextView = (TextView) findViewById(R.id.answer_text);
    }

    public void setText(Spanned answerText) {
        answerTextView.setText(answerText);
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrectAnswer = isCorrect;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    isTouchDown = false;
                    setChecked(true);
                    updateIcon();
                    if (listener != null) {
                        listener.onCheckedChanged(this, isChecked());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isTouchDown = false;
                    updateIcon();
                    break;
                case MotionEvent.ACTION_DOWN:
                    isTouchDown = true;
                    updateIcon();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            if (checked) {
                ViewGroup group = (ViewGroup) this.getParent();
                for (int i = group.getChildCount() - 1; i >= 0; i--) {
                    View view = group.getChildAt(i);
                    if (view instanceof AnswerRadioButton && view != this) {
                        ((AnswerRadioButton) view).setChecked(false);
                    }
                }
            }
            updateIcon();
        }
    }

    public void updateTextStyle() {
        if (isEnabled()) {
            answerTextView.setPaintFlags(answerTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            answerTextView.setTextColor(getResources().getColor(R.color.answer_normal_color));
        } else {
            if (isCorrectAnswer) {
                answerTextView.setPaintFlags(answerTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                answerTextView.setTextColor(getResources().getColor(R.color.answer_correct_color));
            } else if (isChecked()) {
                answerTextView.setPaintFlags(answerTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                answerTextView.setTextColor(getResources().getColor(R.color.answer_incorrect_color));
            } else {
                answerTextView.setPaintFlags(answerTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                answerTextView.setTextColor(getResources().getColor(R.color.answer_normal_color));
            }
        }
    }

    public void updateIcon() {
        if (isEnabled()) {
            if (isTouchDown) {
                radioImageView.setImageResource(R.drawable.radio_button_pressed);
            } else if (checked) {
                radioImageView.setImageResource(R.drawable.radio_button_checked);
            } else {
                radioImageView.setImageResource(R.drawable.radio_button_unchecked);
            }
        } else {
            if (isCorrectAnswer) {
                radioImageView.setImageResource(R.drawable.radio_button_correct);
            } else if (isChecked()) {
                radioImageView.setImageResource(R.drawable.radio_button_incorrect);
            } else {
                radioImageView.setImageResource(R.drawable.radio_button_not_select);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(AnswerRadioButton buttonView, boolean isChecked);
    }
}
