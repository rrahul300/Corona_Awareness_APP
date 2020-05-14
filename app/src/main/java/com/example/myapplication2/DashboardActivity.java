package com.example.myapplication2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class DashboardActivity extends AppCompatActivity implements LocationListener {

    String locaitionFromTextBox = "";

    private AppBarConfiguration mAppBarConfiguration;
    MovementDatabase dbInstance =null;
    CasesDatabase coronaCaseDBInstance = null;
    public static long timeTemp =1000;
    protected LocationManager locationManager;
    public Boolean locationObtained = false;
    protected double latitude, longitude;
    static boolean forcePushLocationData = false;
    public static String stateName = "";
    public static String districtName = "";
    public static String localityName = "";
    TextView nationConfirmedCases=null;
    TextView nationActiveCases =null;
    TextView nationDeathCases=null;
    Spinner stateSelectionSpinner = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        latitude = 10;
        longitude = 10;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        final DBController dbController = new DBController();
        dbInstance = dbController.init(DashboardActivity.this);
        coronaCaseDBInstance = CasesDatabase.getInstance(DashboardActivity.this);
        Log.d("rahdesmp","Calling data init");
        BackGroundDataUpdater.init(DashboardActivity.this);
        Log.d("rahdesmp","Finished data init");
        nationConfirmedCases = findViewById(R.id.nationalCoronaDataActive);
        stateSelectionSpinner = findViewById(R.id.selectState);
        java.util.ArrayList<String> strings = new java.util.ArrayList<>();
        strings.add("Mobile") ;
        strings.add("Home");
        strings.add("Work");
       // ArrayAdapter myadapter = (ArrayAdapter) stateSelectionSpinner.getAdapter();
        //myadapter.add("Test1");
        //myadapter.add("Test2");
        //stateSelectionSpinner.setAdapter(myadapter);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Permission not given", Toast.LENGTH_SHORT).show();
            Log.d("onstart", "rahde no permission started");
            return;
        }
        Log.d("onstart", "rahde ready to get location");




        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
                startActivity(intent);
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG.setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Thread thread = null;
            thread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            if(coronaCaseDBInstance!=null && DBController.getData(coronaCaseDBInstance)!=null)
                            {
                                List<CasesBean> dbData = (List<CasesBean>) DBController.getData(coronaCaseDBInstance);
                                if(dbData.get(0)!=null)
                                {
                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1);
                                    ArrayList<String> stateData = new ArrayList<>();
                                    for(int i=0;i<dbData.size();i++)
                                    {
                                        CasesBean nationalData = dbData.get(i);
                                        if(nationalData!=null && nationalData.categoryName.equalsIgnoreCase("Total"))
                                        {
                                            Log.d("dbdata","value of natiobal data is "+nationalData.toString());
                                            //nationConfirmedCases.setText("Cases in India \n "+"Total cases : "+nationalData.totalCases+"\n Recent Cases : "+nationalData.recentCases);
                                            //nationActiveCases.setText("Cases in India \n "+"Total cases : "+nationalData.totalActive);
                                            //nationDeathCases.setText("Cases in India \n "+"Total cases : "+nationalData.totalDeaths+"\n Recent Cases : "+nationalData.recentDeaths);

                                        }
                                        if(nationalData.categoryName!=null && nationalData.categoryName.length()!=0)
                                        {
                                            stateData.add(nationalData.categoryName);
                                        }
                                    }
                                    Log.d("statename"," "+stateData.toString());
                                    spinnerAdapter.addAll(stateData);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    stateSelectionSpinner.setAdapter(spinnerAdapter);
                                    spinnerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        Log.d("rahdes12","onlocal location change trigerred");
        if(forcePushLocationData || (location.getLatitude() != latitude  || location.getLongitude()!=longitude))
        {
            Log.d("rahde","rahde onlocation changed "+latitude+ " "+location.getLatitude()+" lon "+longitude+" "+location.getLongitude());
            locationObtained = true;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(this, "Got location data and value is"+location.getLatitude()+" long -->"+location.getLongitude(), Toast.LENGTH_LONG).show();
            sendPost(location);
            showDataInDashBoard();
            forcePushLocationData = false;
        }
        else
        {
            Log.d("rahdes","location change trigerred");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Toast.makeText(this,"on status change trigerred"+status,Toast.LENGTH_LONG);
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.d("rahde","enabled");
        Toast.makeText(this,"on provider enabled trigerred",Toast.LENGTH_LONG);
        forcePushLocationData = false;
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.d("rahde","disable");
        MovementTransaction movement = new MovementTransaction(System.currentTimeMillis(),"DISABLED",null,null,null,false);
        DBController dbController = new DBController();
        MovementDatabase dbInstance = dbController.init(DashboardActivity.this);
        dbController.putData(dbInstance,movement);
        Toast.makeText(this,"on provider disabled trigerred",Toast.LENGTH_LONG);
    }


    public void showDataInDashBoard()
    {

    }
    public void sendPost(final Location location) {
        Log.i("STATUS", "rahdes in send post");

        Thread thread = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String apiKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtYWlsSWRlbnRpdHkiOiJycmFodWwzMDBAZ21haWwuY29tIn0.lBLJ6yeiupwMG_cGcLU0zrFPOmr0mocxHQ8lLPfZz_E";
                        String urlForPost ="https://data.geoiq.io/dataapis/v1.0/covid/locationcheck";

                        double[][] latLng = new double[1][2];
                        latLng[0][0] = location.getLatitude();
                        latLng[0][1] = location.getLongitude();
                        URL url = new URL(urlForPost);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);


                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        String formJson = "{\"key\":\""+apiKey+"\",\"latlngs\":"+ Arrays.deepToString(latLng)+"}";
                        Log.i("JSON", "rahdes newformar"+formJson);
                        //DataInputStream is = new DataInputStream(conn.getInputStream());

                        os.writeBytes(formJson);

                        os.flush();
                        os.close();

                        Log.i("STATUS", "rahdes"+String.valueOf(conn.getResponseCode()));
                        try(BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine = null;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            System.out.println(response.toString());
                            JSONObject dataFromAPI = new JSONObject(response.toString());
                            Log.i("MSG" , "rahdes output"+response.toString());
                            Log.i("MSG","rahdes "+dataFromAPI.toString());
                            Log.i("MSG","rahdes status code "+dataFromAPI.get("status"));
                            JSONArray contaminatedArea = (JSONArray) dataFromAPI.get("data");
                            JSONObject firstData = (JSONObject) contaminatedArea.get(0);
                            Log.i("MSG","rahdes 123"+firstData.get("districtZoneType"));
                            MovementTransaction movement = new MovementTransaction(System.currentTimeMillis(),"ENABLED",(location.getLatitude()+","+location.getLongitude()),firstData.get("district").toString(),firstData.get("districtZoneType").toString(),(boolean)firstData.get("inContainmentZone"));
                            DBController dbController = new DBController();
                            MovementDatabase dbInstance = dbController.init(DashboardActivity.this);
                            dbController.putData(dbInstance,movement);
                           // sendNotification(location,response.toString());

                            try {
                                String urlForState = "https://apis.mapmyindia.com/advancedmaps/v1/dr7zd56rk2ffdsyry1wxbihbitwfverk/rev_geocode?lat=";
                                urlForState+=location.getLatitude();
                                urlForState+="&lng=";
                                urlForState+=location.getLongitude();
                                Log.d("rahdes","url for geoencoding"+urlForState);
                                URL urlForGeoCoding = new URL(urlForState);
                                HttpURLConnection urlConnection = (HttpURLConnection) urlForGeoCoding.openConnection();
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                ByteArrayOutputStream result = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = in.read(buffer)) != -1) {
                                    result.write(buffer, 0, length);
                                }
                                JSONObject rawJSONResponse =  new JSONObject(result.toString());
                                Log.d("rahdes","geoencoding "+rawJSONResponse.get("responseCode"));
                                JSONObject resultJSON = ((JSONArray)rawJSONResponse.get("results")).getJSONObject(0);
                                districtName = (String) resultJSON.get("district");
                                districtName.replace("District","");
                                districtName.trim();
                                stateName = (String) resultJSON.get("state");
                                localityName = (String) resultJSON.get("locality");
                                //Toast.makeText(DashboardActivity.this, "Location by reverse geocoding is state "+stateName+" district name "+districtName+" locality "+localityName+" lat "+location.getLatitude()+" lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                                Log.d("RAH","state and district is "+ resultJSON.toString());
                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }
                        }

                        conn.disconnect();
                    } catch (Exception e) {
                        Log.i("STATUS", "rahdes"+String.valueOf(e));
                        e.printStackTrace();
                    }
                }
            });
        }

        thread.start();
    }
}
