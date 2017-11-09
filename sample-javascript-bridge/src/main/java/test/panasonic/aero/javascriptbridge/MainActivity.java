package test.panasonic.aero.javascriptbridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setSupportMultipleWindows(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(this, "native");
        mWebView.loadUrl("file:///android_asset/index.html");
    }

    public void onClick(View view) {
        callJs("message from native");
    }

    @JavascriptInterface
    public void log(String log) {
        Log.v(TAG, log);
    }

    private void callJs(String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ValueCallback<String> value = new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.v(TAG, "onReceiveValue() " + s);
                }
            };
            mWebView.evaluateJavascript("fromNative(\"" + message + "\");", value);
        } else {
            mWebView.loadUrl("javascript:fromNative(" + message + ");");
        }
    }
}
