package com.example.bookapi.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//테이블
@Entity
data class History(


    @PrimaryKey val uid : Int?,
    @ColumnInfo(name = "keyword") val keyword : String?



)