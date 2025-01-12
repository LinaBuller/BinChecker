package com.buller.binchecker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buller.binchecker.data.local.LocalDatabaseConstants


data class BankEntity (
    val bankName: String?,
    val url: String?,
    val phone: String?,
    val city: String?
)