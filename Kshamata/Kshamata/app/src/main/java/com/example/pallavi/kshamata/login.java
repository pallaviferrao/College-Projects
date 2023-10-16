package com.example.pallavi.kshamata;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
=======
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
>>>>>>> 81beaa1446c15b82c85781be92e03461e06e365b
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

<<<<<<< HEAD



=======
>>>>>>> 81beaa1446c15b82c85781be92e03461e06e365b
public class login extends AppCompatActivity {
    EditText name, password,cpass,usn,phone,email;
    Button button;
    String n, p,c,u,ph,e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        button = (Button)findViewById(R.id.submit);
<<<<<<< HEAD


=======
>>>>>>> 81beaa1446c15b82c85781be92e03461e06e365b
    }

    public void submitfunction(View v)
    {
        n = name.getText().toString();
        p = password.getText().toString();



//admin
        if(n.equals("adminroot") && p.equals("adminpass") )
        {
            Toast.makeText(getApplicationContext(), "Loging in. Please wait...", Toast.LENGTH_LONG).show();
            Intent i = new Intent(login.this,admin_activity.class);
            startActivity(i);
        }
<<<<<<< HEAD
        else if(n.equals("balu") || p.equals("balu") )
=======
        else if(n.equals("balu") || p.equals("volunteerpass") )
>>>>>>> 81beaa1446c15b82c85781be92e03461e06e365b
        {

            Toast.makeText(getApplicationContext(), "Loging in. Please wait...", Toast.LENGTH_LONG).show();

            Intent i = new Intent(login.this,volunteer.class);
            startActivity(i);

        }

        else
            Toast.makeText(login.this, "Invalid", Toast.LENGTH_SHORT).show();
    }


    public void clearScr(View v)
    {
        name.setText("");
        password.setText("");
        cpass.setText("");
        usn.setText("");
        email.setText("");
        phone.setText("");
    }

    public void Function(String n)
    {
        n = name.getText().toString();

    }
<<<<<<< HEAD

}

=======
}
>>>>>>> 81beaa1446c15b82c85781be92e03461e06e365b
