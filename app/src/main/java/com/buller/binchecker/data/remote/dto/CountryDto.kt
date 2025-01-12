package com.buller.binchecker.data.remote.dto

data class CountryDto(
    val alpha2: String?,
    val currency: String?,
    val emoji: String?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val numeric: String?
)