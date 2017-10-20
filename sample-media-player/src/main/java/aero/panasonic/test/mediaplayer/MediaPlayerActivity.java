package aero.panasonic.test.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements
        SurfaceHolder.Callback,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = MediaPlayerActivity.class.getSimpleName();

    private static final String DEFAULT_MEDIA_URI = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private EditText url;
    private TextView log;
    private Button load;

    private String uri;
    private int width, height;

    private boolean isPrepared;
    private boolean isVideoSizeReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Log.v(TAG, "onCreate()");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceView.setZOrderMediaOverlay(true);
        surfaceView.setZOrderOnTop(true);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        url = (EditText) findViewById(R.id.url);
        log = (TextView) findViewById(R.id.log);

        log("surfaceHolder initializing.");

        load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            play();
                        } catch (IOException e) {
                            log("play with exception, please kill the app and restart");
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        load.setEnabled(false);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void cleanup() {
        isVideoSizeReady = false;
        isPrepared = false;
        width = 0;
        height = 0;

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceCreated");
        load.setEnabled(true);
        log("surfaceCreated. Please input the media uri in the following edit text");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.v(TAG, "onBufferingUpdate: " + i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.v(TAG, "onCompletion");
        cleanup();
        log("play complemented, try another uri");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.v(TAG, "onPrepared");
        isPrepared = true;
        if (isVideoSizeReady) startPlay();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged");
        this.width = width;
        this.height = height;
        isVideoSizeReady = true;
        if (isPrepared) startPlay();
    }

    private void log(final String message) {
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                log.setText(message);
            }
        });
    }

    private void play() throws IOException {
        cleanup();
        mediaPlayer = new MediaPlayer();
        uri = url.getText().toString();
        uri = uri == null || uri.length() == 0 ? DEFAULT_MEDIA_URI : uri;
        log("try loading: " + uri);
        mediaPlayer.setDataSource(uri);
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        log("start loading: " + uri);
    }

    private void startPlay() {
        Log.v(TAG, "startPlay");
        surfaceHolder.setFixedSize(width, height);
        mediaPlayer.start();
        log("start playing: " + uri);
    }
}
