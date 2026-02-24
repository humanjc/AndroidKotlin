package com.humanjc.myfirstapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "click_records")
data class ClickRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val countValue: Int,
    val actionName: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    // UI에 보여주기 위한 포맷팅 함수
    fun toDisplayString(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeStr = sdf.format(Date(timestamp))
        return "[$timeStr] $actionName: ${countValue}회"
    }
}
