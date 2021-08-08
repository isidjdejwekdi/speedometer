package com.example.fortest.drawing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.fortest.DisplayParameters;
import com.example.fortest.drawing.Themes;

public class WeatherView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String description;
    private long temp;

    public WeatherView(Context context) {
        super(context);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setWeather(long temp, String description) {
        this.temp = temp;
        this.description = description;
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
        if (DisplayParameters.displayHud){
            reflection = -1f;
        }

        //description = "clouds";
//        String text1 = "17C";
        //temp = 21;

        paint.setColor(Color.parseColor(Themes.elementsColorWhite));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.1f);

        canvas.save();
        canvas.scale(width * 0.01f * reflection, 0.02f * height);
        canvas.translate(100f * reflection, 9f);

        if(temp != 0 && description != null) {

            float textSize = width/30f;
            float y0 = 10f;
            paint.setTextSize(textSize);
            float wLetters = 8f;
            float dx0 = -(description.length() / 2f) * wLetters;
            canvas.drawText(description, dx0, y0, paint);

            String t = temp + " °C";
            dx0 = -(t.length() / 2f) * wLetters;
            canvas.drawText(temp + "°C", dx0, textSize + y0 + 5f, paint);
        }
        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //float normal = 4f;
        float aspect = width / (float) height;
        float normalAspect = 4f;
        if (aspect > normalAspect)
            if (widthMode != MeasureSpec.EXACTLY)
                width = Math.round(normalAspect * height);
        if (aspect < 4f)
            if (heightMode != MeasureSpec.EXACTLY)
                height =  Math.round(width / normalAspect);

        setMeasuredDimension(width, height);
    }

}
