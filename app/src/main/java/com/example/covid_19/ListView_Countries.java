package com.example.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListView_Countries extends AppCompatActivity {
    private ListView listVieww;
    private ArrayList<String>countries;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view__countries);

        progressBar = new ProgressDialog(ListView_Countries.this);
        progressBar.setMessage("Fetching data...(Make sure Internet connection)");
        progressBar.show();

        listVieww=findViewById(R.id.listView);
        countries=new ArrayList<String>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("countries");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (progressBar.isShowing()){
                    progressBar.dismiss();
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    countries.add(snapshot.getKey().toString());
                }
                ArrayAdapter arrayAdapter=new ArrayAdapter<>(ListView_Countries.this,android.R.layout.simple_list_item_1,countries);
                listVieww.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListView_Countries.this,countries.get(position).toString(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ListView_Countries.this,searchbyregion.class);
                intent.putExtra("region",countries.get(position));
                startActivity(intent);

            }
        });




    }
}
