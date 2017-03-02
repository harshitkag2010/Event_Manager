package com.example.sag14.application01;

/**
 * Created by sag14 on 30/11/16.
 */

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Harshit on 11/20/2016.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN= "REG_TOKEN";

    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG","coming");
        Log.d(REG_TOKEN,recent_token);
        Log.e("TAG","coming1");
        Toast.makeText(MyFirebaseInstanceIdService.this, recent_token, Toast.LENGTH_SHORT).show();
        // registerToken(token);
    }


}