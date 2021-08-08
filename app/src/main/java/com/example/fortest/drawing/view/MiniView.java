package com.example.fortest.drawing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.fortest.DisplayParameters;
import com.example.fortest.drawing.Digit;
import com.example.fortest.drawing.Themes;

public class MiniView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int num;
    private String text = "km/h";

    public MiniView(Context context) {
        super(context);
    }

    public MiniView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(int num){
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

        if (num <=0)
            num =-1;

        canvas.save();

        canvas.scale(0.5f * width * reflection, -1f * height);
        canvas.translate(1f * reflection, -1f);

        paint.setColor(Color.parseColor(Themes.mainThemeColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.01f);

        canvas.drawCircle(0, 0, 1, paint);

        canvas.save();
        canvas.translate(0f, -0.4f);
        paint.setStyle(Paint.Style.FILL);

        Digit.drawNumber(canvas, num, paint);
        canvas.restore();

        canvas.save();

        canvas.scale(0.075f, -0.07f);

        paint.setTextSize(3f);
        canvas.drawText(text, -3f, -10f, paint);

        canvas.restore();

        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        float aspect = width / (float) height;
        if (aspect > 2f)
            if (widthMode != MeasureSpec.EXACTLY)
                width = Math.round(2 * height);
        if (aspect < 2f)
            if (heightMode != MeasureSpec.EXACTLY)
                height = Math.round(width / 2f);

        setMeasuredDimension(width, height);
    }
}

