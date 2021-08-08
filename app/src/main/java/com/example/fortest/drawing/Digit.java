package com.example.fortest.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

public class Digit {

    private static ArrayList<Path> getPolygonsToDraw(int num, float x01){

        Path polygon1 = new Path();
        polygon1.moveTo(x01, 0.7f);
        polygon1.lineTo(x01, 0.4f);
        polygon1.lineTo(x01+0.05f, 0.45f);
        polygon1.lineTo(x01 + 0.05f, 0.65f);
        polygon1.lineTo(x01, 0.7f);

        Path polygon2 = new Path();
        polygon2.moveTo(x01, 0.7f);
        polygon2.lineTo(x01 + 0.3f, 0.7f);
        polygon2.lineTo(x01+0.25f, 0.65f);
        polygon2.lineTo(x01 + 0.05f, 0.65f);
        polygon2.lineTo(x01, 0.7f);

        Path polygon3 = new Path();
        polygon3.moveTo(x01 + 0.25f, 0.65f);
        polygon3.lineTo(x01 + 0.3f, 0.7f);
        polygon3.lineTo(x01 + 0.3f, 0.4f);
        polygon3.lineTo(x01 + 0.25f, 0.45f);
        polygon3.lineTo(x01 + 0.25f, 0.65f);

        Path polygon4 = new Path();
        polygon4.moveTo(x01 + 0.25f, 0.35f);
        polygon4.lineTo(x01 + 0.3f, 0.4f);
        polygon4.lineTo(x01 + 0.3f, 0.1f);
        polygon4.lineTo(x01 + 0.25f, 0.15f);
        polygon4.lineTo(x01 + 0.25f, 0.35f);

        Path polygon5 = new Path();
        polygon5.moveTo(x01, 0.1f);
        polygon5.lineTo(x01 + 0.3f, 0.1f);
        polygon5.lineTo(x01+0.25f, 0.15f);
        polygon5.lineTo(x01 + 0.05f, 0.15f);
        polygon5.lineTo(x01, 0.1f);

        Path polygon6 = new Path();
        polygon6.moveTo(x01, 0.4f);
        polygon6.lineTo(x01, 0.1f);
        polygon6.lineTo(x01 + 0.05f, 0.15f);
        polygon6.lineTo(x01 + 0.05f, 0.35f);
        polygon6.lineTo(x01, 0.4f);

        Path polygon7 = new Path();
        polygon7.moveTo(x01 + 0.025f, 0.4f);
        polygon7.lineTo(x01 + 0.05f, 0.425f);
        polygon7.lineTo(x01 + 0.25f, 0.425f);
        polygon7.lineTo(x01 + 0.275f, 0.4f);
        polygon7.lineTo(x01 + 0.25f, 0.375f);
        polygon7.lineTo(x01 + 0.05f, 0.375f);
        polygon7.lineTo(x01 + 0.025f, 0.4f);



        ArrayList<Path> polygonsList = new ArrayList<>();

        switch (num){
            case 0:
                polygonsList.add(polygon1);
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon6);
                return polygonsList;

            case 1:
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                return polygonsList;

            case 2:
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon5);
                polygonsList.add(polygon6);
                polygonsList.add(polygon7);
                return polygonsList;

            case 3:
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon7);
                return polygonsList;

            case 4:
                polygonsList.add(polygon1);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                polygonsList.add(polygon7);
                return polygonsList;

            case 5:
                polygonsList.add(polygon1);
                polygonsList.add(polygon2);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon7);
                return polygonsList;

            case 6:
                polygonsList.add(polygon1);
                polygonsList.add(polygon2);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon7);
                polygonsList.add(polygon6);
                return polygonsList;

            case 7:
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                return polygonsList;

            case 8:
                polygonsList.add(polygon1);
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon6);
                polygonsList.add(polygon7);
                return polygonsList;

            case 9:
                polygonsList.add(polygon1);
                polygonsList.add(polygon2);
                polygonsList.add(polygon3);
                polygonsList.add(polygon4);
                polygonsList.add(polygon5);
                polygonsList.add(polygon7);
                return polygonsList;

            case -1:
                polygonsList.add(polygon7);
                return polygonsList;
        }
        return null;
    }

    public static void drawNumber(Canvas canvas, int num, Paint paint){
        int buf = num;
        int n = 0;

        float [][] dx = {{-0.15f},
                         {-0.4f, 0.1f},
                         {-0.65f, -0.15f, 0.35f}};

        ArrayList<Path> polygons;

        if (num == 1)
            dx[0][0] = -0.3f;

        polygons = getPolygonsToDraw(num, dx[0][0]);
            if (polygons != null)
                for (Path pol : polygons)
                    canvas.drawPath(pol, paint);

        while (buf > 0) {
            buf /= 10;
            n++;
        }

        if (n > 3)
            return;

        for (int i=0; i<n; i++) {
            buf = num % 10;
            polygons = getPolygonsToDraw(buf, dx[n - 1][n - 1 - i]);
            if (polygons != null)
                for (Path pol : polygons)
                    canvas.drawPath(pol, paint);

            num /= 10;
        }
    }

    public static void drawDistNumber(Canvas canvas, float num, Paint paint){
        float dx =-1f;

        int integerPart = (int) num;
        float fractionalPart = num%1;

        String s = Integer.toString(integerPart);
        ArrayList<Path> polygons;

        int[] iArr = new int[s.length()];

        for (int i = s.length() - 1; i >= 0; i--) {
            iArr[i] = integerPart % 10;
            integerPart /= 10;
        }

        for (int i=0; i<iArr.length; i++) {
            if (i>0)
                dx+=0.4f;
            polygons = getPolygonsToDraw(iArr[i], dx);
            if (polygons != null)
                for (Path pol : polygons)
                    canvas.drawPath(pol, paint);
        }

        dx+=0.4f;
        canvas.drawRect(dx, 0.15f, dx+0.05f, 0.1f, paint);
        dx+=0.2f;

        for (int i=0; i <2; i++){
            fractionalPart*=10;
            int f = (int)fractionalPart;
            fractionalPart%=1;

            polygons = getPolygonsToDraw(f, dx);
            if (polygons != null)
                for (Path pol : polygons)
                    canvas.drawPath(pol, paint);
            dx+=0.4f;
        }

    }
}

