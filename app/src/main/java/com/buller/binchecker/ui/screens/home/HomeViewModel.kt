package com.buller.binchecker.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.binchecker.domain.models.BinInfo
import com.buller.binchecker.domain.utils.Result
import com.buller.binchecker.domain.usecases.GetBinInfoUseCase
import com.buller.binchecker.domain.usecases.SetBinInfoToDatabaseUseCase
import com.buller.binchecker.ui.screens.home.states.BinInfoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBinInfoUseCase: GetBinInfoUseCase,
    private val setBinInfoToDatabaseUseCase: SetBinInfoToDatabaseUseCase
) : ViewModel() {
    private val _infoState = MutableStateFlow(BinInfoState())
    val infoState = _infoState.map(BinInfoState::toUiState).stateIn(
        viewModelScope, SharingStarted.Eagerly, _infoState.value.toUiState()
    )

    fun setBin(number: String) {
        _infoState.value = _infoState.value.copy(binNumber = number)
    }

    fun getInfo() {
        viewModelScope.launch {
            val result = getBinInfoUseCase.invoke(_infoState.value.binNumber)
            result.collect { res ->
                _infoState.update {
                    when (res) {
                        is Result.Success -> {
                            saveToHistory(_infoState.value.binNumber,res.data)
                            it.copy(info = res.data, isLoading = false)
                        }

                        is Result.Error -> {
                            it.copy(info = null, isLoading = false, error = res.exception)
                        }

                        is Result.Loading -> {
                            it.copy(info = null, isLoading = true, error = null)
                        }

                        else -> {
                            it.copy(info = null, isLoading = false, error = null)
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveToHistory(bin: String, binInfo: BinInfo) {
        Log.d("MyLog", "set item to database")
        setBinInfoToDatabaseUseCase.invoke(
            bin, binInfo
        )
    }
}