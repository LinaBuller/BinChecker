package com.buller.binchecker.models

data class BinInfo(
    val bank: Bank? = null,
    val brand: String? = "",
    val country: Country? = null,
    val number: Number? = null,
    val prepaid: Boolean? = false,
    val scheme: String? = "",
    val type: String? = ""
)