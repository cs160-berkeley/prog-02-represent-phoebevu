package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by phoebevu on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        Context context = getApplicationContext();
        String[] parseStr = value.split("\\|");
        if (!parseStr[0].equalsIgnoreCase("RANDOM")) {
            Intent detail = new Intent(this, DetailView.class);
            detail.putExtra("TITLE", parseStr[0]);
            detail.putExtra("NAME", parseStr[1]);
            detail.putExtra("PARTY", parseStr[2]);
            detail.putExtra("END", parseStr[3]);
            detail.putExtra("BIO_ID", parseStr[4]);

            detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(detail);


        } else if( parseStr[0].equalsIgnoreCase("RANDOM") ) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("RLAT", parseStr[1]);
            intent.putExtra("RLON", parseStr[2]);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Log.d("PListen:", "can't read message");
            super.onMessageReceived( messageEvent );
        }

    }
}

