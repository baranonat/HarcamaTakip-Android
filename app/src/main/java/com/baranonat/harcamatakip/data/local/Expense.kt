package com.baranonat.harcamatakip.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


    @Entity(tableName = "expense_table")
    data class Expense(

        @PrimaryKey(autoGenerate = true)
        val id:Int = 0,
        val isim:String,
        val  miktar :Double,
        val kategori:String,
        val tarih:Long

)
