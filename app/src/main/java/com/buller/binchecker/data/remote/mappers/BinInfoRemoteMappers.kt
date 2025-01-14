package com.buller.binchecker.data.remote.mappers

import com.buller.binchecker.data.remote.dto.BankDto
import com.buller.binchecker.data.remote.dto.BinInfoDto
import com.buller.binchecker.data.remote.dto.CountryDto
import com.buller.binchecker.data.remote.dto.NumberDto
import com.buller.binchecker.domain.models.Bank
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.models.Country
import com.buller.binchecker.domain.models.Number

fun BinInfoDto.toBinInfoDataMap(): BinInfo {
    return BinInfo(
        type = type,
        prepaid = prepaid,
        number = number?.toNumberDataMap(),
        country = country?.toCountryDataMap(),
        brand = brand,
        scheme = scheme,
        bank = bank?.toBankDataMap()
    )
}

fun BankDto.toBankDataMap(): Bank {
    return Bank(city = city, url = url, name = name, phone = phone)
}

fun CountryDto.toCountryDataMap(): Country {
    return Country(
        alpha2 = alpha2,
        currency = currency,
        emoji = emoji,
        longitude = longitude,
        latitude = latitude,
        name = name,
        numeric = numeric
    )
}

fun NumberDto.toNumberDataMap(): Number {
    return Number(length = length, luhn = luhn)
}