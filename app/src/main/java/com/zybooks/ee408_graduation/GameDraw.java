package com.zybooks.ee408_graduation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

public class GameDraw extends View {
    private int[] background = {R.drawable.bg};
    private Bitmap[] bgMap;
    private Paint paintBack;
    private Rect backRect;
    private int scale;

    public GameDraw(Context context) {
        super(context);

        bgMap = new Bitmap[background.length];
        for (int i = 0; i < bgMap   .length; i++)
            bgMap[i] = BitmapFactory.decodeResource(getResources(), background[i]);

        paintBack = new Paint();
        paintBack.setColor(0xFF1E0056);

        DisplayMetrics metrics = new DisplayMetrics();
        //getWindow().getDefaultDisplay().getMetrics(metrics);
        scale = metrics.densityDpi;
        backRect = new Rect(0-20*scale, 0-20*scale, 700+50*scale, 1000);


    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(bgMap[0], null, backRect, paintBack);
    }
}
