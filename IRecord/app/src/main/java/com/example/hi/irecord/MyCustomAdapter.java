package com.example.hi.irecord;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HI on 30-Mar-17.
 */

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.myviewholder> {

    int prepos = 0;
    Context context;
    ArrayList<Contact> list;
    LayoutInflater inflater;
    FragmentManager fragment;

    public MyCustomAdapter(Context context, ArrayList<Contact> list, FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        fragment=fragmentManager;
    }

    @Override
    public MyCustomAdapter.myviewholder onCreateViewHolder(final ViewGroup parent, final int position) {
        View v = inflater.inflate(R.layout.rowitem, parent, false);
        final myviewholder holder = new myviewholder(v, context, list);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.myviewholder holder, int position) {


        Contact re=list.get(position);
        String nm=re.getName();
        holder.name.setText(nm);
        String num=re.getNumber();
        holder.number.setText(num);

        holder.img.setImageResource(R.drawable.contact);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

        ImageView img;
        TextView name,number;
        Context context;
        int id;
        ArrayList<Contact> list = new ArrayList<>();

        public myviewholder(View itemView, Context context, ArrayList<Contact> list) {
            super(itemView);
            this.list = list;
            this.context = context;
            img = (ImageView) itemView.findViewById(R.id.row_image);
            name= (TextView) itemView.findViewById(R.id.row_name);
            number = (TextView) itemView.findViewById(R.id.row_number);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
           // pos = getAdapterPosition();
           // Toast.makeText(context,""+pos,Toast.LENGTH_LONG).show();

//            Intent i=new Intent(context,ShowDetail.class);
//            Bundle b=new Bundle();
//            b.putInt("pos",pos);
//            b.putParcelableArrayList("list",list);
//            i.putExtras(b);
//            context.startActivity(i);

        }

        @Override
        public boolean onLongClick(View view) {
            int pos = getAdapterPosition();
            Contact ob=list.get(pos);
             id=ob.getId();

            final CharSequence[] items = {"Delete Contact"};

            //Toast.makeText(context,""+pos,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if(item==0)
                    {
                        DBAdapter db=new DBAdapter(context);

                        db.open();
                        if(db.deleteContact(id)) {
                            Toast.makeText(context, "contact deleted", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(context,"cannot delete contact", Toast.LENGTH_SHORT).show();
                        db.close();

                        Intent i=new Intent(context,MainActivity.class);
                        context.startActivity(i);

                    }
                    //Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
    }
}
