package com.example.myapplication2;



import androidx.fragment.app.FragmentActivity;



import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication2.ui.AnalyticsController;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity  {


    protected boolean gps_enabled, network_enabled;
    public TextView textView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("onstart", "rahde onstart started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
 //       FirebaseApp.initializeApp(this);
        textView = (TextView) findViewById(R.id.viewCommuteData);
        Toast.makeText(this, "deszsfd", Toast.LENGTH_SHORT).show();






    }




    public static void sendNotification(Location location,String apiResponse)
    {
       /* Log.e("notify","rahde push notifcation called");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("test");
        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        mBuilder.setContentText("Hi, dummy notification"+location.getLatitude()+"long "+location.getLongitude()+"response from api "+apiResponse);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123,mBuilder.build());
        Log.e("notify","rahde push notifcation send"); */
    }



}
