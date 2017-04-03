package com.example.hi.irecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by HI on 28-Mar-17.
 */

public class BrCallReceive extends BroadcastReceiver{


    Context c;
    static boolean wasRinging=false,recordstarted=false;
    String inCall;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        c = context;
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);


            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                 inCall = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER); //Getting the incoming number

                //checking that the incoming number is present int the databse or not.if present then we will record the call
                DBAdapter db=new DBAdapter(context);
                    db.open();
                Cursor c=db.getAllContacts();
                if(c!=null) {
                    if (c.moveToFirst()) {
                        do {
                            String num="+91"+c.getString(2);
                            if(inCall.equals(num))
                            {
                                wasRinging = true;
                                break;
                            }
                        } while (c.moveToNext());
                    }
                    db.close();
                }
              //  wasRinging = true;
                Toast.makeText(context, "ringing ", Toast.LENGTH_LONG).show();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {


                if(wasRinging){
                    intent = new Intent(context, MyService.class);
                      context.startService(intent);
                }
                else
               Toast.makeText(context, "not same number", Toast.LENGTH_LONG).show();

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                intent = new Intent(context, MyService.class);
                context.stopService(intent);

            }
        }
    }


}
