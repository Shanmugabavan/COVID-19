package com.example.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ListView_Hospitals extends AppCompatActivity {
    private ListView listView;
    private ArrayList hospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view__hospitals);

        listView=findViewById(R.id.listView2);
        hospitals=new ArrayList<String>();

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("hospitals");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    hospitals.add(snapshot.getKey().toString());
                }
                ArrayAdapter arrayAdapter=new ArrayAdapter<>(ListView_Hospitals.this,android.R.layout.simple_list_item_1,hospitals);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ArrayList<String> parcel=new ArrayList<String>();
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        parcel.clear();
                        String m=dataSnapshot.child(hospitals.get(position).toString()).getValue().toString();
                        parcel.add(hospitals.get(position).toString());
                        parcel.add(m);
                        Intent intent=new Intent(ListView_Hospitals.this,searchbyhospital.class);
                        intent.putExtra("hospital",parcel);
                        Toast.makeText(ListView_Hospitals.this,hospitals.get(position).toString(),Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
