package com.javy.fave;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.javy.fave.util.EncryptedPrefsUtil;

public class WebsiteActivity extends AppCompatActivity {

    private WebView webView;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_website);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Website");

        username = EncryptedPrefsUtil.getString("username", "username-not-found");

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient()); // Load inside app
        webView.loadUrl("https://"+username+".av.ke");
        System.out.println("https://"+username+".av.ke");



    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            if (webView.canGoBack()) {
                webView.goBack();
            }
            return true;
        }
        else if (item.getItemId() == R.id.action_share) {
            String currentUrl = webView.getUrl();

            // Share the URL via an Intent
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out!");
            intent.putExtra(Intent.EXTRA_TEXT, currentUrl);
            startActivity(Intent.createChooser(intent, "Share via"));
            return true;
        }

//        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();

//                Intent intent = new Intent(ViewProductActivity.this, ProductsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                return true;
        }

//        return super.onOptionsItemSelected(item);
    }

