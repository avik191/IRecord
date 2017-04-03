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

import static com.example.hi.irecord.R.id.email;

public class Account extends AppCompatActivity {

        EditText name,email,pass;
        TextInputLayout lname,lemail,lpass;
        String n="",e="",p="";
        int id=0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_account);

            name= (EditText) findViewById(R.id.name);
            email= (EditText) findViewById(R.id.email);
            pass= (EditText) findViewById(R.id.password);

            lname= (TextInputLayout) findViewById(R.id.layout_name);
            lemail= (TextInputLayout) findViewById(R.id.layout_email);
            lpass= (TextInputLayout) findViewById(R.id.layout_password);

            SharedPreferences app_preferences= PreferenceManager.getDefaultSharedPreferences(Account.this);
            n=app_preferences.getString("name","");
            e=app_preferences.getString("email","");
            p=app_preferences.getString("password","");
            id=app_preferences.getInt("id",0);

            name.setText(n);
            email.setText(e);
            pass.setText(p);

            //Toast.makeText(Account.this,id+" ",Toast.LENGTH_LONG).show();
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

        public void update(View v)
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

            ConnectivityManager check = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
            if(info!=null && info.isConnected())
            {
                String k = "http://irecord.000webhostapp.com/irecord/updateuser.php?name=" + n + "&email=" + e + "&pass=" + p + "&id=" + id;

                MyAsync task5 = new MyAsync();
                task5.execute(k);
            }
            else
                Toast.makeText(Account.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        public void signout(View v)
        {
            SharedPreferences app_preferences= PreferenceManager.getDefaultSharedPreferences(Account.this);
            SharedPreferences.Editor editor= app_preferences.edit();
            editor.putInt("login",0);
            editor.commit();
            Intent i=new Intent(Account.this,Login.class);
            startActivity(i);
            finish();
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
                    Toast.makeText(Account.this,"Cannot connect to server..Please try later",Toast.LENGTH_LONG).show();
                else if(!s.equals(""))
                {
                    Toast.makeText(Account.this, s, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Account.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }
}

