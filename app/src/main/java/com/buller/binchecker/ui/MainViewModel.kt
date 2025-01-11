package com.buller.binchecker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buller.binchecker.domain.Result
import com.buller.binchecker.domain.asResult
import com.buller.binchecker.domain.usecases.GetBinInfoUseCase
import com.buller.binchecker.ui.states.BinInfoState
import com.buller.binchecker.ui.states.InfoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(val getBinInfoUseCase: GetBinInfoUseCase) : ViewModel() {
    private val _infoState = MutableStateFlow(BinInfoState())
    val infoState = _infoState.map(BinInfoState::toUiState).stateIn(
        viewModelScope,
        SharingStarted.Eagerly, _infoState.value.toUiState()
    )

    fun setBin(number: String) {
        _infoState.value = _infoState.value.copy(binNumber = number)
        Log.d("MyLog", _infoState.value.binNumber)
    }

    fun getInfo() {
        viewModelScope.launch {
            Log.d("MyLog", _infoState.value.binNumber)
            val result = getBinInfoUseCase.invoke(_infoState.value.binNumber)
            result.collect { res ->
                _infoState.update {
                    when (res) {
                        is Result.Success -> {
                            it.copy(info = res.data, isLoading = false)
                        }

                        is Result.Error -> {
                            it.copy(info = null, isLoading = false, error = res.exception)
                        }

                        is Result.Loading -> {
                            it.copy(info = null, isLoading = true, error = null)
                        }
                    }
                }
            }
        }
    }

}