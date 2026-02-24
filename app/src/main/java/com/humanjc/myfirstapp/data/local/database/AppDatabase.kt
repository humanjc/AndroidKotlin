package com.humanjc.myfirstapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.humanjc.myfirstapp.data.local.dao.ClickRecordDao
import com.humanjc.myfirstapp.data.local.entity.ClickRecord

@Database(entities = [ClickRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clickRecordDao(): ClickRecordDao
}
