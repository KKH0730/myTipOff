package com.life.myTipOff.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.life.myTipOff.model.Report;

import java.util.List;

@Dao
public interface ReportDao {

    @Query("SELECT * FROM report")
    List<Report> getAll();

    @Insert()
    void insert(Report report);
}
