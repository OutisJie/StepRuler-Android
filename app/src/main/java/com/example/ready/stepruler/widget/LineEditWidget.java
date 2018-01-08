package com.example.ready.stepruler.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ready on 2017/12/23.
 */

@SuppressLint("AppCompatCustomView")
public class LineEditWidget extends EditText {
    private Paint mPaint;

    public LineEditWidget(Context context) {
        super(context);
        initPaint();
    }

    public LineEditWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public LineEditWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.LTGRAY);
        PathEffect effect = new DashPathEffect(new float[]{5, 5, 5, 5},5);
        mPaint.setPathEffect(effect);

        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int spacingHeight = (int) getLineSpacingExtra();

        int count = (height - paddingBottom - paddingTop)/lineHeight;
        for(int i = 0; i < count; i ++){
            int baseLine = lineHeight * (i + 1) + paddingTop - spacingHeight/2;
            canvas.drawLine(paddingLeft, (int)(baseLine * 1.0), right - paddingRight * (int)1.8, (int)(baseLine * 1.0), mPaint  );
        }
        super.onDraw(canvas);
    }
}
