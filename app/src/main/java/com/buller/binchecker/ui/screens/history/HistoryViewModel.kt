package com.buller.binchecker.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.binchecker.domain.usecases.GetAllBinInfoUseCase
import com.buller.binchecker.ui.screens.history.states.HistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(private val getAllBinInfoUseCase: GetAllBinInfoUseCase) : ViewModel() {

    private val _historyState = MutableStateFlow(HistoryState())
    val historyState get() = _historyState

    init {
        getAllBinInfo()
    }

    private fun getAllBinInfo() {
        viewModelScope.launch {
            val historyList = getAllBinInfoUseCase.invoke()
            _historyState.update {
                it.copy(binInfoList = historyList)
            }
        }
    }
}