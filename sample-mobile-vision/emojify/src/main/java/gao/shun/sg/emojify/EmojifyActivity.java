package gao.shun.sg.emojify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public class EmojifyActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "gao.shun.sg.emojify.fileprovider";

    private ImageView mImageView;

    private Button mEmojifyButton;
    private FloatingActionButton mShareFab;
    private FloatingActionButton mSaveFab;
    private FloatingActionButton mClearFab;

    private TextView mTitleTextView;
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emojify);

        Timber.plant(new Timber.DebugTree());

        mImageView = (ImageView) findViewById(R.id.image_view);
        mEmojifyButton = (Button) findViewById(R.id.emojify_button);
        mShareFab = (FloatingActionButton) findViewById(R.id.share_button);
        mSaveFab = (FloatingActionButton) findViewById(R.id.save_button);
        mClearFab = (FloatingActionButton) findViewById(R.id.clear_button);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
    }

    public void emojifyMe(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    launchCamera();
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                BitmapUtils.rotate(mTempPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            processAndSetImage();
        } else {
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void processAndSetImage() {
        mEmojifyButton.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.VISIBLE);
        mClearFab.setVisibility(View.VISIBLE);

        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        mResultsBitmap = Emojifier.detectFaces(this, mResultsBitmap);

        mImageView.setImageBitmap(mResultsBitmap);
    }

    public void saveMe(View view) {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
    }

    public void shareMe(View view) {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
        BitmapUtils.shareImage(this, mTempPhotoPath);
    }

    public void clearImage(View view) {
        mImageView.setImageResource(0);
        mEmojifyButton.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mSaveFab.setVisibility(View.GONE);
        mShareFab.setVisibility(View.GONE);
        mClearFab.setVisibility(View.GONE);

        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
    }
}
