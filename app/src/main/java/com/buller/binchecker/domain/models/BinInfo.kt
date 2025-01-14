package com.buller.binchecker.domain.models

data class BinInfo(
    val bin: String? = "",
    val bank: Bank? = null,
    val brand: String? = "",
    val country: Country? = null,
    val number: Number? = null,
    val prepaid: Boolean? = null,
    val scheme: String? = "",
    val type: String? = ""
)