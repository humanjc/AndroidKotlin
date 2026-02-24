package com.humanjc.myfirstapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.humanjc.myfirstapp.data.local.entity.ClickRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ClickRecordDao {
    @Query("SELECT * FROM click_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ClickRecord>>

    @Insert
    suspend fun insertRecord(record: ClickRecord)

    @Query("DELETE FROM click_records")
    suspend fun deleteAllRecords()
}
