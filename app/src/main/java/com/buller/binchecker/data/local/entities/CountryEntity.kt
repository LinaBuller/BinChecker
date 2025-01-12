package com.buller.binchecker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buller.binchecker.data.local.LocalDatabaseConstants

class CountryEntity (
    val alpha2: String?,
    val currency: String?,
    val emoji: String?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val numeric: String?
)