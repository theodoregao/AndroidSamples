package sg.shun.gao.sample.renderscript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v8.renderscript.*;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

public class RenderScriptSample extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = RenderScriptSample.class.getSimpleName();

    private SeekBar seekBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_script_sample);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        imageView = (ImageView) findViewById(R.id.image);

        seekBar.setOnSeekBarChangeListener(this);
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.v(TAG, "onProgressChanged() " + progress);

        new AsyncTask<Integer, Void, Bitmap>() {
            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected Bitmap doInBackground(Integer... params) {
                return blurImage(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }.execute(progress);
    }

    private Bitmap blurImage(int progress) {
        RenderScript renderScript = RenderScript.create(this);
        Bitmap inBitmap = loadBitmap(R.drawable.tree);
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        Allocation inAllocation = Allocation.createFromBitmap(renderScript, inBitmap);
        Allocation outAllocation = Allocation.createCubemapFromBitmap(renderScript, outBitmap);

        blurScript.setRadius(progress);
        blurScript.setInput(inAllocation);
        blurScript.forEach(outAllocation);

        outAllocation.copyTo(outBitmap);
        inBitmap.recycle();
        renderScript.destroy();

        return outBitmap;
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private Bitmap loadBitmap(int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resource, options);
    }
}
