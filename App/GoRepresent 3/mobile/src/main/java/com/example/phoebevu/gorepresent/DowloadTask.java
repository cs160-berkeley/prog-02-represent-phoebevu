package com.example.phoebevu.gorepresent;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by phoebevu on 3/10/16.
 */
public class DowloadTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;


    public DowloadTask(AsyncResponse delegate) {
        super();
        this.delegate = delegate;
    }
    public DowloadTask() {
        super();
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        if (delegate == null) {
            return;
        }
        delegate.processFinish(result);
    }
    private String downloadUrl(String myurl) throws IOException {
        try {
            Log.d("BILL URL:", myurl);
            URL url = new URL(myurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }

}
