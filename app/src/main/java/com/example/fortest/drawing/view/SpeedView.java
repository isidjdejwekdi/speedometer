package com.example.fortest.drawing.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.example.fortest.DisplayParameters;
import com.example.fortest.drawing.Digit;
import com.example.fortest.drawing.Themes;

public class SpeedView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int num;
    private int prevNum;
    private String text = "km/h";

    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        float aspect = width / height;
        if (aspect > 2f)
            width = 2 * height;
        if (aspect < 2f)
            height = width / 2f;

        float reflection = 1f;
        if (DisplayParameters.displayHud)
            reflection = -1f;

        paint.setColor(Color.parseColor(Themes.elementsColorOrange));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.01f);

        if (DisplayParameters.displayAnalog){
            int maxValue = 260;
            this.num = Math.min(num, maxValue);
            canvas.save();

            canvas.scale(0.5f * width * reflection, -1f * height);
            canvas.translate(1f * reflection, -1f);
            canvas.drawCircle(0, 0, 1, paint);
            paint.setStyle(Paint.Style.FILL);

            float scale = 0.9f;
            float longScale = 0.9f;
            float textPadding = 0.85f;

            double step = Math.PI / maxValue;
            int markRange = 20;
            for (int i = 0; i <= maxValue; i+= markRange /2) {
                float x1 = (float) Math.cos(Math.PI - step*i);
                float y1 = (float) Math.sin(Math.PI - step*i);
                float x2;
                float y2;
                if (i % markRange == 0) {
                    x2 = x1 * scale * longScale;
                    y2 = y1 * scale * longScale;
                } else {
                    x2 = x1 * scale;
                    y2 = y1 * scale;
                }
                canvas.drawLine(x1, y1, x2, y2, paint);
            }
            canvas.restore();

            canvas.save();
            canvas.scale(reflection, 1f);
            canvas.translate(width / 2 * reflection, 0);

            paint.setTextSize(height / 12);

            float factor = height * scale * longScale * textPadding;

            for (int i = 0; i <= maxValue; i+= markRange) {
                float x = (float) Math.cos(Math.PI - step*i) * factor;
                float y = (float) Math.sin(Math.PI - step*i) * factor;
                int textLen = Math.round(paint.measureText(text));
                canvas.drawText(Integer.toString(i), x - textLen / 2f, height - y, paint);
            }

            canvas.restore();

            canvas.save();
            canvas.scale(0.5f * width * reflection, -1f * height);
            canvas.translate(1f * reflection, -1f);
            canvas.rotate( -180 * (num / (float) maxValue));//*reflection

            paint.setColor(Color.parseColor(Themes.elementsColorRed));
            paint.setStrokeWidth(0.025f);
            canvas.drawLine(0.01f, 0f, -1f, 0f, paint);//*reflection
            canvas.drawLine(-0.01f, 0f, -1f, 0f, paint);//*reflection
            canvas.drawCircle(0f,0f,0.05f, paint);

            canvas.restore();

            canvas.save();
            canvas.scale(0.5f * width * reflection, -1f * height);
            canvas.translate(1f * reflection, -1f);
            canvas.scale(0.075f, -0.07f);

            paint.setColor(Color.parseColor(Themes.elementsColorOrange));
            paint.setTextSize(2f);
            canvas.drawText(text, -3f, -1.5f, paint);
            canvas.restore();

            setValueAnimated(num);


        } else {
            canvas.scale(0.5f * width * reflection, -1f * height);
            canvas.translate(1f * reflection, -1f);
            canvas.drawCircle(0, 0, 1, paint);

            paint.setStyle(Paint.Style.FILL);
            Digit.drawNumber(canvas, num, paint);

            canvas.save();
            canvas.scale(0.075f, -0.07f);

            paint.setTextSize(2f);
            canvas.drawText(text, -3f, -12f, paint);

            canvas.restore();
        }

        if (prevNum != num)
            prevNum = num;
    }

    ObjectAnimator objectAnimator;
    public void setValueAnimated(int num) {
        if(objectAnimator != null)
            objectAnimator.cancel();
     objectAnimator = ObjectAnimator.ofInt(this, "num", prevNum, num);
     objectAnimator.setDuration(100 + Math.abs(prevNum - num) * 5);
     objectAnimator.setInterpolator(new DecelerateInterpolator());
     objectAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        float aspect = width / (float) height;
        if (aspect > 2f)
            if (widthMode != MeasureSpec.EXACTLY)
                width = Math.round(2f * height);
        if (aspect < 2f)
            if (heightMode != MeasureSpec.EXACTLY)
                height = Math.round(width / 2f);

        setMeasuredDimension(width, height);
    }
}

