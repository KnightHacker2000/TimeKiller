package edu.osu.timekiller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TouchActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private static final String DEBUG_TAG = "Gestures";
    private ImageView image;
    private ViewGroup mainLayout;
    private GestureDetectorCompat detectMe;
    private SpringAnimation springAnim;
    private float original_y;
    private float dy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main);
        image = (ImageView) findViewById(R.id.mainCharacter);
        detectMe = new GestureDetectorCompat(this, this);
        detectMe.setOnDoubleTapListener(this);
        original_y = image.getY();
    }

    public void SpringAnimationY() {
        springAnim = new SpringAnimation(image, DynamicAnimation.TRANSLATION_Y, 0);
        springAnim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        springAnim.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        //springAnim.getSpring().setFinalPosition(-500f);
        float pixelPerSecond = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2000f, getResources().getDisplayMetrics());
        springAnim.setStartVelocity(-pixelPerSecond);
        springAnim.setStartValue(original_y);

        springAnim.start();
//    springAnim.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.detectMe.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.i(DEBUG_TAG, "onDown: " + image.getY());
        dy = original_y - event.getRawY();
        SpringAnimationY();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.i(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.i(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.i(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //springAnim.cancel();
        Log.i(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //springAnim.cancel();
        Log.i(DEBUG_TAG, "onSingleTapUp: " + e.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.i(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.i(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //springAnim.cancel();
        Log.d(DEBUG_TAG, "onConfirmed" + Float.toString(image.getY()));
        return true;
    }
}