package com.example.pallavi.kshamata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class update_info extends AppCompatActivity {
    //dbAdapter db = new dbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);


    }


    public void analyzefunc(View v)
    {
        Intent i = new Intent(this,analyze.class);
        startActivity(i);
    }

}

