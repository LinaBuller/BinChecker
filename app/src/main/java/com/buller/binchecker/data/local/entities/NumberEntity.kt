package com.buller.binchecker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buller.binchecker.data.local.LocalDatabaseConstants

data class NumberEntity (
    val length: Int? = null,
    val luhn: Boolean?=null
)