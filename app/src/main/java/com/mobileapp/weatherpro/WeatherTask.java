package com.mobileapp.weatherpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private WeatherTaskListener mListener;
    private ProgressDialog mDialog;

    public WeatherTask(Context context, WeatherTaskListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please Wait");
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(true);
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        if (mListener != null) {
            if (!TextUtils.isEmpty(json)) {
                mListener.onDataReceived(json);
            } else {
                mListener.onDataError();
            }
        }

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    interface WeatherTaskListener {
        void onDataReceived(String json);
        void onDataError();
    }
}
