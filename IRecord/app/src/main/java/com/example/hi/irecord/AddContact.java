package com.example.hi.irecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    EditText etname,etnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etname= (EditText) findViewById(R.id.etname);
        etnumber= (EditText) findViewById(R.id.etnumber);
    }

    public void addcontact(View v)
    {
        String name=etname.getText().toString();
        String number=etnumber.getText().toString();
        if(name.equals("") || number.equals(""))
            Toast.makeText(AddContact.this,"Enter correct details",Toast.LENGTH_SHORT).show();
        else
        {
            try {
                DBAdapter db = new DBAdapter(AddContact.this);
                db.open();
                if (db.insertContact(name, number) > 0) {
                    Toast.makeText(AddContact.this, "Contact Added", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddContact.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                db.close();
            }
            catch (Exception e)
            {
                Toast.makeText(AddContact.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
