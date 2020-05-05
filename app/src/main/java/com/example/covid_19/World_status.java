package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

public class World_status extends AppCompatActivity {
    private TextView cases;
    private TextView todayCases;
    private TextView deaths;
    private TextView todayDeaths;
    private TextView recovered;
    private TextView active;
    private TextView critical;
    private TextView tests;
    private TextView timestamp;
    private TextView affectedCountries;
    private Timestamp ts;
    private Date date;
    private ProgressDialog progressBar;

    public class DownloadTask1 extends AsyncTask<String,Void,String> {


        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();

                while (data!=-1){
                    char current=(char) data;
                    result+=current;
                    data=reader.read();

                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressBar.isShowing()){
                progressBar.dismiss();
            }

            try{
                JSONObject jsonObject=new JSONObject(s);
                ts=new Timestamp(Long.parseLong(jsonObject.getString("updated")));
                date=ts;

                cases.setText(jsonObject.getString("cases"));
                todayCases.setText(jsonObject.getString("todayCases"));
                deaths.setText(jsonObject.getString("deaths"));
                todayDeaths.setText(jsonObject.getString("todayDeaths"));
                recovered.setText(jsonObject.getString("recovered"));
                active.setText(jsonObject.getString("active"));
                critical.setText(jsonObject.getString("critical"));
                tests.setText(jsonObject.getString("tests"));
                affectedCountries.setText(jsonObject.getString("affectedCountries"));
                timestamp.setText(String.valueOf(ts));
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_status);

        cases=findViewById(R.id.cases_val1);
        todayCases=findViewById(R.id.todayCases_val1);
        deaths=findViewById(R.id.deaths_val1);
        todayDeaths=findViewById(R.id.todayDeaths_val1);
        recovered=findViewById(R.id.recovered_val1);
        active=findViewById(R.id.active_val1);
        critical=findViewById(R.id.critical_val1);
        tests=findViewById(R.id.tests_val1);
        timestamp=findViewById(R.id.timeStamp_val1);
        affectedCountries=findViewById(R.id.affectedCountries_val);
        progressBar = new ProgressDialog(World_status.this);
        progressBar.setMessage("Fetching data...(Make sure Internet connection)");
        progressBar.show();

        DownloadTask1 downloadTask=new DownloadTask1();
        downloadTask.execute("https://corona.lmao.ninja/v2/all?today=");
    }
}
