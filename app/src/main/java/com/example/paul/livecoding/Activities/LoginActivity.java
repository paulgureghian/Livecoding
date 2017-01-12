package com.example.paul.livecoding.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.livecoding.BuildConfig;
import com.example.paul.livecoding.R;

public class LoginActivity extends AppCompatActivity {

    public static String access_token = "";
    private static String RANDOM_STATE = "state=random_state_string";
    Intent liveStreamsIntent;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    private WebView webView;
    private String OAUTH_URL = "https://www.livecoding.tv/o/authorize/?client_id=" + BuildConfig.CLIENT_ID + "&response_type=token&" + RANDOM_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.title_activity_login));

        pref = getSharedPreferences("access_token", MODE_PRIVATE);
        Access = (TextView) findViewById(R.id.Access);

        String stored_token = pref.getString("access_token", access_token);
        if (!TextUtils.isEmpty(stored_token)) {

            liveStreamsIntent = new Intent(LoginActivity.this,
                    LiveStreamsOnAirA.class);

            liveStreamsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(liveStreamsIntent);
            Log.e("stored_token", stored_token);

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.authorize), Toast.LENGTH_SHORT).show();
        }

        auth = (Button) findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {

            Dialog auth_dialog;

            @Override
            public void onClick(View arg0) {

                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                auth_dialog.show();
                auth_dialog.setCancelable(true);

                webView = (WebView) auth_dialog.findViewById(R.id.webv);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(OAUTH_URL);
                Log.e("oauthurl", OAUTH_URL);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        access_token = getAccessToken(url);

                        if (access_token != null) {

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("access_token", access_token);
                            editor.commit();

                            liveStreamsIntent = new Intent(LoginActivity.this,
                                    LiveStreamsOnAirA.class);

                            liveStreamsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(liveStreamsIntent);
                            Log.e("access_token", access_token);

                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.authorize), Toast.LENGTH_SHORT).show();
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









