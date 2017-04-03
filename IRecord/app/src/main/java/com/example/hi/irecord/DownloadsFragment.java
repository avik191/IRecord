package com.example.hi.irecord;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends Fragment {

    RecyclerView recycle;
    ArrayList<Downloads> list;
    MyCustomAdapter3 adapter;

    public DownloadsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=new ArrayList<>();
        list=getDownloads();
    }

    private ArrayList<Downloads> getDownloads() {
        ArrayList<Downloads> list=new ArrayList<>();
        File file=new File(Environment.getExternalStorageDirectory()+"/extrasssss");
        File listfiles[]=file.listFiles();

        for(int i=0;i<listfiles.length;i++)
        {
            Downloads d=new Downloads();
            d.setName(listfiles[i].getName());
            list.add(d);
        }

        if(list.size()==0) {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("isemptydownloadlist", 0);
            editor.commit();
        }
        else
        {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("isemptydownloadlist", 1);
            editor.commit();
        }
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int isEmpty      = app_preferences.getInt("isemptydownloadlist", 0);

        if(isEmpty!=0)
        {
            v= inflater.inflate(R.layout.fragment_downloads, container, false);


            recycle= (RecyclerView) v.findViewById(R.id.recycle);
            recycle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            adapter=new MyCustomAdapter3(getActivity(),list);
            recycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        else
            v= inflater.inflate(R.layout.norecord, container, false);


        return v;
    }

}
