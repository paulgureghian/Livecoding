package com.example.paul.livecoding.activivities_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.paul.livecoding.R;
import com.example.paul.livecoding.endpoints.LiveStreams_OnAir;
import com.example.paul.livecoding.oauthentication.AccessToken;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAir;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAirDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.paul.livecoding.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements Callback<List<LiveStreamsOnAir>> {

    private static String RANDOM_STATE = "random_state_string";
    private static String CLIENT_ID = "kBadIfKZYpgLTowhMreDXJ5ckyGz7t8BxUdIJ2XC";
    private static String CLIENT_SECRET = "yfhYWhsXkjct9R2bfHYVMGcdEQWNT7Plc99MYx98ejlJpc90Mj2hky3ADOPWeyTZz43KrjGrkQLEPUawkZnsmFfxm8RZQzqVZuQ4SxtdISBrDAqbjt1OWuv60LEBDL7R";
    private static String REDIRECT_URI = "http://localhost/externalapp";
    private static String GRANT_TYPE = "implicit";
    private static String OAUTH_TOKEN_URL = "https://www.livecoding.tv/o/auhorize/?client_id=" + CLIENT_ID + "&response_type=token&" + RANDOM_STATE;

    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;

    List<LiveStreamsOnAir> items;
    Type listType = new TypeToken<List<LiveStreamsOnAir>>() {}.getType();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access = (TextView) findViewById(R.id.Access);
        auth = (Button) findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;

            @Override
            public void onClick(View view) {
                auth_dialog = new Dialog(MainActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView) auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(OAUTH_TOKEN_URL + "?redirect_uri=" + REDIRECT_URI + "&response_type=token&client_id" + CLIENT_ID);
                web.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public void onPageStarted(WebView view1, String url, Bitmap favicon) {
                        super.onPageStarted(view1, url, favicon);
                    }

                    String authCode;

                    @Override
                    public void onPageFinished(WebView view1, String url) {
                        super.onPageFinished(view1, url);

                        if (url.contains("?code=") && authComplete != true) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("", "CODE: " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            MainActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();
                            new TokenGet().execute();
                            Toast.makeText(getApplicationContext(), "Authorization code is: " + authCode, Toast.LENGTH_SHORT).show();
                        } else if (url.contains("error_acccess_denied")) {
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_SHORT).show();

                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Authorize Livecoding");
                auth_dialog.setCancelable(true);
            }
        });
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Contacting Livecoding...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            AccessToken jParser = new AccessToken();
            JSONObject json = jParser.gettoken(OAUTH_TOKEN_URL, Code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null) {
                try {
                    String tok = json.getString("access_token");
                    String expire = json.getString("expires_in");
                    String refresh = json.getString("refresh_token");

                    Log.d("Token Access", tok);
                    Log.d("Expire", expire);
                    Log.d("Refresh", refresh);
                    auth.setText("Authenticated");
                    Access.setText("Access Token:" + tok + "nExpires:" + expire + "nRefresh Token:" + refresh);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();



                Gson gson = new GsonBuilder()
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.livecoding.tv/")
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                                listType, new LiveStreamsOnAirDeserializer()).create()))
                        .build();
                LiveStreams_OnAir liveStreams_onAir = retrofit.create(LiveStreams_OnAir.class);
                Call<List<LiveStreamsOnAir>> call = liveStreams_onAir.getData();
//        call.enqueue(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse
            (Call<List<LiveStreamsOnAir>> call, Response<List<LiveStreamsOnAir>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreamsOnAir> items = response.body();

        Log.d("res", response.raw().toString());

        for (LiveStreamsOnAir item : items) {
            Log.i("item", item.getUser());
        }
        if (code == 200) {

            Toast.makeText(this, context.getResources().getString(R.string.connection_made), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, context.getResources().getString(R.string.no_connection_made) + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<LiveStreamsOnAir>> call, Throwable t) {
        Toast.makeText(this, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
    }
}







































































































































