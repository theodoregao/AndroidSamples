package sg.gao.shun.sample.touch;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

public class ScrollableView extends View {

    private static final String TAG = ScrollableView.class.getSimpleName();

    OverScroller overScroller;

    GestureDetectorCompat gestureDetectorCompat;
    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            setX(getX() - distanceX);
            setY(getY() - distanceY);
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v(TAG, "onFling()");
            overScroller.fling((int) getX(), (int) getY(),
                    (int) velocityX, (int) velocityY,
                    0, (int) (getWidth() * 2 - getX()),
                    0, (int) (getHeight() * 2 - getY()),
                    0, 0);
            invalidate();
            return true;
        }
    };

    public ScrollableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        overScroller = new OverScroller(context);

        gestureDetectorCompat = new GestureDetectorCompat(context, simpleOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (overScroller != null && overScroller.computeScrollOffset()) {

            float oldx = getX();
            float oldy = getY();

            float x = overScroller.getCurrX();
            float y = overScroller.getCurrY();

            setX(x);
            setY(y);

            postInvalidate();
        }
    }
}
