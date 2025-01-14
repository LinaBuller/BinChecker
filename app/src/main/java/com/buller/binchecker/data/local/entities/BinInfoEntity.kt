package com.buller.binchecker.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.buller.binchecker.data.local.LocalDatabaseConstants

@Entity(
    tableName = LocalDatabaseConstants.BIN_INFO
)
data class BinInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bin: String,
    @Embedded
    val bank: BankEntity?,
    val brand: String?,
    @Embedded
    val country: CountryEntity?,
    @Embedded
    val number: NumberEntity?,
    val prepaid: Boolean?,
    val scheme: String?,
    val type: String?,
)