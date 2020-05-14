package com.example.myapplication2;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BackGroundDataUpdater
{
    public static CasesDatabase init(Context context)
    {
        final CasesDatabase dbInstance = CasesDatabase.getInstance(context);

        try
        {
            Thread dbUpaterThreadForNationStateData = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    while (true) {
                        try {
                            Log.d("dbupd", "rahdes db updation trigerred");
                            if (dbInstance != null)
                            {
                                Log.i("STATUS", "rahdes backgroup updateion");
                                URL url = new URL("https://api.covid19india.org/data.json");
                                try {
                                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = in.read(buffer)) != -1) {
                                        result.write(buffer, 0, length);
                                    }
                                    JSONObject rawJSONResponse =  new JSONObject(result.toString());
                                    JSONArray stateWiseData = (JSONArray) rawJSONResponse.get("statewise");
                                    for(int i=0;i<stateWiseData.length();i++)
                                    {
                                        JSONObject stateData = (JSONObject) stateWiseData.get(i);
                                        CasesBean stateDataToInsert = new CasesBean();

                                        stateDataToInsert.categoryName = (String) stateData.get("state");
                                        stateDataToInsert.codeName = (String) stateData.get("statecode");

                                        stateDataToInsert.totalActive = Integer.parseInt(stateData.get("active").toString());

                                        stateDataToInsert.totalCases = Integer.parseInt(stateData.get("confirmed").toString());
                                        stateDataToInsert.recentCases = Integer.parseInt(stateData.get("deltaconfirmed").toString());

                                        stateDataToInsert.totalRecovered = Integer.parseInt(stateData.get("recovered").toString());

                                        stateDataToInsert.recentRecovered = Integer.parseInt(stateData.get("deltarecovered").toString());
                                        stateDataToInsert.totalActive = Integer.parseInt(stateData.get("deaths").toString());
                                        stateDataToInsert.recentDeaths = Integer.parseInt(stateData.get("deltadeaths").toString());
                                        stateDataToInsert.updationTime = (String) stateData.get("lastupdatedtime");

                                        DBController dbController = new DBController();

                                        dbController.putData(dbInstance, stateDataToInsert);

                                        Log.d("MPRAH","MPRAH"+ stateDataToInsert.toString());
                                    }
                                }
                                catch (Exception e )
                                {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(300000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dbUpaterThreadForNationStateData.start();
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }

        return dbInstance;
    }


}
