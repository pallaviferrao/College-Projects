package com.example.pallvi.kshamata;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class add_woman extends AppCompatActivity {
    dbAdapter db;
    String pname;

    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_woman2);
        db = new dbAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //---add a contact---
        //db.open();

    }

//    public void AddData(View v)
//    {
//
//        EditText txt1, txt2, txt3, txt4;
//        String str1, str2, str3, str4;
//
//        txt1 = (EditText) findViewById(R.id.editText4);
//        txt2 = (EditText) findViewById(R.id.editText);
//        txt3 = (EditText) findViewById(R.id.editText2);
//        txt4 = (EditText) findViewById(R.id.editText5);
//
//        str1 = txt1.getText().toString();
//        str2 = txt2.getText().toString();
//        str3 = txt3.getText().toString();
//        str4 = txt4.getText().toString();
//
//        boolean insertData = db.addDataToTable(str1);
////        boolean insertDatad = DB.addData(dob);
////        boolean insertDataa = DB.addData(adr);
////        boolean insertDatap = DB.addData(ph);
////        boolean insertDatae = DB.addData(email);
////        boolean insertDatab = DB.addData(bg);
////        boolean insertDatah = DB.addData(hi);
////        boolean insertDataw = DB.addData(pwt);
//
//        // boolean insertData = DB.addDataToTable(pname,pdob,padr,pph,pemail,pbg,pwt,phi);
//        Log.d("db","created..");
//
//
//        if(insertData == true)
//        {
//            Toast.makeText(add_woman.this,"Successfully entered",Toast.LENGTH_LONG).show();
//            Log.d("db","created..after entering values");
//        }
//
//        else
//        {
//            Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
//        }
//    }
//

    public void insertContact(View view)
    {
        dbAdapter db = new dbAdapter(this);
        //---add a contact---
        db.open();

        EditText txt1, txt2, txt3, txt4, txt5;
        String str1, str2, str3, str4, str5;

        txt1 = (EditText) findViewById(R.id.editText4);
        txt2 = (EditText) findViewById(R.id.editText);
        txt3 = (EditText) findViewById(R.id.editText2);
        txt4 = (EditText) findViewById(R.id.editText5);
        txt5 = (EditText) findViewById(R.id.ehistory);


        str1 = txt1.getText().toString();
        str2 = txt2.getText().toString();
        str3 = txt3.getText().toString();
        str4 = txt4.getText().toString();
        str5 = txt5.getText().toString();

        long id = db.insertContact(str1, str2, str3, str4);
        if(id != 0)
            Toast.makeText(getApplicationContext(),"Data Inserted Successfully ",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Data could not be inserted ",Toast.LENGTH_LONG).show();
        db.close();
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");
        txt5.clearFocus();
    }




    public void display_details_function(View v)
    {
        Intent intent;
        intent = new Intent(this, display_details.class);
        startActivity(intent);

    }

}
