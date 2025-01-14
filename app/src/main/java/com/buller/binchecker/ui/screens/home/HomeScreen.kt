package com.buller.binchecker.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivities
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.binchecker.R
import com.buller.binchecker.domain.models.Bank
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.models.Country
import com.buller.binchecker.domain.models.Number
import com.buller.binchecker.ui.screens.home.states.InfoState
import org.koin.androidx.compose.koinViewModel
import retrofit2.HttpException

@Composable
fun HomeRoute(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.infoState.collectAsStateWithLifecycle()
    HomeScreen(modifier = modifier,
        state = state,
        onBinChanged = { bin -> viewModel.setBin(bin) },
        getInfo = { viewModel.getInfo() })
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: InfoState,
    onBinChanged: (String) -> Unit,
    getInfo: () -> Unit
) {
    var bin by rememberSaveable { mutableStateOf(state.binNumber) }
    var showError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        UpdateZone(state = state, onRefreshInfo = getInfo)
        Spacer(modifier = Modifier.height(32.dp))
        NumberInputField(
            value = bin,
            onValueChange = {
                bin = it
                onBinChanged.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            isError = showError,
            onIsErrorChange = {
                showError = it
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            showError = bin.isNotEmpty() && (bin.length != 6 && bin.length != 8)
            if (!showError){
                getInfo.invoke()
            }
        }, enabled = bin.isNotEmpty())
        {
            Text(text = stringResource(R.string.get_info))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun UpdateZone(modifier: Modifier = Modifier, state: InfoState, onRefreshInfo: () -> Unit) {
    LoadingContent(
        empty = when (state) {
            is InfoState.HasInfo -> false
            is InfoState.NoInfo -> state.isLoading
        },
        content = {
            when (state) {
                is InfoState.HasInfo -> {
                    if (state.info.number == null) {
                        Text(text = stringResource(R.string.not_found_bin))
                    } else {
                        BinInfoDisplay(modifier = Modifier, binInfo = state.info)
                    }
                }

                is InfoState.NoInfo -> {
                    if (state.error != null) {
                        val errorMessage = when (val error = state.error) {
                            is HttpException -> {
                                when (error.code()) {
                                    404 -> stringResource(R.string.error_404)
                                    429 -> stringResource(R.string.error_429)
                                    else -> stringResource(R.string.error_some)
                                }
                            }

                            else -> stringResource(R.string.error_some)
                        }
                        TextButton(
                            onClick = onRefreshInfo, modifier = modifier
                        ) {
                            Text(
                                text = "${errorMessage}\n${stringResource(R.string.tap_to_load_content)}",
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.introduce_1),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.introduce_2),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier,
        emptyContent = { CircularProgressIndicator() },
        loading = state.isLoading,
        onRefreshInfo = onRefreshInfo
    )
}


@Composable
fun BinInfoDisplay(modifier: Modifier = Modifier, binInfo: BinInfo) {
    Column(modifier = modifier.fillMaxWidth()) {
        BinCard(binInfo = binInfo)
        Spacer(modifier = Modifier.height(8.dp))
        val bank = binInfo.bank
        BankPart(name = bank?.name, city = bank?.city, phone = bank?.phone, url = bank?.url)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    empty: Boolean,
    loading: Boolean,
    onRefreshInfo: () -> Unit,
    content: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        PullToRefreshBox(
            isRefreshing = loading, onRefresh = { onRefreshInfo.invoke() }, modifier = modifier
        ) {
            content()
        }
    }
}

@Composable
fun BinCard(binInfo: BinInfo) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(end = 16.dp)) {

            InfoItem(
                title = stringResource(R.string.scheme),
                value = if (!binInfo.scheme.isNullOrBlank()) {
                    binInfo.scheme
                } else {
                    stringResource(R.string.unknown)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoItem(
                title = stringResource(R.string.brand),
                value = if (!binInfo.brand.isNullOrBlank()) {
                    binInfo.brand
                } else {
                    stringResource(R.string.unknown)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                val length = binInfo.number?.length
                val luhn = binInfo.number?.luhn
                InfoItem(
                    title = stringResource(R.string.length),
                    value = length?.toString() ?: stringResource(R.string.unknown)
                )
                Spacer(modifier = Modifier.width(16.dp))
                InfoItem(
                    title = stringResource(R.string.luhn),
                    value = if (luhn != null) {
                        if (luhn) {
                            stringResource(R.string.yes)
                        } else {
                            stringResource(R.string.no)
                        }
                    } else {
                        stringResource(R.string.unknown)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CountryPart(binInfo)
        }

        Column {
            InfoItem(
                title = stringResource(R.string.type_card),
                value = if (!binInfo.type.isNullOrBlank()) {
                    binInfo.type
                } else {
                    stringResource(R.string.unknown)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoItem(
                title = stringResource(R.string.prepaid), value = if (binInfo.prepaid != null) {
                    if (binInfo.prepaid) {
                        stringResource(R.string.yes)
                    } else {
                        stringResource(R.string.no)
                    }
                } else {
                    stringResource(R.string.unknown)
                }
            )
        }
    }
}

@Composable
fun CountryPart(binInfo: BinInfo) {

    val context = LocalContext.current

    fun openMap(context: Context, latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    Column {
        InfoItem(
            title = stringResource(R.string.country),
            value = if (binInfo.country != null) {
                "${binInfo.country.name ?: stringResource(R.string.unknown)} ${binInfo.country.emoji}"
            } else {
                stringResource(R.string.unknown)
            }
        )

        LinkTextWithIcon(text = if (binInfo.country != null) {
            "${stringResource(R.string.latitude)}: ${
                binInfo.country.latitude ?: stringResource(
                    R.string.unknown
                )
            } ${stringResource(R.string.longitude)}: ${
                binInfo.country.longitude ?: stringResource(R.string.unknown)
            }"
        } else {
            stringResource(R.string.unknown)
        },
            label = "",
            icon = Icons.Rounded.Place,
            contentDescription = "Open map",
            onClick = {
                val lat = binInfo.country?.latitude
                val long = binInfo.country?.longitude
                if (lat != null && long != null) {
                    openMap(context, latitude = lat, longitude = long)
                }
            })
    }
}


@Composable
fun BankPart(name: String?, city: String?, phone: String?, url: String?) {
    val context = LocalContext.current

    fun openUrl(url: String?) {
        if (!url.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivities(context, arrayOf(intent))
        }
    }

    fun makePhoneCall(phoneNumber: String?) {
        if (!phoneNumber.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivities(context, arrayOf(intent))
        }
    }

    Column {
        InfoItem(
            title = stringResource(R.string.bank),
            value = name ?: stringResource(R.string.unknown)
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (!city.isNullOrBlank()) {
            Text(text = "${stringResource(R.string.city)}: $city", style = TextStyle.Default)
        } else {
            Text(
                text = "${stringResource(R.string.city)}: ${stringResource(R.string.unknown)}",
                style = TextStyle.Default
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinkTextWithIcon(text = url, label = stringResource(R.string.site),
            contentDescription = stringResource(R.string.open_bank_site),
            icon = Icons.Rounded.AccountBox,
            onClick = {
                openUrl(url)
            })
        Spacer(modifier = Modifier.height(16.dp))

        LinkTextWithIcon(text = phone, label = stringResource(R.string.phone),
            contentDescription = stringResource(R.string.call_to_bank),
            icon = Icons.Rounded.Call,
            onClick = {
                makePhoneCall(phone)
            })
    }

}

@Composable
fun LinkTextWithIcon(
    label: String,
    text: String?, contentDescription: String, icon: ImageVector, onClick: () -> Unit
) {
    Row(modifier = Modifier.clickable {
        onClick.invoke()
    }, verticalAlignment = Alignment.CenterVertically) {
        val annotatedString = if (!text.isNullOrBlank()) {
            buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )
                append(text)
                pop()
            }
        } else {
            buildAnnotatedString {
                append(stringResource(R.string.unknown))
            }
        }
        if (label.isNotBlank()) {
            Text(text = "$label: ", style = TextStyle.Default)
        }
        Text(text = annotatedString, style = TextStyle.Default)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.clickable {
                onClick()
            })
    }
}

@Composable
fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onIsErrorChange: (Boolean) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val borderColor = if (isError) Color.Red else Color.Gray
    val textColor = if (isError) Color.Red else Color.Gray

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = {
                if (it.all { char -> char.isDigit() } && (it.length <= 8)) {
                    onValueChange(it)
                    onIsErrorChange(false)
                }
            },
            label = { Text(text = stringResource(R.string.check_bin)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            modifier = modifier
                .fillMaxWidth()
                .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
            textStyle = TextStyle(
                color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Medium
            ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = textColor,
                focusedTextColor = textColor,
                errorIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            }
        )
        if (isError) {
            Text(
                text = stringResource(R.string.wrong_str),
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun InfoItem(title: String, value: String) {
    Column {
        Text(text = title.uppercase(), modifier = Modifier.padding(bottom = 4.dp))
        Text(text = value, style = TextStyle(fontWeight = FontWeight.Bold))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val state = InfoState.HasInfo(
        binNumber = "23422221", error = null, isLoading = false, info = BinInfo(
            bin = "21312234",
            Bank(
                city = "Rostov-on-Don", name = "VTB", phone = "+34029383342", url = "https://vtb.ru"
            ),
            brand = "visa",
            country = Country(
                alpha2 = "ad",
                currency = "",
                emoji = "",
                name = "Russia",
                latitude = 23.0,
                longitude = 43.0,
                numeric = ""
            ),
            number = Number(length = 12, luhn = true),
            prepaid = true,
            type = "Debit",
            scheme = "Visa"
        )
    )
    HomeScreen(state = state, onBinChanged = {}, getInfo = {})
}