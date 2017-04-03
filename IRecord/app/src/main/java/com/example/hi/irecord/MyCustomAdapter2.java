package com.example.hi.irecord;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HI on 01-Apr-17.
 */
public class MyCustomAdapter2 extends RecyclerView.Adapter<MyCustomAdapter2.myviewholder> {

    int prepos = 0;
    Context context;
    ArrayList<Record> list;
    LayoutInflater inflater;
    public ProgressDialog dialog;
    public static final int progress_bar_type = 0;


    //FragmentManager fragment;

    public MyCustomAdapter2(Context context, ArrayList<Record> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
     //   fragment=fragmentManager;
    }




    @Override
    public MyCustomAdapter2.myviewholder onCreateViewHolder(final ViewGroup parent, final int position) {
        View v = inflater.inflate(R.layout.rowitem, parent, false);
        final myviewholder holder = new myviewholder(v, context, list);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter2.myviewholder holder, int position) {


        Record re=list.get(position);
        String nm=re.getName();
        holder.name.setText(nm);
        String num=re.getDate();
        holder.number.setText(num);

        holder.img.setImageResource(R.drawable.download);


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
        String fname="";
        ArrayList<Record> list = new ArrayList<>();

        public myviewholder(View itemView, Context context, ArrayList<Record> list) {
            super(itemView);
            this.list = list;
            this.context = context;
            img = (ImageView) itemView.findViewById(R.id.row_image);
            name= (TextView) itemView.findViewById(R.id.row_name);
            number = (TextView) itemView.findViewById(R.id.row_number);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        /**
         * Showing Dialog
         * */


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
            Record ob=list.get(pos);
            id=ob.getId();

            final CharSequence[] items = {"Download record"};

            //Toast.makeText(context,""+pos,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if(item==0)
                    {
                        String server_url="http://irecord.000webhostapp.com/irecord/downloadrecord.php?id="+id;
                        StringRequest stringRequest=new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                fname=response;
                                String server_url="http://irecord.000webhostapp.com/irecord/uploads/"+response+".mp3";
                                new DownloadFileFromURL().execute(server_url);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

                                //request.stop();
                            }
                        });
                        MySingleton.getInstance(context).addtoRequestQueue(stringRequest);


                    }
                    //Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }

        private class DownloadFileFromURL extends AsyncTask<String, String, String>{


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(context);
                dialog.setMessage("Downloading file. Please wait...");
                dialog.setIndeterminate(false);
                dialog.setMax(100);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(true);
                dialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                int count;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/extrasssss/"+fname+".mp3");

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    return "error";
                }

                return "Download Complete";
            }

            protected void onProgressUpdate(String... progress) {
                // setting progress percentage
                dialog.setProgress(Integer.parseInt(progress[0]));
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                if(s.equals("error"))
                    Toast.makeText(context,"cannot download..try again later",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context,s+" Restart app to see changes",Toast.LENGTH_LONG).show();
            }
        }
    }
}