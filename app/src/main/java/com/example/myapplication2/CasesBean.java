package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "casesdb")
public class CasesBean
{
    @PrimaryKey @NonNull
    String categoryName;
    String codeName;
    public int totalCases;
    public int totalActive;
    public int totalRecovered;
    public int totalDeaths;

    public int recentCases;
    public int recentRecovered;
    public int recentDeaths;

    String updationTime;


    public CasesBean() {
    }


    @NonNull
    @Override
    public String toString() {
        return categoryName+" "+codeName+" "+updationTime+" "+totalCases+" "+totalActive+" "+totalDeaths+" "+totalRecovered;
    }
}
