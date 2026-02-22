package com.auraos.launcher.engine

import com.auraos.launcher.data.model.AuraIntent
import com.auraos.launcher.data.model.IntentResponse
import com.auraos.launcher.data.model.IntentType
import com.auraos.launcher.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * IntentEngine - The "brain" of Aura OS.
 *
 * Intercepts raw text input, classifies it into a known IntentType,
 * fetches enriched data from the Python mock server (with local fallback),
 * and returns a fully-formatted IntentResponse.
 */
class IntentEngine {

    companion object {
        // Keywords that map to known intent types (case-insensitive)
        private val CALENDAR_KEYWORDS = listOf("calendar", "meeting", "schedule", "appointment", "event")
        private val TRAVEL_KEYWORDS   = listOf("travel", "flight", "trip", "hotel", "nyc", "vacation")
        private val PIZZA_KEYWORDS    = listOf("pizza", "food", "hungry", "order", "eat", "dinner")
    }

    /**
     * Parses the user's raw text and returns an AuraIntent.
     */
    fun parseIntent(input: String): AuraIntent {
        val lower = input.lowercase()
        val intentType = when {
            CALENDAR_KEYWORDS.any { lower.contains(it) } -> IntentType.Calendar
            TRAVEL_KEYWORDS.any  { lower.contains(it) } -> IntentType.Travel
            PIZZA_KEYWORDS.any   { lower.contains(it) } -> IntentType.Pizza
            else                                         -> IntentType.Unknown
        }
        val keyword = intentType.toKeyword()
        return AuraIntent(keyword = keyword, intentType = intentType, rawInput = input)
    }

    /**
     * Fetches a response for the given AuraIntent.
     * First tries the Python server; falls back to local mock data if unreachable.
     */
    suspend fun fetchResponse(intent: AuraIntent): IntentResponse = withContext(Dispatchers.IO) {
        // Skip network call for unknown intents
        if (intent.intentType is IntentType.Unknown) {
            return@withContext buildUnknownResponse(intent.rawInput)
        }

        try {
            val response = RetrofitClient.apiService.getIntentResponse(intent.keyword)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                buildLocalFallback(intent)
            }
        } catch (e: Exception) {
            // Python server not running - use local mock data
            buildLocalFallback(intent)
        }
    }

    // ─── Local Fallback Mock Data ─────────────────────────────────────────────

    private fun buildLocalFallback(intent: AuraIntent): IntentResponse {
        return when (intent.intentType) {
            IntentType.Calendar -> IntentResponse(
                intent    = "calendar",
                title     = "📅 Meeting at 3 PM",
                subtitle  = "Sync with the Product Team · Google Meet",
                emoji     = "📅",
                action    = "Open Calendar",
                timestamp = "Today, 3:00 PM"
            )
            IntentType.Travel -> IntentResponse(
                intent    = "travel",
                title     = "✈️ Flight to NYC",
                subtitle  = "AA2847 · JFK → LAX · Gate B12 · On Time",
                emoji     = "✈️",
                action    = "View Boarding Pass",
                timestamp = "Tomorrow, 7:45 AM"
            )
            IntentType.Pizza -> IntentResponse(
                intent    = "pizza",
                title     = "🍕 Ordering Pizza...",
                subtitle  = "Domino's · Pepperoni Large · ETA 35 min",
                emoji     = "🍕",
                action    = "Track Order",
                timestamp = "Now"
            )
            else -> buildUnknownResponse(intent.rawInput)
        }
    }

    private fun buildUnknownResponse(input: String) = IntentResponse(
        intent    = "unknown",
        title     = "🤔 Not sure about that",
        subtitle  = "Try: 'Calendar', 'Travel', or 'Pizza'",
        emoji     = "🤔",
        action    = "Try Again",
        timestamp = "Just now"
    )

    private fun IntentType.toKeyword(): String = when (this) {
        IntentType.Calendar -> "calendar"
        IntentType.Travel   -> "travel"
        IntentType.Pizza    -> "pizza"
        IntentType.Unknown  -> "unknown"
    }
}
