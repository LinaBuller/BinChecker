package com.buller.binchecker.domain.models

data class Country(
    val alpha2: String?,
    val currency: String?,
    val emoji: String?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val numeric: String?
)