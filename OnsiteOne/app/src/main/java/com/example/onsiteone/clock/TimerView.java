package com.example.onsiteone.clock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.onsiteone.R;

public class TimerView extends View {

    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int numeralSpacing = 0;
    private int handsize = 0,minutehandsize=0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {5,10,15,20,25,30,35,40,45,50,55,60};
    private int[] numbersRef = {1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();
    private Context context;
    private int sec = 0, min = 0;

    public void setMin( int min ) {
        this.min = min;
    }

    public void setSec( int sec ) {
        this.sec = sec;
    }

    public TimerView( Context context ) {
        super ( context );
        this.context = context;
    }

    public TimerView( Context context , @Nullable AttributeSet attrs ) {
        super ( context , attrs );
        this.context = context;
    }

    public TimerView( Context context , @Nullable AttributeSet attrs , int defStyleAttr ) {
        super ( context , attrs , defStyleAttr );
        this.context = context;
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handsize = min / 7;
        minutehandsize = (int) (min / 4.5);
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw( Canvas canvas) {
        if (!isInit) {
            initClock();
        }

        canvas.drawColor( Color.TRANSPARENT);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHand(canvas, sec);
        drawCircleMinutes ( canvas );
        drawHandMinutes (canvas, min);
        invalidate();
    }

    private void drawHand(Canvas canvas, double loc) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = radius - handsize;

        paint.setStrokeWidth ( 10 );
        paint.setStrokeCap ( Paint.Cap.ROUND );
        canvas.drawLine(width / 2, height / 2, (float) (width / 2 + Math.cos(angle) * handRadius), (float) (height / 2 + Math.sin(angle) * handRadius), paint);
    }

    private void drawHandMinutes(Canvas canvas, double loc) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = radius - minutehandsize - handsize;

        paint.setStrokeWidth ( 5 );
        paint.setStrokeCap ( Paint.Cap.ROUND );
        canvas.drawLine(width / 2, height / 2, (float) (width / 2 + Math.cos(angle) * handRadius), (float) (height / 2 + Math.sin(angle) * handRadius), paint);
    }


    private void drawNumeral(Canvas canvas) {
        paint.setTextSize(fontSize);

        for (int i = 0; i<numbers.length; i++) {
            String tmp = String.valueOf(numbers[i]);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (numbersRef[i] - 3);
            int x = (int) (width / 2 + Math.cos(angle) * (radius-70) - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * (radius-70) + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor ( Color.RED );
        canvas.drawCircle(width / 2, height / 2, 12, paint);
    }


    private void drawCircle(Canvas canvas) {
        Bitmap clock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock);
        Bitmap sized = Bitmap.createScaledBitmap ( clock,width,height,true );
        float drawHeight = (height-sized.getHeight ())/2;
        canvas.drawBitmap ( sized,0, drawHeight ,null );
    }
    private void drawCircleMinutes(Canvas canvas) {
        Bitmap clock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock2);
        Bitmap sized = Bitmap.createScaledBitmap ( clock,(int) (width/3.5),(int) (height/3.5),true );
        float drawHeight = (height-sized.getHeight ())/2;
        float drawWidth = (width-sized.getWidth ())/2;
        canvas.drawBitmap ( sized,drawWidth, drawHeight ,null );
        paint.setStyle(Paint.Style.FILL);
        paint.setColor ( Color.RED );
        canvas.drawCircle(width / 2, drawHeight+(sized.getHeight ()/2), 8, paint);
    }
}
