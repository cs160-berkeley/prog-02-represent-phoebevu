package com.example.phoebevu.gorepresent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by phoebevu on 3/2/16.
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages


    public boolean isZipcode(String zipString) {
        if (zipString.length() != 5) {
            return false;
        }
        try {
            Integer.parseInt(zipString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        String[] parseStr = value.split("\\|");
        Intent intent = new Intent(this, MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("JSON", parseStr[0]);
        intent.putExtra("COUNTY", parseStr[1]);
        intent.putExtra("STATE", parseStr[2]);
        intent.putExtra("VOTE", parseStr[3]);
        startActivity(intent);


    }
}
