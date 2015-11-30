package sg.gao.shun.sample.touch;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

public class GestureDetector extends AppCompatActivity
        implements android.view.GestureDetector.OnGestureListener, android.view.GestureDetector.OnDoubleTapListener {
    // if only want to capture subset of the events, use GestureDetector.SimpleOnGestureListener to instead

    private static final String TAG = GestureDetector.class.getSimpleName();

    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);

        gestureDetectorCompat = new GestureDetectorCompat(this, this);
        gestureDetectorCompat.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent() " + event.getHistorySize());
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.v(TAG, "onSingleTapConfirmed()");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.v(TAG, "onDoubleTap()");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.v(TAG, "onDoubleTapEvent()");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.v(TAG, "onDown()");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.v(TAG, "onShowPress()");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.v(TAG, "onSingleTapUp()");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.v(TAG, "onScroll() dx=" + distanceX + ", dy=" + distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.v(TAG, "onLongPress()");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.v(TAG, "onFling() vx=" + velocityX + ", vy=" + velocityY);
        return true;
    }
}
