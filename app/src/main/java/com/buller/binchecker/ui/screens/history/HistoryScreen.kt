package com.buller.binchecker.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buller.binchecker.R
import com.buller.binchecker.domain.models.Bank
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.models.Country
import com.buller.binchecker.domain.models.Number
import com.buller.binchecker.ui.screens.history.states.HistoryState
import com.buller.binchecker.ui.screens.home.BinInfoDisplay
import com.buller.binchecker.ui.screens.home.HomeViewModel
import com.buller.binchecker.ui.screens.home.InfoItem
import kotlinx.coroutines.flow.combine
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoute(modifier: Modifier = Modifier) {
    val viewModel: HistoryViewModel = koinViewModel()
    val state by viewModel.historyState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getAllBinInfo()
    }
    HistoryScreen(state = state, modifier = modifier)
}

@Composable
fun HistoryScreen(modifier: Modifier = Modifier, state: HistoryState) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(state.binInfoList) { binInfo ->
            BinInfoListItem(binInfo = binInfo)
        }
    }
}

@Composable
fun BinInfoListItem(binInfo: BinInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column() {
            Text(
                modifier = Modifier.padding(8.dp),
                text = binInfo.bin.toString()
            )
            if (!binInfo.scheme.isNullOrBlank() || !binInfo.type.isNullOrBlank()) {
                BinInfoDisplay(modifier = Modifier.padding(8.dp), binInfo = binInfo)
            } else {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.not_found_bin)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BinInfoListItemPreview() {
    val binInfo = BinInfo(
        bin = "34523453",
        bank = Bank(
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
    Column {
        Text(modifier = Modifier.padding(8.dp), text = binInfo.bin.toString())
        BinInfoDisplay(modifier = Modifier.padding(8.dp), binInfo = binInfo)
    }
}