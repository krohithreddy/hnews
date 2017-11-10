package com.example.rohithreddy.hnews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String header = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ImageView image = (ImageView) findViewById(R.id.imageView);
        TextView title = (TextView) findViewById(R.id.title);
        TextView body = (TextView) findViewById(R.id.body);
        title.setText(header);
        body.setText(message);
        System.out.println("the url u r looking at is ============="+url);
        if(url.length()>0){
            if(!url.equals("null")) {
                InputStream imageStream = this.getResources().openRawResource(R.raw.loading);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(bitmap);
                new ImageLoadTask(url, image,Notification.this).execute();
            }
        }




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
//    public static Bitmap getBitmapFromURL(String src) {
//        new AsyncTask<Void, Void, Void>()
//        {
//            @Override
//            protected Void doInBackground(Void... arg0)
//            {
//                try {
//                    Log.e("src",src);
//                    URL url = new URL(src);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream input = connection.getInputStream();
//                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                    Log.e("Bitmap","returned");
//                    return myBitmap;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Exception",e.getMessage());
//                    return null;
//                }
//           return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//
//            }
//        }.execute();
//
//    }
}
