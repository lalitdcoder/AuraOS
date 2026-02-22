package com.auraos.launcher.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a parsed intent from the user's text input.
 */
data class AuraIntent(
    val keyword: String,
    val intentType: IntentType,
    val rawInput: String
)

/**
 * Sealed class representing the different types of intents Aura can handle.
 */
sealed class IntentType {
    object Calendar : IntentType()
    object Travel : IntentType()
    object Pizza : IntentType()
    object Unknown : IntentType()
}

/**
 * Response received from the Python mock server.
 */
data class IntentResponse(
    @SerializedName("intent") val intent: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("subtitle") val subtitle: String = "",
    @SerializedName("emoji") val emoji: String = "",
    @SerializedName("action") val action: String = "",
    @SerializedName("timestamp") val timestamp: String = ""
)

/**
 * A single item in the intent history list shown in the UI.
 */
data class IntentHistoryItem(
    val id: Long = System.currentTimeMillis(),
    val query: String,
    val response: IntentResponse,
    val intentType: IntentType
)
