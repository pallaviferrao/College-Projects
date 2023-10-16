package com.example.pallavi.kshamata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class List_of_Volunteers extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of__volunteers);
        listView = (ListView) findViewById(R.id.listofvolunteers);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext() , android.R.layout.simple_list_item_1 , Manager.listofvolunteers);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Hol",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(List_of_Volunteers.this,List_of_Women.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent inti = new Intent(List_of_Volunteers.this, admin_activity.class);
        startActivity(inti);
        finish();
    }
}
