package shun.gao.plugin.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Theodore on 2016/5/5.
 */
public class CustomButton extends Button {

    private String mAttrsIntent;
    private String mAttrsParams;

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
        init();
    }

    @SuppressLint("NewApi")
    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupAttributes(attrs);
        init();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText("CB:" + text, type);
    }

    private void setupAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);

        try {
            mAttrsIntent = typedArray.getString(R.styleable.CustomButton_intent);
            mAttrsParams = typedArray.getString(R.styleable.CustomButton_params);
        } finally {
            typedArray.recycle();
        }
    }

    private void init() {
        if (!TextUtils.isEmpty(mAttrsIntent)) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(mAttrsIntent);
                    intent.putExtra(getResources().getString(R.string.EXTRA_CRITICAL_EVENT), mAttrsParams);
//                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                    getContext().sendBroadcast(intent);
                }
            });
        }
    }
}
