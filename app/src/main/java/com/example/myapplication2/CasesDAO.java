package com.example.myapplication2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface CasesDAO
{
    @Query("select * from casesdb")
    List<CasesBean> getCoronaData();

    @Query("SELECT * FROM casesdb WHERE categoryName = :categoryName ")
    CasesBean getCoronaData(String categoryName);

    @Insert
    void insertCoronaData(CasesBean casesBean);

    @Update
    void updateCoronaData(CasesBean casesBean);
}
