package com.example.pallavi.kshamata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }


    public void update(View v){
        Intent intent;
        intent = new Intent(this, update_info.class);
        startActivity(intent);

    }

    public void track(View v){
        Intent intent;
        intent = new Intent(this, track_woman.class);
        startActivity(intent);

        }
    public void history(View v)
    {
        Intent intent;
        intent = new Intent(this, story.class);
        startActivity(intent);

    }
}
