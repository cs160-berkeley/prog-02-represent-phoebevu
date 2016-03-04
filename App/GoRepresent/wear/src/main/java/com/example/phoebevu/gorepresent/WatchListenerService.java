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
    private static final String ZIP_CODE = "/94709";

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
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        if( isZipcode(value) ) {

            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("ZIPCODE", value);
            Log.d("T", "about to start watch MainActivity with ZIPCODE: " + value );
            Log.i("INFO: ", value);
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
