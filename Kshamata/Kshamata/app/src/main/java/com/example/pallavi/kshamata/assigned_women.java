package com.example.pallavi.kshamata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class assigned_women extends AppCompatActivity implements View.OnClickListener {


    Button PersonalInfo, Placements, Independent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_women);

        PersonalInfo = (Button) findViewById(R.id.btnSubmit3);
        Placements = (Button) findViewById(R.id.btnSubmit2);
        Independent = (Button) findViewById(R.id.btnSubmit4);

        PersonalInfo.setOnClickListener(this);
        Placements.setOnClickListener(this);
        Independent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == Placements ){
            finish();
            startActivity(new Intent(this, Skills.class));
        }
        if(view == Independent ){
            finish();
            startActivity(new Intent(this,Independent.class));
        }
        if(view == PersonalInfo ){
            finish();
            startActivity(new Intent(this, Placement.class));
        }
    }
}
