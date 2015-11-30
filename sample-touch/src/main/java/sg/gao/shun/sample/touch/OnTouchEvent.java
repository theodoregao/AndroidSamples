package sg.gao.shun.sample.touch;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OnTouchEvent extends AppCompatActivity {

    private static final String TAG = OnTouchEvent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_touch_event);

        View view = findViewById(R.id.view);
        view.setOnTouchListener(onTouchListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);

            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    Log.d(TAG, "View was DOWN");
                    return true;
                case (MotionEvent.ACTION_MOVE) :
                    Log.d(TAG,"View was MOVE");
                    return true;
                case (MotionEvent.ACTION_UP) :
                    Log.d(TAG,"View was UP");
                    return true;
                case (MotionEvent.ACTION_CANCEL) :
                    Log.d(TAG,"View was CANCEL");
                    return true;
                case (MotionEvent.ACTION_OUTSIDE) :
                    Log.d(TAG,"Movement occurred outside bounds " +
                            "of current screen element");
                    return true;
                default :
                    return false;
            }
        }
    };
}
