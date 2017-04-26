package com.example.paul.livecoding.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

import static com.example.paul.livecoding.sharedprefs.Prefs.preferences;

public class LoginActivity extends AppCompatActivity {

    String parsedCode2;
    Dialog auth_dialog;
    String access_token;
    String refresh_token;
    String parsedCode;
    String onresponse;
    public static String code = "";
    private static String RANDOM_STATE = "state=random_state_string";
    Intent liveStreamsIntent;
    Button auth;
    TextView Access;
    private WebView webView;
    private String OAUTH_URL = "https://www.liveedu.tv/o/authorize/?client_id=" + BuildConfig.CLIENT_ID + "&response_type=code&" + RANDOM_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.title_activity_login));

        Access = (TextView) findViewById(R.id.Access);

        liveStreamsIntent = new Intent(LoginActivity.this,
                LiveStreamsOnAirA.class);
        liveStreamsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        parsedCode2 = preferences.getString("parsed_code", parsedCode);
        preferences.edit().putString("parsed_code", parsedCode2).commit();

        if (parsedCode2 != null) {
            startActivity(liveStreamsIntent);
            Log.e("parsedcode2", parsedCode2);
        }

        auth = (Button) findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                auth_dialog.show();
                auth_dialog.setCancelable(true);

                webView = (WebView) auth_dialog.findViewById(R.id.webv);
                webView.getSettings().setJavaScriptEnabled(true);

                Log.e("oauthurl", OAUTH_URL);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (url.contains("http://localhost")) {

                            Uri uri = Uri.parse(url);
                            parsedCode = uri.getQueryParameter("code");

                            preferences.edit().putString("parsed_code", parsedCode).commit();

                            if (!TextUtils.isEmpty(parsedCode)) {

                                startActivity(liveStreamsIntent);

                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.authorize), Toast.LENGTH_SHORT).show();
                            }

                            Log.e("url", url);
                            Log.e("parsedcode", parsedCode);
                        }
                        return false;
                    }
                });
                webView.loadUrl(OAUTH_URL);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (auth_dialog != null) {
            auth_dialog.dismiss();
            auth_dialog = null;
        }
    }
}














