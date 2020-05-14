package com.example.myapplication2.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication2.DBController;
import com.example.myapplication2.MapsActivity;
import com.example.myapplication2.MovementDatabase;
import com.example.myapplication2.MovementTransaction;
import com.example.myapplication2.R;
import com.example.myapplication2.ui.AnalyticsController;

import java.util.HashMap;
import java.util.List;

public class GalleryFragment extends FragmentActivity
{
    public TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_gallery);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("db", "in run");
                    Log.d("db", "db init called");
                    DBController dbController = new DBController();
                    MovementDatabase dbInstance = dbController.init(GalleryFragment.this);

                    if (DBController.getData(dbInstance) != null) {
                        List<MovementTransaction> dbData = (List<MovementTransaction>) DBController.getData(dbInstance);

                        Log.d("e", "setting data to view " + dbData);
                        textView = (TextView) findViewById(R.id.text_gallery);
                        textView.setText(dbData.toString());
                        AnalyticsController collectAnalytics = new AnalyticsController(dbData, System.currentTimeMillis(), System.currentTimeMillis());
                        HashMap<String, Long> zoneWiseSplit = collectAnalytics.calculateZoneSplit();
                        Log.d("e", "Obtained analytics data" + zoneWiseSplit.toString());
                        HashMap<String, Long> trackedVSUntrackedTime = collectAnalytics.getTrackedTimeData();
                        Log.d("e", "Obtained analytics data tracked vs " + trackedVSUntrackedTime.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
