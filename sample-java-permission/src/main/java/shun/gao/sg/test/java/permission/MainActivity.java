package shun.gao.sg.test.java.permission;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!directory.exists()) directory.mkdir();
        Log.v(TAG, directory.toString() + ": " + directory.exists());

        File file = new File(directory, "permission.policy");

        if (!file.exists()) {
            Log.e(TAG, "file not exist");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String content = "";
            String line;
            while ((line = br.readLine()) != null) content += line;
            Log.v(TAG, "content: " + content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("java.security.policy", file.toString());

//        SecurityManager securityManager = new SecurityManager();
//
//        System.setSecurityManager(securityManager);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_check:
                check();
                break;

            default:
                break;
        }
    }

    private void check() {
        String text = ((TextView) findViewById(R.id.text)).getText().toString();
        WordCheckPermission wordCheckPermission = new WordCheckPermission(text, "insert");
        SecurityManager manager = System.getSecurityManager();
        if (manager != null) {
            manager.checkPermission(wordCheckPermission);
            Log.v(TAG, "checked");
        }
        else Log.e(TAG, "security manager is null");
    }
}
