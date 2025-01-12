package com.buller.binchecker.data.remote.dto

data class BinInfoDto(
    val bank: BankDto? = null,
    val brand: String? = "",
    val country: CountryDto? = null,
    val number: NumberDto? = null,
    val prepaid: Boolean? = false,
    val scheme: String? = "",
    val type: String? = ""
)