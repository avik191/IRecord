package com.example.hi.irecord;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HI on 02-Apr-17.
 */
public class MyCustomAdapter3 extends RecyclerView.Adapter<MyCustomAdapter3.myviewholder> {

    int prepos = 0;
    Context context;
    ArrayList<Downloads> list;
    LayoutInflater inflater;
    public ProgressDialog dialog;
    public static final int progress_bar_type = 0;
    static MediaPlayer mediaplayer=new MediaPlayer();
    ;


    //FragmentManager fragment;

    public MyCustomAdapter3(Context context, ArrayList<Downloads> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        //   fragment=fragmentManager;
    }




    @Override
    public MyCustomAdapter3.myviewholder onCreateViewHolder(final ViewGroup parent, final int position) {
        View v = inflater.inflate(R.layout.rowitem, parent, false);
        final myviewholder holder = new myviewholder(v, context, list);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter3.myviewholder holder, int position) {


        Downloads re=list.get(position);
        String nm=re.getName();
        holder.name.setText(nm);


        holder.img.setImageResource(R.drawable.play);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener {

        ImageView img;
        TextView name, number;
        Context context;
        int id;
        String fname = "";
        ArrayList<Downloads> list = new ArrayList<>();

        public myviewholder(View itemView, Context context, ArrayList<Downloads> list) {
            super(itemView);
            this.list = list;
            this.context = context;
            img = (ImageView) itemView.findViewById(R.id.row_image);
            name = (TextView) itemView.findViewById(R.id.row_name);
            number = (TextView) itemView.findViewById(R.id.row_number);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        /**
         * Showing Dialog
         */


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Downloads d=list.get(pos);
            String name=d.getName();
            String filePath = Environment.getExternalStorageDirectory()+"/extrasssss/"+name;
            if(mediaplayer.isPlaying())
            {
                mediaplayer.stop();
               // mediaplayer.release();
               mediaplayer.reset();
            }

            try {
                mediaplayer.setDataSource(filePath);
                mediaplayer.prepare();
                mediaplayer.start();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public boolean onLongClick(View view) {
            int pos = getAdapterPosition();
            Downloads ob = list.get(pos);


            //id = ob.getId();

            final CharSequence[] items = {"Stop recording"};

            //Toast.makeText(context,""+pos,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if (item == 0) {

                        if(mediaplayer.isPlaying())
                        {
                            mediaplayer.stop();
                            // mediaplayer.release();
                            mediaplayer.reset();
                        }
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
