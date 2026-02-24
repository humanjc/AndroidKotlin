package com.humanjc.myfirstapp.di

import android.content.Context
import androidx.room.Room
import com.humanjc.myfirstapp.data.CounterDataStore
import com.humanjc.myfirstapp.data.local.dao.ClickRecordDao
import com.humanjc.myfirstapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCounterDataStore(
        @ApplicationContext context: Context
    ): CounterDataStore {
        return CounterDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_first_app_db"
        ).build()
    }

    @Provides
    fun provideClickRecordDao(database: AppDatabase): ClickRecordDao {
        return database.clickRecordDao()
    }
}
