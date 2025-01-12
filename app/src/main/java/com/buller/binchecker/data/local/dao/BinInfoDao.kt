package com.buller.binchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.buller.binchecker.data.local.LocalDatabaseConstants
import com.buller.binchecker.data.local.entities.BinInfoEntity

@Dao
interface BinInfoDao {
    @Transaction
    @Query("SELECT * FROM ${LocalDatabaseConstants.BIN_INFO}")
    fun getAll(): List<BinInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(binInfo: BinInfoEntity) : Long

    @Update
    suspend fun update(binInfo: BinInfoEntity)

    @Delete
    suspend fun delete(binInfo: BinInfoEntity)
}

