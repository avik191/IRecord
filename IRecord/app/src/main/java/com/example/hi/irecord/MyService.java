package com.example.hi.irecord;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HI on 29-Mar-17.
 */

public class MyService extends Service {
static MediaRecorder m_recorder=new MediaRecorder();
static boolean isRecording=false;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();

        start_recorder();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "2. onStartCommand Executed ", Toast.LENGTH_SHORT).show();
     return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(isRecording==true) {
            Toast.makeText(this, "4. Service Destroyed", Toast.LENGTH_LONG).show();
            stop_recorder();
        }
    }

    public void start_recorder () {
        isRecording=true;


        m_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        m_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        m_recorder.setOutputFile(Environment.getExternalStorageDirectory()+"/extrasssss/newrecord.3gp");
        m_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            m_recorder.prepare();
            m_recorder.start();
            Toast.makeText(this,"started",Toast.LENGTH_LONG).show();
            MainActivity.isrecording=1;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stop_recorder () {

        isRecording=false;
        m_recorder.stop();
        m_recorder.release();
        Uri file = Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), "/extrasssss/newrecord.3gp"));
        Toast.makeText(this,
                "record stored at "+file.toString(),
                Toast.LENGTH_LONG).show();
        MainActivity.isrecording=0;
        
        uploadToServer();


        //t_manager.listen(p_listener, PhoneStateListener.LISTEN_NONE);
    }

    private void uploadToServer() {
        String server_url="http://irecord.000webhostapp.com/irecord/vollyupload.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                File file = new File(Environment.getExternalStorageDirectory(), "/extrasssss/newrecord.3gp");
                boolean deleted = file.delete();
                if(deleted==true)
                    Toast.makeText(getApplicationContext(),"File deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"cannot delete file",Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                //request.stop();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                //params.put("image",ImageToString(bitmap));
                //Time today = new Time(Time.getCurrentTimezone());
                //today.setToNow();
                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String s = app_preferences.getString("name","avik")+"_";
                int id      = app_preferences.getInt("id", 0);
                //String s="avik_";
                //String date=today.monthDay+"/"+(today.month+1)+"/"+today.year;

                params.put("audio",FiletoString());
                params.put("name",s);
                params.put("user_id",Integer.toString(id));
                //params.put("date",date);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(stringRequest);
    }

    private String FiletoString() {
        File file = new File(Environment.getExternalStorageDirectory(), "/extrasssss/newrecord.3gp");

        byte[] filebyte= new byte[0];
        filebyte = read(file);
        return Base64.encodeToString(filebyte,Base64.DEFAULT);
    }

    private byte[] read(File file) {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }
}

