package com.example.myapplication2;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movementdb")
public class MovementTransaction
{
    @PrimaryKey(autoGenerate = true)
    private long time;
    private String mode;
    private String location;
    private String district;
    private String zoneInfo;
    private boolean isContaminated;

    public MovementTransaction()
    {
    }

    public MovementTransaction(long time, String mode, String location, String district, String zoneInfo, boolean isContaminated) {
        this.time = time;
        this.mode = mode;
        this.location = location;
        this.district = district;
        this.zoneInfo = zoneInfo;
        this.isContaminated = isContaminated;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZoneInfo() {
        return zoneInfo;
    }

    public void setZoneInfo(String zoneInfo) {
        this.zoneInfo = zoneInfo;
    }

    public boolean isContaminated() {
        return isContaminated;
    }

    public void setContaminated(boolean contaminated) {
        isContaminated = contaminated;
    }

    @NonNull
    @Override
    public String toString() {
        return " "+time+" "+mode+" "+location+" "+district+" "+zoneInfo+" "+isContaminated+"\n";
    }
}
