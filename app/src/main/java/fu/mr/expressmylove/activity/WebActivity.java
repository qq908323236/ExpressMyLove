package fu.mr.expressmylove.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fu.mr.expressmylove.R;

public class WebActivity extends AppCompatActivity {

    private WebView wv_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        wv_web = (WebView) findViewById(R.id.wv_web);
        wv_web.loadUrl("http://www.qlgbbq.cn/expressmylove/");
        wv_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
