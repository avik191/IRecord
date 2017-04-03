package com.example.hi.irecord;


import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class listFragment extends Fragment {

    RecyclerView recycle;
    ArrayList<Record> list;
    MyCustomAdapter2 adapter;
    static boolean found=false;
    public listFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=new ArrayList<>();

        ConnectivityManager check = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = check.getActiveNetworkInfo();			//for checking whether app is connected to a network or not..
        if(!(info!=null && info.isConnected()))
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            int id      = app_preferences.getInt("id", 0);
            list = getRecordsFromServer("http://irecord.000webhostapp.com/irecord/getrecord.php?userid="+id);
            if (list.size() == 0) {
             //   SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putInt("isemptyrecordlist", 0);
                editor.commit();
            } else {
              //  SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putInt("isemptyrecordlist", 1);
                editor.commit();
            }
        }
       // new Task().execute("http://192.168.1.106:8088/dude/getrecord.php");
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
                        found=true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"cannot connect to server..Try again later or restart the app",Toast.LENGTH_LONG).show();
            }
        });

            MySingleton.getInstance(getActivity()).addtoRequestQueue(request);

            return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int isEmpty      = app_preferences.getInt("isemptyrecordlist", 0);

//        if(list.size()!=0)
//        {
            v= inflater.inflate(R.layout.fragment_list, container, false);


            recycle= (RecyclerView) v.findViewById(R.id.recycle);
            recycle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            adapter=new MyCustomAdapter2(getActivity(),list);
            recycle.setAdapter(adapter);


//        }
//        else
//            v= inflater.inflate(R.layout.norecord, container, false);


        return v;
    }


}
