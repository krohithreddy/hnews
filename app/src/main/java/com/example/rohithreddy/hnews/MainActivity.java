package com.example.rohithreddy.hnews;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import com.example.rohithreddy.hnews.R;
import com.example.rohithreddy.hnews.Config;
import com.example.rohithreddy.hnews.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    private SQLiteDatabase db;
    private Cursor c;
    MyItemRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private List<mapuser> mapuserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("                 Notifications");  // provide compatibility to all the versions


//        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
//        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mapuserList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), mColumnCount));
        }

//        final EditText search = (EditText) findViewById(R.id.outletname);
//        mapuserList = new ArrayList<>();
//        mapuserList.clear();
//        GetList("all");

//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String x =search.getText().toString();
//                System.out.println(x);
//               // GetList(x);
//                System.out.println(x);
//                // /search.setError(null);
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        // mapuserList.add(0,new mapuser(14,"rggdfgo","gndsgng","gndsgng","gndsgng","gndsgng"));
        // mapuserList.add(0,new mapuser(12,"hhhhhhhh","hhhhhhh","hhhhhhh","hhh","gndsgng"));
        getlist();
        adapter =new MyItemRecyclerViewAdapter(mapuserList, MainActivity.this);
        recyclerView.setAdapter(adapter);

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");


//                    txtMessage.setText(message);
                    getlist();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        displayFirebaseRegId();
    }

    public void getlist(){
        db=openOrCreateDatabase("HnewsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Notification(title VARCHAR,message VARCHAR,imageUrl VARCHAR,timestamp VARCHAR,id VARCHAR);");
        c = db.rawQuery("SELECT * FROM Notification", null);
        mapuserList.clear();
        if (!(c.moveToFirst()) || c.getCount() == 0){
            System.out.println("empty db ---------------=======");
        }
        else {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                mapuserList.add(0,new mapuser(c.getString(0),c.getString(1),c.getString(2),c.getString(3)));
                c.moveToNext();
            }
        }
        c.close();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();



        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        getlist();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}//eWESem0GO9c:APA91bHN5hOqa0umKPcXwPTvEBtI3IFWqksLBTQTxInicYyZtk_dW8uwygdp64yKHv0nCp3f23a6QV9Urtxnhlhga5OrbktV9BiivNOzyhlMm7N0vxRjZlJKnmCepXP6SROlkGqRzxbB