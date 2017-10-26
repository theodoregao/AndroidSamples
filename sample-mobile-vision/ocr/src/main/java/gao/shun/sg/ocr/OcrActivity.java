package gao.shun.sg.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class OcrActivity extends AppCompatActivity {

    private TextRecognizer textRecognizer;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        textView = (TextView) findViewById(R.id.text);

        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            findViewById(R.id.button).setEnabled(false);
            textView.setText("text recognizer is not available");
        }
    }

    public void onClick(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.text, options);

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> texts = textRecognizer.detect(frame);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < texts.size(); i++)
            if (texts.get(i) != null)
                stringBuilder.append("\n").append(texts.get(i).getValue());
        textView.setText(stringBuilder.toString());
    }
}
