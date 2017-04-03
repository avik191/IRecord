package com.example.hi.irecord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

public class Login extends AppCompatActivity {
    EditText email,pass;
    TextInputLayout lemail,lpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences app_preferences= PreferenceManager.getDefaultSharedPreferences(Login.this);
        int login=app_preferences.getInt("login",0);
        if(login==1)
        {
            Intent i=new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_login);


        ConnectivityManager check = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
        if(!(info!=null && info.isConnected()))
        {
            Toast.makeText(Login.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.password);

        lemail= (TextInputLayout) findViewById(R.id.layout_email);
        lpass= (TextInputLayout) findViewById(R.id.layout_password);


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

    public void login(View v)
    {
        if(!validateEmail())
            return;
        if(!validatePass())
            return;

        String e=email.getText().toString();
        String p=pass.getText().toString();
        //Toast.makeText(Register.this,"All inputs are valid",Toast.LENGTH_SHORT).show();
        String k="http://irecord.000webhostapp.com/irecord/checkuser.php?email="+e+"&pass="+p;
        //Toast.makeText(Register.this,k,Toast.LENGTH_LONG).show();
        MyAsync task5 = new MyAsync();
        task5.execute(k);
    }

    public void goregister(View v)
    {
        Intent i=new Intent(Login.this,Register.class);
        startActivity(i);
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
                    return "error";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //webPage=e.toString();
                return "error";
            }
            return webPage;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.equals("error"))
                Toast.makeText(Login.this,"Cannot connect to server..Please try later",Toast.LENGTH_LONG).show();
            else if(s.equals("failed") || s.equals(""))
                Toast.makeText(Login.this,"Login Failed", Toast.LENGTH_SHORT).show();
            else
            {
                String temp[] = s.split(",");
                Toast.makeText(Login.this,"Welcome "+temp[0], Toast.LENGTH_SHORT).show();

                SharedPreferences app_preferences= PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor editor= app_preferences.edit();

                editor.putString("name",temp[0]);
                editor.putString("email",temp[1]);
                editor.putString("password",temp[2]);
                editor.putInt("id",Integer.parseInt(temp[3]));
                editor.putInt("login",1);

                editor.commit();

                Intent i=new Intent(Login.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
