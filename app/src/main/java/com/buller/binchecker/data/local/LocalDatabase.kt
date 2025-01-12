package com.buller.binchecker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buller.binchecker.data.local.dao.BinInfoDao
import com.buller.binchecker.data.local.entities.BankEntity
import com.buller.binchecker.data.local.entities.BinInfoEntity
import com.buller.binchecker.data.local.entities.CountryEntity
import com.buller.binchecker.data.local.entities.NumberEntity

@Database(
    entities = [BinInfoEntity::class],
    version = LocalDatabaseConstants.DATABASE_VERSION
)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDao(): BinInfoDao

    companion object {
        fun newInstance(context: Context): LocalDatabase {
            return Room.databaseBuilder(
                context = context,
                LocalDatabase::class.java,
                LocalDatabaseConstants.DATABASE_TABLE_NAME
            ).setJournalMode(JournalMode.TRUNCATE)
                .build()
        }
    }
}