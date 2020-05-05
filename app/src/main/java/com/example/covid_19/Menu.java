package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Menu extends AppCompatActivity {
    private Button worldStatus;
    private Button countries;
    private Button hospitalAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        worldStatus=findViewById(R.id.word_status_menu_bt);
        countries=findViewById(R.id.countries_menu_bt);
        hospitalAnalysis=findViewById(R.id.hospitalwise_menu_bt);

        worldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this,World_status.class));

            }
        });

        countries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, ListView_Countries.class));
            }
        });

        hospitalAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this,ListView_Hospitals.class));
            }
        });
    }
}
