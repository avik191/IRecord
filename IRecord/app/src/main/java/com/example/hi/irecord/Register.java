package com.example.hi.irecord;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity {

    EditText name,email,pass;
    TextInputLayout lname,lemail,lpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name= (EditText) findViewById(R.id.name);
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.password);

        lname= (TextInputLayout) findViewById(R.id.layout_name);
        lemail= (TextInputLayout) findViewById(R.id.layout_email);
        lpass= (TextInputLayout) findViewById(R.id.layout_password);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateName();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateEmail();
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validatePass();
            }
        });
    }

    private boolean validatePass() {

        if(pass.getText().toString().trim().isEmpty())
        {
            lpass.setError("Enter proper password");
            requestFocus(pass);
            return false;
        }
        else
            lpass.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmail() {

        String s=email.getText().toString().trim();
        int x=s.indexOf("@");
        if(s.isEmpty() || x==-1)
        {
            lemail.setError("Enter proper email");
            requestFocus(email);
            return false;
        }
        else
            lemail.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View v) {

        if(v.requestFocus())
        {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if(name.getText().toString().trim().isEmpty())
        {
            lname.setError("Enter proper name");
            requestFocus(name);
            return false;
        }
        else
            lname.setErrorEnabled(false);
        return true;
    }

    public void signup(View v)
    {
        if(!validateName())
            return;
        if(!validateEmail())
            return;
        if(!validatePass())
            return;

        String n=name.getText().toString();
        String e=email.getText().toString();
        String p=pass.getText().toString();

        String k="http://192.168.1.107:8088/dude/adduser.php?name="+n+"&email="+e+"&pass="+p;

        MyAsync task5 = new MyAsync();
        task5.execute(k);
    }

    public class MyAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            String data="",webPage="";
            try {
                URL url=new URL(strings[0]);
                //Toast.makeText(MainActivity.this, "async1", Toast.LENGTH_SHORT).show();

                try {
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();// Opening browser and entering url
                    con.connect();						//going to url...
                    InputStream is = con.getInputStream();	//getting response from the page...
                    BufferedReader reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));	//converting it into string..


                    while ((data = reader.readLine()) != null){
                        webPage += data;
                    }
                    //Toast.makeText(MainActivity.this, "async2", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    // webPage=e.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //webPage=e.toString();
            }
            return webPage;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(Register.this,s, Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Register.this,Login.class);
            startActivity(i);
            finish();
        }
    }
}
