package com.talhahasan.projects.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkTask netTask=new NetworkTask();
        netTask.execute("");
    }



    // async tasks allows code to run in background
    class NetworkTask extends AsyncTask<String, Void, String>
    {

        String finalData=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // code to do something before going in doInBackground
       }

        @Override
        protected String doInBackground(String... params) {

            try {

                Uri.Builder ub=new Uri.Builder();
                ub.scheme("http").authority("10.0.2.2").appendPath("firstservice.php");

                URL requestUrl = new URL(ub.toString());  // set link
                HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection(); // create connection
                connection.setRequestMethod("GET");  // set HTTP Method type
                connection.connect();  // connect to server

                int responseCode = connection.getResponseCode(); // get response code from server

                if (responseCode == HttpURLConnection.HTTP_OK) { // check if server says "Ok! i will respond to your request"

                    finalData="";
                    BufferedReader reader = null; //Get buffer ready

                    InputStream inputStream = connection.getInputStream(); // get input stream from server ready

                    if (inputStream == null) { // if there is nothing in stream
                        return "";
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));  // else pass stream data to buffer

                    String line;
                    while ((line = reader.readLine()) != null) {  // read each line

                        finalData+="\n"+line; // save them to string
                    }

                    if (finalData.length() == 0) { // check if string is empty
                        return "";
                    }


                }
                else {
                    Log.d("Unsuccessful", "Unsuccessful HTTP Response Code: " + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.e("Wrong URL", "Error processing  web service URL", e);
            } catch (IOException e) {
                Log.e("Error", "Error connecting to web service", e);
            }

            return finalData;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView tv=(TextView) findViewById(R.id.textView); // get text view reference
            tv.setText(s);  // set data in textview received from server that was passed here in String s variable
        }
    }

}
