package aero.panasonic.sampleshell;

import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Process;

public class SampleShell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_shell);

        final TextView result = (TextView) findViewById(R.id.result);
        final EditText command = (EditText) findViewById(R.id.command);
        final EditText param = (EditText) findViewById(R.id.param);

        final Button buttonExec = (Button) findViewById(R.id.button_exec);
        buttonExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (command.getText() == null || TextUtils.isEmpty(command.getText().toString())) return;
                    Process process = Runtime.getRuntime().exec(command.getText().toString(), (param.getText()==null || TextUtils.isEmpty(param.getText().toString()) ? null : new String[] {param.getText().toString()}));
//                    Process process = Runtime.getRuntime().exec(command.getText().toString());
//                    Runtime.getRuntime().ex
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    result.setText("");
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) result.append("\n" + line);
                    while ((line = errorBufferReader.readLine()) != null) result.append("\n" + line);
                } catch (IOException e) {
                    e.printStackTrace();
                    result.setText("error! " + e.getLocalizedMessage());
                }
            }
        });
    }
}
