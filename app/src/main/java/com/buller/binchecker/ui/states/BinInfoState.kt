package com.buller.binchecker.ui.states

import com.buller.binchecker.models.BinInfo
import java.lang.Error


sealed interface InfoState {
    val error: Throwable?
    val isLoading: Boolean
    val binNumber: String

    data class NoInfo(
        override val binNumber: String = "",
        override val isLoading: Boolean,
        override val error: Throwable?
    ) : InfoState

    data class HasInfo(
        override val binNumber: String = "",
        val info: BinInfo? = null,
        override val isLoading: Boolean,
        override val error: Throwable?
    ) : InfoState
}

data class BinInfoState(
    val binNumber: String = "",
    val info: BinInfo? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) {
    fun toUiState(): InfoState = if (info == null) {
        InfoState.NoInfo(isLoading = isLoading, error = error)
    } else {
        InfoState.HasInfo(binNumber = binNumber, info = info, isLoading = isLoading, error = error)
    }
}