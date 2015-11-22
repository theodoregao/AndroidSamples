package sg.shun.gao.sample.fileprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sg.shun.gao.libs.android.common.adapter.FileAdapter;

public class FileSelectActivity extends AppCompatActivity implements FileAdapter.OnItemClickListener {

    public static final String FILE_PROVIDER_AUTHORITY = "sg.shun.gao.sample.fileprovider";
    private static final String TAG = FileSelectActivity.class.getSimpleName();
    FileAdapter fileAdapter;
    File[] mImageFiles;
    File[] mTextFiles;
    String[] mImageFileNames;
    String[] mTextFileNames;
    private Intent mResultIntent;
    private File mPrivateRootDir;
    private File mTextDir;
    private File mImagesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mTextDir = new File(mPrivateRootDir, "text");

        mImageFiles = mImagesDir.listFiles();
        mTextFiles = mTextDir.listFiles();

        fileAdapter = new FileAdapter(this);
        fileAdapter.setOnItemClickListener(this);
        RecyclerView listFiles = (RecyclerView) findViewById(R.id.listFiles);
        listFiles.setLayoutManager(new LinearLayoutManager(this));
        listFiles.setAdapter(fileAdapter);

        generateTextFiles();
    }

    private void generateTextFiles() {
        if (!mTextDir.exists()) mTextDir.mkdir();
        for (int i = 0; i < 5; i++) {
            File file = new File(mTextDir, "file" + i);
            generateTextFile(file, "contents " + i);
            fileAdapter.add(file);
        }
    }

    private void generateTextFile(File file, String contents) {
        try {
            if (!file.exists()) file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(contents.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, File file) {
        mResultIntent = new Intent("sg.shun.gao.sample.fileprovider.ACTION_RETURN_FILE");

        Uri fileUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, file);

        if (fileUri != null) {
            mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
            setResult(RESULT_OK, mResultIntent);
        } else {
            mResultIntent.setDataAndType(null, "");
            setResult(RESULT_CANCELED, mResultIntent);
        }

        finish();
    }
}
