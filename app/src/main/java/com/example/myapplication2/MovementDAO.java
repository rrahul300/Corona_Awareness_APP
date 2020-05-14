package com.example.myapplication2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface MovementDAO
{
    @Query("select * from movementdb")
    List<MovementTransaction> getMovement();

    @Insert
    void insertTransactiom(MovementTransaction movementTransaction);
}
