package com.android_viewanimator_demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static Button btnNext, btnPrevious, btnDelete;
	private static ViewAnimator viewAnimator;

	private GestureDetector gestureDetector;

	List<View> views;
	int currentIndex;

	Animation inRight, outRight, inLeft, outLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setListeners();

		gestureDetector = new GestureDetector(this, new GestureListener());
	}
	
	void init() {
		views = new ArrayList<>();
		viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		btnNext = (Button) findViewById(R.id.buttonNext);
		btnPrevious = (Button) findViewById(R.id.buttonPrevious);
		btnDelete = (Button) findViewById(R.id.buttonDelete);
		views.add(findViewById(R.id.v1));
		views.add(findViewById(R.id.v2));
		views.add(findViewById(R.id.v3));
		views.add(findViewById(R.id.v4));
		views.add(findViewById(R.id.v5));
		views.add(findViewById(R.id.v6));

		//Load animations for in and out for viewanimator
		final Animation inAnim = AnimationUtils.loadAnimation(this,
				R.anim.in_right);
		final Animation outAnim = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);

		viewAnimator.setInAnimation(inAnim);
		viewAnimator.setOutAnimation(outAnim);

		inRight = AnimationUtils.loadAnimation(this,
				R.anim.in_right);
		outRight = AnimationUtils.loadAnimation(this,
				R.anim.out_right);
		inLeft = AnimationUtils.loadAnimation(this,
				R.anim.in_left);
		outLeft = AnimationUtils.loadAnimation(this,
				R.anim.out_left);
	}

	//Setting listeners to buttons and viewanimator
	void setListeners() {
		btnPrevious.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonNext:
			next();
			break;

		case R.id.buttonPrevious:
			previous();
			break;

		case R.id.buttonDelete:
			removeCurrent();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
					result = true;
				}
				else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						onSwipeBottom();
					} else {
						onSwipeTop();
					}
				}
				result = true;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public void onSwipeRight() {
		previous();
	}

	public void onSwipeLeft() {
		next();
	}

	public void onSwipeTop() {

	}

	public void onSwipeBottom() {

	}

	private void next() {
		if (currentIndex >= views.size() - 1) return;
		currentIndex++;
		viewAnimator.setInAnimation(inRight);
		viewAnimator.setOutAnimation(outLeft);
		viewAnimator.showNext();
	}

	private void previous() {
		if (currentIndex <= 0) return;
		currentIndex--;
		viewAnimator.setInAnimation(inLeft);
		viewAnimator.setOutAnimation(outRight);
		viewAnimator.showPrevious();
	}

	private void removeCurrent() {
		View current = viewAnimator.getCurrentView();
		if (current == views.get(views.size() - 1)) {
			viewAnimator.setInAnimation(inLeft);
			viewAnimator.setOutAnimation(outRight);
			viewAnimator.showPrevious();
		}
		else {
			viewAnimator.setInAnimation(inRight);
			viewAnimator.setOutAnimation(outLeft);
			viewAnimator.showNext();
		}

		//Show next view
		viewAnimator.removeView(current);
		views.remove(current);
		while (currentIndex > 0 && currentIndex >= views.size()) currentIndex--;
		viewAnimator.setDisplayedChild(currentIndex);
		Log.v(TAG, "" + viewAnimator.getCurrentView().getId());
	}

}
