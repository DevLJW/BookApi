package com.example.bookapi.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookapi.Model.History

 //쿼리들
@Dao
interface HistoryDao {

  @Insert
  fun insertHistory(history: History)



   @Query("SELECT * FROM history")
    fun getAll() : List<History>




    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword : String)

}