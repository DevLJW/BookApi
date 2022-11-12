package com.example.bookapi

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.bookapi.Dao.HistoryDao
import com.example.bookapi.Model.History


@Database

    (entities = [History::class], version = 1)


    abstract class Database  : RoomDatabase(){
    abstract fun historyDao() : HistoryDao


}