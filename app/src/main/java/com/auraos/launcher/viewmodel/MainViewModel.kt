package com.auraos.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auraos.launcher.data.model.IntentHistoryItem
import com.auraos.launcher.data.model.IntentType
import com.auraos.launcher.engine.IntentEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the Aura OS home screen.
 */
data class AuraUiState(
    val inputText: String = "",
    val isProcessing: Boolean = false,
    val intentHistory: List<IntentHistoryItem> = emptyList(),
    val currentAnimation: IntentType? = null,  // drives specific animations (Pizza, etc.)
    val errorMessage: String? = null,
    val serverOnline: Boolean = false
)

/**
 * MainViewModel - Connects the UI to the IntentEngine.
 *
 * Exposes a [StateFlow] of [AuraUiState] that the Compose UI observes.
 * All business logic is delegated to [IntentEngine].
 */
class MainViewModel : ViewModel() {

    private val intentEngine = IntentEngine()

    private val _uiState = MutableStateFlow(AuraUiState())
    val uiState: StateFlow<AuraUiState> = _uiState.asStateFlow()

    // ─── Input Handling ───────────────────────────────────────────────────────

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text, errorMessage = null) }
    }

    /**
     * Main entry point: user submits a query.
     * Parses intent → fetches response → updates history.
     */
    fun onSubmit() {
        val input = _uiState.value.inputText.trim()
        if (input.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, currentAnimation = null) }

            val intent = intentEngine.parseIntent(input)
            val response = intentEngine.fetchResponse(intent)

            val historyItem = IntentHistoryItem(
                query      = input,
                response   = response,
                intentType = intent.intentType
            )

            _uiState.update { state ->
                state.copy(
                    inputText        = "",
                    isProcessing     = false,
                    intentHistory    = listOf(historyItem) + state.intentHistory,
                    currentAnimation = intent.intentType,
                    errorMessage     = null
                )
            }
        }
    }

    /**
     * Clears the currently active intent animation after it finishes.
     */
    fun clearAnimation() {
        _uiState.update { it.copy(currentAnimation = null) }
    }

    /**
     * Removes an item from the history list.
     */
    fun dismissHistoryItem(id: Long) {
        _uiState.update { state ->
            state.copy(intentHistory = state.intentHistory.filter { it.id != id })
        }
    }

    /**
     * Clears the full intent history.
     */
    fun clearHistory() {
        _uiState.update { it.copy(intentHistory = emptyList()) }
    }
}
