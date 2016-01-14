package sg.shun.gao.libs.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import sg.shun.gao.libs.android.common.R;

/**
 * Created by Theodore on 2016/1/13.
 */
public class CaptionedImageView extends FrameLayout implements View.OnLayoutChangeListener {

    private Drawable mDrawable;
    private TextView mTextView;
    private SquareImageView mImageView;
    private int mScrimColor;

    public CaptionedImageView(Context context) {
        super(context);
        init(context);
    }

    public CaptionedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CaptionedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CaptionedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public SquareImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setImage(Drawable drawable) {
        mDrawable = drawable;
        mImageView.setImageDrawable(mDrawable);
        updateBlur();
    }

    private void init(Context context) {
        inflate(context, R.layout.captioned_image_view, this);
        mTextView = (TextView) findViewById(R.id.text);
        mImageView = (SquareImageView) findViewById(R.id.image);
        mScrimColor = getResources().getColor(android.R.color.darker_gray);
        mTextView.addOnLayoutChangeListener(this);
    }

    /**
     * Called when the layout bounds of a view changes due to layout processing.
     *
     * @param v         The view whose bounds have changed.
     * @param left      The new value of the view's left property.
     * @param top       The new value of the view's top property.
     * @param right     The new value of the view's right property.
     * @param bottom    The new value of the view's bottom property.
     * @param oldLeft   The previous value of the view's left property.
     * @param oldTop    The previous value of the view's top property.
     * @param oldRight  The previous value of the view's right property.
     * @param oldBottom The previous value of the view's bottom property.
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (v.getVisibility() != VISIBLE) return;
        final int height = bottom - top;
        final int width = right - left;
        if (height == 0 || width == 0) return;
        updateBlur();
    }

    private void updateBlur() {
        if (!(mDrawable instanceof BitmapDrawable)) return;
        final int textViewHeight = mTextView.getHeight();
        if (textViewHeight == 0) return;
        final float ratio = (float) textViewHeight / mImageView.getHeight();
        final BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
        final Bitmap originalBitmap = bitmapDrawable.getBitmap();
        int height = (int) (ratio * originalBitmap.getHeight());
        final int y = originalBitmap.getHeight() - height;
        final Bitmap portionToBlur = Bitmap.createBitmap(originalBitmap, 0, y, originalBitmap.getWidth(), height);
        final Bitmap blurredBitmap = portionToBlur.copy(Bitmap.Config.ARGB_8888, true);
        RenderScript renderScript = RenderScript.create(getContext());
        ScriptIntrinsicBlur blurIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, portionToBlur);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, blurredBitmap);
        blurIntrinsic.setRadius(25f);
        blurIntrinsic.setInput(tmpIn);
        blurIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(blurredBitmap);
        new Canvas(blurredBitmap).drawColor(mScrimColor);
        final Bitmap newBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(blurredBitmap, 0, y, new Paint());
        mImageView.setImageBitmap(newBitmap);
    }
}
