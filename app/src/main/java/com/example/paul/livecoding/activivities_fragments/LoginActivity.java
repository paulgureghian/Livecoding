package com.example.paul.livecoding.activivities_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.livecoding.BuildConfig;
import com.example.paul.livecoding.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.paul.livecoding.R.id.Access;
import static com.example.paul.livecoding.R.id.auth;

public class LoginActivity extends AppCompatActivity {

    private WebView webView;
    public static String access_token;
    private static String RANDOM_STATE = "state=random_state_string";
    private String OAUTH_URL = "https://www.livecoding.tv/o/authorize/?client_id=" + BuildConfig.CLIENT_ID + "&response_type=token&" + RANDOM_STATE;

    Button auth;
    SharedPreferences pref;
    TextView Access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.title_activity_login));

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access = (TextView) findViewById(R.id.Access);
        auth = (Button) findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {

            Dialog auth_dialog;

            @Override
            public void onClick(View arg0) {

                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                auth_dialog.show();
                auth_dialog.setCancelable(true);

                Log.e("oauthurl", OAUTH_URL);
                webView = (WebView) auth_dialog.findViewById(R.id.webv);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(OAUTH_URL);
                webView.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {


                        access_token = getAccessToken(url);
                        Log.e("access_token", access_token);

                        if (access_token != null) {

                            Intent liveStreamsIntent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(liveStreamsIntent);

                        } else {
                            Toast.makeText(LoginActivity.this, "User needs to login", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });

                webView.loadUrl(OAUTH_URL);
            }

            private String getAccessToken(String OAUTH_URL) {

                String token = null;
                int tokenIndex = OAUTH_URL.indexOf("access_token");

                if (tokenIndex != -1) {
                    tokenIndex = OAUTH_URL.indexOf("=", tokenIndex) + 1;
                    token = OAUTH_URL.substring(tokenIndex, OAUTH_URL.indexOf("&", tokenIndex));
                }
                return token;
            }
        });
    }
}









