package com.buller.binchecker.data.local.mappers

import androidx.room.Transaction
import com.buller.binchecker.data.local.dao.BinInfoDao
import com.buller.binchecker.data.local.entities.BankEntity
import com.buller.binchecker.data.local.entities.BinInfoEntity
import com.buller.binchecker.data.local.entities.CountryEntity
import com.buller.binchecker.data.local.entities.NumberEntity
import com.buller.binchecker.domain.models.Bank
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.models.Country
import com.buller.binchecker.domain.models.Number
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun BinInfo.insert(
    bin: String, binInfoDao: BinInfoDao
): Long {
    return withContext(Dispatchers.IO) {
        insertInternal(bin, binInfoDao)
    }
}

@Transaction
private suspend fun BinInfo.insertInternal(
    bin: String, binInfoDao: BinInfoDao
): Long {
    val binInfoEntity = this.toBinInfoEntity(bin)
    return binInfoDao.insert(binInfoEntity)
}


fun BinInfo.toBinInfoEntity(bin: String): BinInfoEntity = BinInfoEntity(
    bank = this.bank?.toBankEntity(),
    brand = this.brand,
    country = this.country?.toCountEntity(),
    number = this.number?.toNumberEntity(),
    prepaid = this.prepaid,
    scheme = this.scheme,
    type = this.type,
    bin = bin
)

fun Bank.toBankEntity(): BankEntity =
    BankEntity(city = this.city, url = this.url, phone = this.phone, bankName = this.name)

fun Country.toCountEntity(): CountryEntity = CountryEntity(
    name = this.name,
    numeric = this.numeric,
    currency = this.currency,
    alpha2 = this.alpha2,
    emoji = this.emoji,
    latitude = this.latitude,
    longitude = this.longitude
)

fun Number.toNumberEntity(): NumberEntity = NumberEntity(
    length = this.length, luhn = this.luhn
)

fun BinInfoEntity.toBinInfo(): BinInfo = BinInfo(
    bank = this.bank?.toBank(),
    brand = this.brand,
    country = this.country?.toCountry(),
    number = this.number?.toNumber(),
    prepaid = this.prepaid,
    scheme = this.scheme,
    type = this.type,
    bin = this.bin
)

fun BankEntity.toBank(): Bank = Bank(
    name = this.bankName ?: "",
    url = this.url ?: "",
    phone = this.phone ?: "",
    city = this.city ?: ""
)

fun CountryEntity.toCountry(): Country = Country(
    numeric = this.numeric ?: "",
    alpha2 = this.alpha2 ?: "",
    name = this.name ?: "",
    emoji = this.emoji ?: "",
    currency = this.currency ?: "",
    longitude = this.longitude ?: 0.0,
    latitude = this.latitude ?: 0.0
)

fun NumberEntity.toNumber(): Number = Number(
    length = this.length ?: 0, luhn = this.luhn ?: false
)