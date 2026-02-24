package com.humanjc.myfirstapp.data.repository

import com.humanjc.myfirstapp.data.CounterDataStore
import com.humanjc.myfirstapp.data.local.dao.ClickRecordDao
import com.humanjc.myfirstapp.data.local.entity.ClickRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterRepository @Inject constructor(
    private val dataStore: CounterDataStore,
    private val clickRecordDao: ClickRecordDao
) {
    // DataStore (Settings)
    val countFlow: Flow<Int> = dataStore.countFlow
    val maxCountFlow: Flow<Int> = dataStore.maxCountFlow
    val isDarkModeFlow: Flow<Boolean?> = dataStore.isDarkModeFlow

    suspend fun saveSettings(count: Int, maxCount: Int, isDarkMode: Boolean) {
        // 히스토리는 이제 Room에 저장하므로 빈 리스트 전달 (이후 DataStore에서 히스토리 필드 제거 가능)
        dataStore.saveCounterData(count, maxCount, emptyList(), isDarkMode)
    }

    suspend fun clearSettings() {
        dataStore.clearData()
    }

    // Room (History)
    val historyFlow: Flow<List<ClickRecord>> = clickRecordDao.getAllRecords()

    suspend fun addClickRecord(countValue: Int, actionName: String) {
        clickRecordDao.insertRecord(
            ClickRecord(countValue = countValue, actionName = actionName)
        )
    }

    suspend fun clearHistory() {
        clickRecordDao.deleteAllRecords()
    }
}
