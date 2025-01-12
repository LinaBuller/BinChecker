package com.buller.binchecker.ui.screens.history.states

import com.buller.binchecker.domain.models.BinInfo

data class HistoryState(val binInfoList: List<BinInfo> = emptyList())