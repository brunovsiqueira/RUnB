package br.unb.runb.screens.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import br.unb.runb.R;

public class NewsActivity extends AppCompatActivity {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbarTitle = findViewById(R.id.toolbar_container_title);
        toolbarTitle.setText("Notícias do RU");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://ru.unb.br/");
    }
}
