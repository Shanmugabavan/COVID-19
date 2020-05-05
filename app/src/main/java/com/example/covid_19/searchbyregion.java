package com.example.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

public class searchbyregion extends AppCompatActivity {
    private TextView country;
    private TextView cases;
    private TextView todayCases;
    private TextView deaths;
    private TextView todayDeaths;
    private TextView recovered;
    private TextView active;
    private TextView critical;
    private TextView tests;
    private TextView timestamp;
    private Timestamp ts;
    private Date date;
    private String region;
    private FirebaseUser user;
    private ImageView flag;
    private ProgressDialog progressBar;


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

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
            if (progressBar.isShowing()){
                progressBar.dismiss();
            }
            super.onPostExecute(s);

            try{
                JSONObject jsonObject=new JSONObject(s);
                ts=new Timestamp(Long.parseLong(jsonObject.getString("updated")));
                date=ts;

                JSONObject jsonObject1=jsonObject.getJSONObject("countryInfo");
                String flagImage=jsonObject1.getString("flag");

                new DownloadImageTask((ImageView) flag)
                        .execute(flagImage);

                country.setText(jsonObject.getString("country"));
                cases.setText(jsonObject.getString("cases"));
                todayCases.setText(jsonObject.getString("todayCases"));
                deaths.setText(jsonObject.getString("deaths"));
                todayDeaths.setText(jsonObject.getString("todayDeaths"));
                recovered.setText(jsonObject.getString("recovered"));
                active.setText(jsonObject.getString("active"));
                critical.setText(jsonObject.getString("critical"));
                tests.setText(jsonObject.getString("tests"));
                timestamp.setText(String.valueOf(ts));
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyregion);

        country=findViewById(R.id.country_val2);
        cases=findViewById(R.id.cases_val2);
        todayCases=findViewById(R.id.todayCases_val2);
        deaths=findViewById(R.id.deaths_val2);
        todayDeaths=findViewById(R.id.todayDeaths_val2);
        recovered=findViewById(R.id.recovered_val2);
        active=findViewById(R.id.active_val2);
        critical=findViewById(R.id.critical_val2);
        tests=findViewById(R.id.tests_val2);
        timestamp=findViewById(R.id.timeStamp_val2);
        flag=findViewById(R.id.country_img2);
        progressBar = new ProgressDialog(searchbyregion.this);
        progressBar.setMessage("Fetching data...(Make sure Internet connection)");
        progressBar.show();


        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference();
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            region=bundle.getString("region");
        }

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DownloadTask downloadTask=new DownloadTask();
                String region_code=dataSnapshot.child("countries").child(region).getValue().toString();
                downloadTask.execute("https://corona.lmao.ninja/v2/countries/"+region_code+"?today");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
