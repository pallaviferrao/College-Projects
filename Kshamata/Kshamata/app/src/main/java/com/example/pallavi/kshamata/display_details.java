package com.example.pallavi.kshamata;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class display_details extends AppCompatActivity {
View view;

    TextView name,dob, age, phone,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (TextView)findViewById(R.id.name);
        dob = (TextView)findViewById(R.id.dob);
        age = (TextView)findViewById(R.id.age);
        phone = (TextView)findViewById(R.id.phone);
        history = (TextView)findViewById(R.id.historytext);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        displayContact(view);
    }

    public void displayContact(View view) {
        dbAdapter db = new dbAdapter(this);
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {
                display(c);
            } while (c.moveToNext());
        }
        db.close();
    }

    public void display(Cursor c)
    {

        name.setText(c.getString(1));
        dob.setText(c.getString(2));
        String s = c.getString(0) + c.getString(1) + c.getString(2);
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "dob: " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }
}
