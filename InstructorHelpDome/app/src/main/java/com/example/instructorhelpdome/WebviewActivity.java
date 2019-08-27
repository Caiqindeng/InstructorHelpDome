package com.example.instructorhelpdome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener{
    TextView title;
    private WebView webView;
    private ImageButton back_btn;
    private Button btn_top;
    private Button btn_refresh;
    private ImageButton zou;
    private ImageButton you;
    String name;
    String url;
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        selectUrl();
        init();
    }
    private void selectUrl() {
        Intent i=getIntent();
        Bundle b=new Bundle();
        b=i.getBundleExtra("data");
        url=b.getString("url");
        name=b.getString("name");
        Toast.makeText(WebviewActivity.this, name, Toast.LENGTH_SHORT).show();
    }


    private void init() {

        webView = (WebView) findViewById(R.id.webView);
        // 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
        WebSettings webSettings = webView.getSettings();
        //声明WebSettings子类
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        // 其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存模式
        // 开启DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启database storage API功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        webSettings.setAppCachePath(cacheDirPath);
        webSettings.setAppCacheEnabled(true);
        Log.i("databasepath", webSettings.getDatabasePath());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);

        title=(TextView)findViewById(R.id.title_text);
        title.setText(name);

        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        zou=(ImageButton)findViewById(R.id.zuo);
        you=(ImageButton)findViewById(R.id.you);

        zou.setOnClickListener(this);
        you.setOnClickListener(this);

        btn_top = (Button) findViewById(R.id.btn_top);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);
        btn_top.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.zuo:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    Toast.makeText(WebviewActivity.this, "已经是第一页！",  Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.you:
                if (webView.canGoForward()) {
                    webView.goForward();
                } else {
                    Toast.makeText(WebviewActivity.this, "已经是最后一页！", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.btn_refresh:
                webView.reload();    //刷新当前页面
                break;
            case R.id.btn_top:
                webView.setScrollY(0);   //滚动到顶部
                break;
        }

    }
}
