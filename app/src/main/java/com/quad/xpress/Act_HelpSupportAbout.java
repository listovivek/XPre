package com.quad.xpress;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by kural on 2/21/2017.
 */

public class Act_HelpSupportAbout extends Activity {

    WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView tv_title = (TextView) findViewById(R.id.tb_normal_title);

        mWebView = (WebView) findViewById(R.id.html_content);
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);


        String ItemParent = "help";
        Intent getItemParent = getIntent();
        ItemParent = getItemParent.getStringExtra("ItemParent");

        if(ItemParent.equalsIgnoreCase("help")){
            tv_title.setText("Help");
            init("help.html");
        }else if (ItemParent.equalsIgnoreCase("About")){
            tv_title.setText("About");
            init("about.html");
        }
        else if(ItemParent.equalsIgnoreCase("support")){
            tv_title.setText("Support");
            init("support.html");
        }


        ImageButton btn_back = (ImageButton) findViewById(R.id.tb_normal_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init(String s) {

        AssetManager mgr = getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(s, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = StreamToString(in);
            in.close();
            mWebView.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
