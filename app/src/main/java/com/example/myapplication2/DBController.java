package com.example.myapplication2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBController  {

    public static final String db_name = "db_name";
    public class DBPutBean
    {
        public MovementDatabase movementDatabase;
        public MovementTransaction movementInfo;

        public DBPutBean(MovementDatabase movementDatabase, MovementTransaction movementInfo) {
            this.movementDatabase = movementDatabase;
            this.movementInfo = movementInfo;
        }

    }

    public class CasePutBean
    {
        public CasesDatabase casesDatabase;
        public CasesBean casesBean;

        public CasePutBean(CasesDatabase casesDatabase, CasesBean casesBean) {
            this.casesDatabase = casesDatabase;
            this.casesBean = casesBean;
        }

    }
    static BlockingQueue<DBPutBean> queue = new ArrayBlockingQueue<>(10);
    static BlockingQueue<CasePutBean> caseUpdatequeue = new ArrayBlockingQueue<>(5);

    //SQLiteDatabase mydatabase = openOrCreateDatabase(db_name,MODE_PRIVATE,null);;
    public  MovementDatabase init(Context context)
    {
        try
        {
            Thread consumerThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            Thread.sleep(1000); //Better performance
                            if(queue.size()!=0)
                            {
                                DBPutBean dataToPut = queue.take();
                                if(dataToPut!=null)
                                {
                                    dataToPut.movementDatabase.movementDAO().insertTransactiom(dataToPut.movementInfo);
                                    Log.e("db","data inseretd"+getData(dataToPut.movementDatabase).toString());
                                }
                            }
                            if(caseUpdatequeue.size()!=0)
                            {
                                CasePutBean casesdataToPut = caseUpdatequeue.take();
                                if(casesdataToPut!=null)
                                {
                                    try
                                    {
                                        if(casesdataToPut.casesDatabase.casesDAO().getCoronaData(casesdataToPut.casesBean.categoryName)==null)
                                        {
                                            Log.e("db","dashdata data inseretd"+getData(casesdataToPut.casesDatabase).toString());
                                            casesdataToPut.casesDatabase.casesDAO().insertCoronaData(casesdataToPut.casesBean);
                                        }
                                        else
                                        {
                                            Log.e("db","dashdata data is updated"+getData(casesdataToPut.casesDatabase).toString());
                                            casesdataToPut.casesDatabase.casesDAO().updateCoronaData(casesdataToPut.casesBean);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
            consumerThread.start();

            MovementDatabase dbInstance = MovementDatabase.getInstance(context);
//            getData(dbInstance);
            return dbInstance;
        }
        catch(Exception e)
        {
            Log.e("db","exception db created");
            e.printStackTrace();
            return null;
        }
    }

    public boolean putData(MovementDatabase dbInstance,MovementTransaction movementInfo)
    {
        try
        {
            queue.put(new DBPutBean(dbInstance,movementInfo));
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static Object getData(MovementDatabase dbInstance)
    {
        List<MovementTransaction> dbData = dbInstance.movementDAO().getMovement();
        Log.e("db","db data "+dbData.toString());
        return dbData;
    }

    public boolean putData(CasesDatabase dbInstance,CasesBean casesBean)
    {
        try
        {
            caseUpdatequeue.put(new CasePutBean(dbInstance,casesBean));
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static Object getData(CasesDatabase dbInstance)
    {
        List<CasesBean> dbData = dbInstance.casesDAO().getCoronaData();
        Log.e("db","db data "+dbData.toString());
        return dbData;
    }

}
