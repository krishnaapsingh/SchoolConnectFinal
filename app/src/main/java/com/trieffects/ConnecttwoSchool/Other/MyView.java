package com.trieffects.ConnecttwoSchool.Other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Trieffects on 28-Oct-17.
 */

public class MyView extends View {

    String text;
    public MyView(Context context,String txt) {
        super(context);
        text=txt;

    }

    @Override

    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub

        super.onDraw(canvas);



        float width = (float)getWidth();

        float height = (float)getHeight();

        float radius;



        if (width > height){

            radius = height/4;

        }else{

            radius = width/4;

        }



        Paint paint = new Paint();

        paint.setColor(Color.WHITE);

        paint.setStrokeWidth(5);

        paint.setStyle(Paint.Style.STROKE);



        Path path = new Path();



        float center_x, center_y;

        center_x = width/2;

        center_y = height/2;

        final RectF oval = new RectF();

        oval.set((center_x - radius)/2,

                (center_y -radius),

                (center_x + radius),

                (center_y + radius)*2);

        path.addArc(oval, -170, 180);

      /*  paint.setColor(Color.WHITE);

        paint.setStrokeWidth(1);

        paint.setStyle(Paint.Style.STROKE);

        paint.setTextSize(37);



        canvas.drawTextOnPath(text, path, 0, 0, paint);


*/
        paint.setTextSize(60);
        paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.FILL);

        canvas.drawTextOnPath(text,

                path, 10, 10,

                paint);

    }

}
