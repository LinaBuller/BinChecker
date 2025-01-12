package com.buller.binchecker.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.content.ContextCompat.startActivities
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.binchecker.R
import com.buller.binchecker.domain.models.Bank
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.models.Country
import com.buller.binchecker.domain.models.Number
import com.buller.binchecker.ui.screens.home.states.InfoState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.infoState.collectAsStateWithLifecycle()
    HomeScreen(modifier = modifier,
        state = state,
        onBinChanged = { bin -> viewModel.setBin(bin) },
        getInfo = {
            Log.d("MyLog", "click button get info")
            viewModel.getInfo()
        })
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: InfoState,
    onBinChanged: (String) -> Unit,
    getInfo: () -> Unit
) {
    var bin by remember { mutableStateOf(state.binNumber) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        UpdateZone(state = state, onRefreshInfo = getInfo)
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = bin,
            onValueChange = {
                bin = it
                onBinChanged.invoke(it)
            },
            label = { Text(text = stringResource(R.string.check_bin)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            getInfo.invoke()
        }) {
            Text(text = stringResource(R.string.get_info))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun UpdateZone(
    modifier: Modifier = Modifier, state: InfoState, onRefreshInfo: () -> Unit
) {
    LoadingContent(
        empty = when (state) {
            is InfoState.HasInfo -> false
            is InfoState.NoInfo -> state.isLoading
        },
        content = {
            when (state) {
                is InfoState.HasInfo -> {
                    BinInfoDisplay(modifier = Modifier, binInfo = state.info)
                }

                is InfoState.NoInfo -> {
                    TextButton(
                        onClick = onRefreshInfo, modifier = modifier
                    ) {
                        Text(
                            text = stringResource(R.string.home_tap_to_load_content),
                            textAlign = TextAlign.Center
                        )
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
fun BinInfoDisplay(modifier: Modifier = Modifier,binInfo: BinInfo?) {
    Column(modifier = modifier.fillMaxWidth()) {
        BinCard(binInfo = binInfo)
        Spacer(modifier = Modifier.height(16.dp))
        binInfo?.bank?.let {
            BankCard(name = it.name, city = it.city, phone = it.phone, url = it.url)
        }
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
fun BinCard(binInfo: BinInfo?) {
    val context = LocalContext.current

    fun openMap(context: Context, latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(end = 16.dp)) {
            InfoItem(
                title = stringResource(R.string.scheme),
                value = binInfo?.scheme ?: stringResource(R.string.unknown)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoItem(
                title = stringResource(R.string.brand),
                value = binInfo?.brand ?: stringResource(R.string.unknown)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                val length = binInfo?.number?.length
                val luhn = binInfo?.number?.luhn
                InfoItem(
                    title = stringResource(R.string.length),
                    value = length?.toString() ?: stringResource(R.string.unknown)
                )
                Spacer(modifier = Modifier.width(16.dp))
                InfoItem(
                    title = stringResource(R.string.luhn),
                    value = luhn.toString() ?: stringResource(R.string.unknown)
                )
            }
        }
        Column {
            InfoItem(
                title = stringResource(R.string.type_card),
                value = binInfo?.type ?: stringResource(R.string.unknown)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoItem(
                title = stringResource(R.string.prepaid), value = if (binInfo?.prepaid != null) {
                    if (binInfo.prepaid) {
                        stringResource(R.string.yes)
                    } else {
                        stringResource(R.string.no)
                    }
                } else {
                    stringResource(R.string.unknown)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))


            Column {
                InfoItem(
                    title = stringResource(R.string.country),
                    value = "${binInfo?.country?.name ?: stringResource(R.string.unknown)} ${binInfo?.country?.emoji}"
                )

                LinkTextWithIcon(text = "${stringResource(R.string.latitude)}: ${
                    binInfo?.country?.latitude ?: stringResource(
                        R.string.unknown
                    )
                } " + "${stringResource(R.string.longitude)}: ${
                    binInfo?.country?.longitude ?: stringResource(
                        R.string.unknown
                    )
                }", icon = Icons.Rounded.Place, contentDescription = "Open map", onClick = {
                    val lat = binInfo?.country?.latitude
                    val long = binInfo?.country?.longitude
                    if (lat != null && long != null) {
                        openMap(context, latitude = lat, longitude = long)
                    }
                })
            }
        }
    }
}

@Composable
fun BankCard(name: String?, city: String?, phone: String?, url: String?) {
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

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                InfoItem(
                    title = stringResource(R.string.bank),
                    value = name ?: stringResource(R.string.unknown)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = city ?: "")
            }
            Spacer(modifier = Modifier.height(16.dp))

            LinkTextWithIcon(text = url ?: "Url not found",
                contentDescription = "Open bank's web site",
                icon = Icons.Rounded.AccountBox,
                onClick = {
                    openUrl(url)
                })
            Spacer(modifier = Modifier.height(16.dp))

            LinkTextWithIcon(text = phone ?: "Phone not found",
                contentDescription = "Call to bank",
                icon = Icons.Rounded.Call,
                onClick = {
                    makePhoneCall(phone)
                })
        }
    }
}

@Composable
fun LinkTextWithIcon(
    text: String?, contentDescription: String, icon: ImageVector, onClick: () -> Unit
) {
    if (!text.isNullOrBlank()) {
        Row(modifier = Modifier.clickable {
            onClick.invoke()
        }, verticalAlignment = Alignment.CenterVertically) {
            val annotatedString = buildAnnotatedString {
                pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
                append(text)
                pop()
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
        binNumber = "234221", error = null, isLoading = false, info = BinInfo(
            Bank(
                city = "Rostov-on-Don", name = "VTB", phone = "+340293842", url = "https://vtb.ru"
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