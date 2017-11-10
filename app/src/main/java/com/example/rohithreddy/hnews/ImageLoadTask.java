package com.example.rohithreddy.hnews;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rohithreddy on 01/11/17.
 */

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private ProgressDialog pDialog;
    private String url;
    private ImageView imageView;
    private Context contextl;

    public ImageLoadTask(String url, ImageView imageView, Context context) {
        this.url = url;
        this.imageView = imageView;
        this.contextl = context;
    }
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        pDialog = new
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        System.out.println("this is what we are looking at "+result);
        if(result==null){
            InputStream imageStream = contextl.getResources().openRawResource(R.raw.azista);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(bitmap);
        }
        else{
            imageView.setImageBitmap(result);
        }
    }

}