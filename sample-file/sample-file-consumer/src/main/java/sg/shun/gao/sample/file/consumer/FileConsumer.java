package sg.shun.gao.sample.file.consumer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileConsumer extends AppCompatActivity {

    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_consumer);

        textViewResult = (TextView) findViewById(R.id.textResult);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRequestFile:
                requestFile();
                break;

            default:
                break;
        }
    }

    private void requestFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("text/plain");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) return;

        Uri fileUri = intent.getData();

//        try {
//            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(fileUri, "r");
//            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//            textViewResult.setText(fileDescriptor.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
//        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//        cursor.moveToFirst();
//        String name = cursor.getString(nameIndex);
//        long size = cursor.getLong(sizeIndex);
//        cursor.close();
//        textViewResult.setText(name + " : " + size);

        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            byte[] buffer = new byte[1024];
            int size = inputStream.read(buffer);
            String string = new String(buffer, 0, size);
            textViewResult.setText(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
