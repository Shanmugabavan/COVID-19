package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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
    private FirebaseUser user;
    private String username;
    private Button menu;
    private Button reload;
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



    public class DownloadTask extends AsyncTask<String,Void,String>{


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
        setContentView(R.layout.activity_main);


        country=findViewById(R.id.country_val);
        cases=findViewById(R.id.cases_val);
        todayCases=findViewById(R.id.todayCases_val);
        deaths=findViewById(R.id.deaths_val);
        todayDeaths=findViewById(R.id.todayDeaths_val);
        recovered=findViewById(R.id.recovered_val);
        active=findViewById(R.id.active_val);
        critical=findViewById(R.id.critical_val);
        tests=findViewById(R.id.tests_val);
        timestamp=findViewById(R.id.timeStamp_val);
        user= FirebaseAuth.getInstance().getCurrentUser();
        menu=findViewById(R.id.menu_btn_home);
        reload=findViewById(R.id.reload_main_bt);
        flag=findViewById(R.id.country_img);
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Fetching data...(Make sure Internet connection)");
        progressBar.show();

        username=user.getEmail().split("@")[0];


        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference();

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DownloadTask downloadTask=new DownloadTask();
                String country_name=dataSnapshot.child("users").child(username).child("country").getValue().toString();
                String countryCode=dataSnapshot.child("countries").child(country_name).getValue().toString();
                downloadTask.execute("https://corona.lmao.ninja/v2/countries/"+countryCode+"?today");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Menu.class));
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });



    }

}
