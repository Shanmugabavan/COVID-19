package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

public class searchbyhospital extends AppCompatActivity {
    private TextView hospital;
    private TextView hospitalId;
    private TextView cumulativeLocal;
    private TextView cumulativeForeign;
    private TextView treatmentLocal;
    private TextView treatmentForeign;
    private TextView cumulativeTotal;
    private TextView treatmentTotal;
    private TextView time;
    private ArrayList bundlee;
    private String hospitale;
    private int hospital_id;
    private ProgressDialog progressBar;

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
            super.onPostExecute(s);


            try{
                JSONObject jsonObject=new JSONObject(s);
                Bundle bundle=getIntent().getExtras();

                if (bundle!=null){
                    bundlee=bundle.getStringArrayList("hospital");
                    hospitale= (String) bundlee.get(0);
                    hospital_id=Integer.parseInt(bundlee.get(1).toString());


                }
                JSONObject jsonObject1= (JSONObject) jsonObject.getJSONObject("data").getJSONArray("hospital_data").get(hospital_id-1);

                hospital.setText(hospitale);
                hospitalId.setText(String.valueOf(hospital_id));
                cumulativeLocal.setText(jsonObject1.getString("cumulative_local"));
                cumulativeForeign.setText(jsonObject1.getString("cumulative_foreign"));
                treatmentLocal.setText(jsonObject1.getString("treatment_local"));
                treatmentForeign.setText(jsonObject1.getString("treatment_foreign"));
                cumulativeTotal.setText(jsonObject1.getString("cumulative_total"));
                treatmentTotal.setText(jsonObject1.getString("treatment_total"));
                time.setText(jsonObject1.getString("created_at"));
                if (progressBar.isShowing()){
                    progressBar.dismiss();
                }

//
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyhospital);

        hospital=findViewById(R.id.hospital_val);
        hospitalId=findViewById(R.id.hospital_id_val);
        cumulativeLocal=findViewById(R.id.cumulative_local_val);
        cumulativeForeign=findViewById(R.id.cumulative_foreign_val);
        treatmentLocal=findViewById(R.id.treatment_local_val);
        treatmentForeign=findViewById(R.id.treatment_foreign_val);
        cumulativeTotal=findViewById(R.id.cumulative_total_val);
        treatmentTotal=findViewById(R.id.treatment_total_val);
        time=findViewById(R.id.timeStamp_val3);
        progressBar = new ProgressDialog(searchbyhospital.this);
        progressBar.setMessage("Fetching Data...(Make sure Internet connection)");
        progressBar.show();

        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute("https://www.hpb.health.gov.lk/api/get-current-statistical");

    }
}
