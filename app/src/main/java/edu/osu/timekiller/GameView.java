package edu.osu.timekiller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.os.Handler;

public class GameView extends View {

    int screenWidth, screenHeight, newWidth, newHeight;
    int cloudX = 0, mountainX = 0, grassX = 0;
    int horseX, horseY, horseFrame = 0;
    Bitmap could, mountain, grass;
    Bitmap horse[] = new Bitmap[12]; // 12 frames/ images
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS=30;

    public GameView(Context context) {
        super(context);
        mountain = BitmapFactory.decodeResource(getResources(),R.drawable.background_lite);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        float height = mountain.getHeight();
        float width = mountain.getWidth();
        float ratio = width / height;
        newHeight = screenHeight;
        newWidth = (int) (ratio * screenHeight);
        mountain = Bitmap.createScaledBitmap(mountain,newWidth,newHeight,false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

    }

    @Override
    protected void onDraw(Canvas canvas){
            mountainX -= 100;
            if(mountainX < -newWidth){
                mountainX = 0;
            }
            canvas.drawBitmap(mountain,mountainX,0, null);

            // when image is leaving the right edge of the screen, reinitialize background
            if(mountainX< screenWidth - newWidth){
                canvas.drawBitmap(mountain,mountainX+newWidth,0,null);
            }

    }
}
