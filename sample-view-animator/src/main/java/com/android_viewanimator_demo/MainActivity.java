package com.android_viewanimator_demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

	List<View> views;
	int currentIndex;

	Animation inRight, outRight, inLeft, outLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setListeners();
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
		View current = viewAnimator.getCurrentView();
		switch (v.getId()) {
		case R.id.buttonNext:
			if (currentIndex >= views.size() - 1) break;
			currentIndex++;
			viewAnimator.setInAnimation(inRight);
			viewAnimator.setOutAnimation(outLeft);
			
			//Show next view
			viewAnimator.showNext();
//			viewAnimator.removeView(current);
			break;

		case R.id.buttonPrevious:
			if (currentIndex <= 0) break;
			currentIndex--;
			viewAnimator.setInAnimation(inLeft);
			viewAnimator.setOutAnimation(outRight);

			//Show next view
			viewAnimator.showPrevious();
//			viewAnimator.removeView(current);
			break;

			case R.id.buttonDelete:
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
}
