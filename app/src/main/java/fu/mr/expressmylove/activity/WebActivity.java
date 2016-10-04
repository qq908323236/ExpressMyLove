package fu.mr.expressmylove.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.utils.Constans;

public class WebActivity extends AppCompatActivity {

    private WebView wv_web;
    private ImageView iv_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_web);
        wv_web = (WebView) findViewById(R.id.wv_web);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);

    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        String what = getIntent().getStringExtra("what");
        switch (what) {
            case Constans.WHAT_WEB_SERVICE_AGREEMENT:   //打开服务协议
                tv_title.setText("服务协议");
                wv_web.loadUrl(Constans.URL_SERVICE_AGREEMENT);
                break;
        }
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
