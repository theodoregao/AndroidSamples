package gao.shun.sg.barcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarcodeActivity extends AppCompatActivity {

    private BarcodeDetector detector;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        textView = (TextView) findViewById(R.id.txtContent);

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        if(!detector.isOperational()){
            textView.setText("Could not set up the detector!");
            findViewById(R.id.button).setEnabled(false);
            return;
        }
    }

    public void onClick(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.puppy);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        textView.setText(barcodes.valueAt(0).rawValue);
    }
}
