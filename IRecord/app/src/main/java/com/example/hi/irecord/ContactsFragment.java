package com.example.hi.irecord;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

RecyclerView recycle;
    DBAdapter db;
    ArrayList<Contact> list;
    MyCustomAdapter adapter;

    public ContactsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=new ArrayList<>();
        db=new DBAdapter(getActivity());
        list=getContactsFromDb();
    }

    private ArrayList<Contact> getContactsFromDb() {
        ArrayList<Contact> list=new ArrayList<>();
        db.open();
        Cursor c = db.getAllContacts();
        if(c!=null) {
            if (c.moveToFirst()) {
                do {
                    Contact ob = new Contact();
                    ob.setId(c.getInt(0));
                    ob.setName(c.getString(1));
                    ob.setNumber(c.getString(2));

                    list.add(ob);

                } while (c.moveToNext());
            }
            db.close();
        }
        if(list.size()==0) {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("isemptycontactlist", 0);
            editor.commit();
        }
        else
        {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("isemptycontactlist", 1);
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
        int isEmpty      = app_preferences.getInt("isemptycontactlist", 0);

        if(isEmpty!=0)
        {
            v= inflater.inflate(R.layout.fragment_contacts, container, false);


            recycle= (RecyclerView) v.findViewById(R.id.recycle);
            recycle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            adapter=new MyCustomAdapter(getActivity(),list,getFragmentManager());
            recycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        else
            v= inflater.inflate(R.layout.nodata, container, false);


        return v;
    }

}
