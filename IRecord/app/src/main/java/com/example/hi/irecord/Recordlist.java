package com.example.hi.irecord;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recordlist extends AppCompatActivity {
    RecyclerView recycle;
    ArrayList<Record> list;
    MyCustomAdapter2 adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=new ArrayList<>();

        ConnectivityManager check = (ConnectivityManager)Recordlist.this.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
        if(!(info!=null && info.isConnected()))
        {
            Toast.makeText(Recordlist.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(Recordlist.this);
            int id      = app_preferences.getInt("id", 0);
            list = getRecordsFromServer("http://irecord.000webhostapp.com/irecord/getrecord.php?userid="+id);
        }

        setContentView(R.layout.activity_recordlist);
        recycle= (RecyclerView)findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(Recordlist.this));

        adapter=new MyCustomAdapter2(Recordlist.this,list);
        recycle.setAdapter(adapter);
    }

    public ArrayList<Record> getRecordsFromServer(String x) {
        final ArrayList<Record> list=new ArrayList<>();
        String server_url=x;

        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, server_url, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count=0;
                while (count<response.length())
                {
                    Record ob=new Record();
                    try {
                        JSONObject object=response.getJSONObject(count);
                        ob.setId(object.getInt("id"));
                        ob.setName(object.getString("name"));
                        ob.setDate(object.getString("date"));
                        list.add(ob);
                        count++;
                       // found=true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Recordlist.this,"cannot connect to server..Try again later or restart the app",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(Recordlist.this).addtoRequestQueue(request);

        return list;
    }
}
