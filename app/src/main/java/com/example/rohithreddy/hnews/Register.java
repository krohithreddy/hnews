package com.example.rohithreddy.hnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
     UserSessionManager session;
    private ProgressDialog pDialog;
    String x="nointernet"; String responseBody; String firstname="",fullname;
    Response response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  Initialization();
        //  SP = getSharedPreferences(filename, 0);
        //  String getname = SP.getString("key1","");
        //  etname.setText(getname);
       session = new UserSessionManager(getApplicationContext());

        if(session.isUserLoggedIn()){
            Intent loginIntent = new Intent(Register.this, MainActivity.class);
            Register.this.startActivity(loginIntent);
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                }
            }
        };

        final EditText phonenum = (EditText) findViewById(R.id.username);
        final Button blogin = (Button) findViewById(R.id.login);
        phonenum.setFilters( new InputFilter[] { new InputFilter.LengthFilter(10)});
        blogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(phonenum.getText().toString().trim().length() < 10) {
                    phonenum.setError("phone number should be 10 digits");
                }

                else {
                    final String name = phonenum.getText().toString();
                    new AsyncTask<Void, Void, Void>()
                    {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            pDialog = new ProgressDialog(Register.this);
                            pDialog.setMessage("Please wait...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                        @Override
                        protected Void doInBackground(Void... arg0)
                        {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                            String regId = pref.getString("regId", null);

                            OkHttpClient client = new OkHttpClient();
                            RequestBody formBody = new FormBody.Builder()
                                    .add("phoneNumber", name)
                                    .add("fireBaseId",regId)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://services.heterohcl.com/Testing/firebase/register.php")
                                    .post(formBody)
                                    .build();
                            try {
                                response = client.newCall(request).execute();
                                responseBody = response.body().string();
                                System.out.println("respone is -------------"+responseBody);
                                try {
                                    JSONObject jsonObj = new JSONObject(responseBody);
                                    firstname = jsonObj.getString("status");
                                    /////////
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                x="internet";
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            if (x=="nointernet"){
                                Toast.makeText(getApplicationContext(),"internet problem or server down", Toast.LENGTH_LONG).show();
                            }
                            else if(firstname.equals("success")){
                                Intent loginIntent = new Intent(Register.this, MainActivity.class);
                                session.createUserLoginSession(name);
                                Register.this.startActivity(loginIntent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Not a registered user or server down", Toast.LENGTH_LONG).show();
                                //phonenum.setText("");
                                x="nointernet";
                            }
                        }
                    }.execute();
                }
            }
        });
        phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phonenum.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s) {
                phonenum.setError(null);
            }
        });

    }

    //    private void Initialization() {
//        // TODO Auto-generated method stub
//
//        etname = (EditText) findViewById(R.id.username);
//
//        submit = (Button) findViewById(R.id.login);
//
//        //tvoutput = (TextView) findViewById(R.id.tv_output);
//        //submit.setOnClickListener();
//
//    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
}


