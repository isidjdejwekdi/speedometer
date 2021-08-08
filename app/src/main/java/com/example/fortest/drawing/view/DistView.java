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

public class DistView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float num;
    private String text = "km";

    public DistView(Context context) {
        super(context);
    }

    public DistView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DistView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(float num) {

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

        float dx =0;
        float reflection = 1f;
        if (DisplayParameters.displayHud){
            reflection = -1f;
            dx = -1.5f;
        }

        canvas.save();

        canvas.scale(0.5f * width * reflection, -1f * height);
        canvas.translate(1f * reflection + dx, -1f);

        paint.setColor(Color.parseColor(Themes.elementsColorOrange));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.01f);

        Digit.drawDistNumber(canvas, num, paint);

        canvas.save();

        canvas.scale(0.12f, -0.1f);

        paint.setTextSize(1.2f);
        canvas.drawText(text, -8f, 0f, paint);

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
                width = Math.round(getWidth());
        if (aspect < 2f)
            if (heightMode != MeasureSpec.EXACTLY)
                height = Math.round(width / 2f);

        setMeasuredDimension(width, height);
    }
}

