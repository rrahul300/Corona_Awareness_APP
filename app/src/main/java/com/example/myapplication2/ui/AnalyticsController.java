package com.example.myapplication2.ui;

import com.example.myapplication2.DBController;
import com.example.myapplication2.DashboardActivity;
import com.example.myapplication2.MovementTransaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AnalyticsController
{
    List<MovementTransaction> commuteData;
    long startTime;
    long endTime;
    long duration;
    long trackedTime=0;
    long unTrackedTime=0;
    HashMap<String,Long> zoneWiseTimeSpent = new HashMap<>();
    long timeSpentInContaminated;
    HashSet<String> districtsTravelled;
    int numberOfDisabledTime;

    public AnalyticsController(List<MovementTransaction> commuteData, long startTime, long endTime)
    {
        this.commuteData = commuteData;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime-startTime;
    }

    public HashMap<String, Long> calculateZoneSplit()
    {
        boolean stopProcess = false;
        for(int i=0;i<commuteData.size();i++)
        {
            MovementTransaction current = commuteData.get(i);
            if(current!=null)
            {
                if(startTime<current.getTime() && endTime!=0 && endTime<current.getTime())
                {
                    unTrackedTime+=(endTime-startTime);
                    break;
                }
                else if(startTime<current.getTime())
                {
                    unTrackedTime+=(current.getTime()-startTime);
                }

                long nextElementTime = System.currentTimeMillis();
                if(i<(commuteData.size()-1))
                {
                    nextElementTime = commuteData.get(i+1).getTime();
                    if(nextElementTime>endTime)
                    {
                        nextElementTime = endTime;
                        stopProcess = true;
                    }
                }
                else
                {
                    if(endTime==0)
                    {
                        nextElementTime = System.currentTimeMillis();
                    }
                    else
                    {
                        nextElementTime = endTime;
                    }
                    DashboardActivity.timeTemp+=1000;
                    //tEndTime = DashboardActivity.timeTemp;
                    stopProcess = true;
                }
                String gpsMode = current.getMode();
                if(gpsMode.equalsIgnoreCase("DISABLED"))
                {
                    unTrackedTime+=(nextElementTime-current.getTime());
                }
                else
                {
                    String currentZone = current.getZoneInfo();
                    checkAndPutZoneData(zoneWiseTimeSpent,currentZone,(nextElementTime-current.getTime()));
                    trackedTime+=(nextElementTime-current.getTime());
                }
            }

            if(stopProcess)
            {
                break;
            }
        }
        return zoneWiseTimeSpent;
    }

    public HashMap<String,Long> getTrackedTimeData()
    {
        HashMap<String,Long> trackedUnTracked =  new HashMap<>();
        trackedUnTracked.put("Time_tracked",trackedTime);
        trackedUnTracked.put("Time_untracked",unTrackedTime);
        return  trackedUnTracked;
    }

    public static void checkAndPutZoneData(Map<String,Long> zoneWiseTimeSpent,String zoneName, long duration)
    {
        if(zoneWiseTimeSpent.containsKey(zoneName) && zoneWiseTimeSpent.get(zoneName)!=0)
        {
            duration+= zoneWiseTimeSpent.get(zoneName);
        }
        zoneWiseTimeSpent.put(zoneName,duration);
    }


}
