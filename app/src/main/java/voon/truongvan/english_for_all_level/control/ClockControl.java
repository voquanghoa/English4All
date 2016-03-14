package voon.truongvan.english_for_all_level.control;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import voon.truongvan.english_for_all_level.util.SafeThread;

/** Created by voqua on 3/7/2016. */ public class ClockControl extends View{ private int fontSize=-1; private int second=10; private float STROKE_PERCENT = 2; private float TEXT_HEIGHT_PERCENT = 45; private int DEFAULT_INTERVAL = 30; private Paint paint = null; private SafeThread thread = null; private Runnable onEnded; private Runnable invalidateRunnable = new Runnable() { public synchronized void run() { ClockControl.this.invalidate(); second--; if (second == -1) { second = 0; onEnded.run(); thread.cancel(); } } }; private Runnable updateRunnable = new Runnable() { public synchronized void run() { ((Activity) getContext()).runOnUiThread(invalidateRunnable); } }; public ClockControl(Context context) { super(context); init(); } public ClockControl(Context context, AttributeSet attrs) { super(context, attrs); init(); } public ClockControl(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(); } private void init(){ paint = new Paint(); paint.setColor(Color.BLACK); paint.setTextAlign(Paint.Align.CENTER); paint.setAlpha(0); paint.setAntiAlias(true); } public void setOnEnded(Runnable onEnded) { this.onEnded = onEnded; }

    public synchronized void stop() {
        if (thread != null) {
            thread.cancel();
        }
        thread = null;
    }

    public synchronized void start(){
        stop();
        second = DEFAULT_INTERVAL;
        thread = new SafeThread();
        thread.setOnUpdate(updateRunnable);
        thread.start();
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        float standardSize = Math.min(width, height);

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        if(fontSize==-1){
            fontSize = getFixedFontSize((int) (height * TEXT_HEIGHT_PERCENT / 100));
            paint.setTextSize(fontSize);
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("" + second, halfWidth, (1 + TEXT_HEIGHT_PERCENT / 100) * height / 2, paint);

        float strokeWidth = STROKE_PERCENT*standardSize/100;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(strokeWidth);
        RectF rectF = new RectF(halfWidth - halfHeight + strokeWidth, strokeWidth,
                halfWidth + halfHeight - strokeWidth, height - strokeWidth);
        canvas.drawOval(rectF, paint);
    }

    private int getFixedFontSize(int height){
        Paint paint = new Paint();
        Rect rect = new Rect();
        final String STRING_TEXT = "00";
        final int MIN_FONT_SIZE = 1;
        final int MAX_FONT_SIZE = 1000;

        int min = MIN_FONT_SIZE;
        int max = MAX_FONT_SIZE;
        int average = (min+max)/2;
        while(average-min>1 && max-average>1){
            paint.setTextSize(average);
            paint.getTextBounds(STRING_TEXT,0,STRING_TEXT.length(), rect);

            if(rect.height()>height){
                max = average;
            }else{
                min = average;
            }
            average = (min+max)/2;
        }

        return average;
    }
}
