#!/usr/bin/env python3
"""
Aura OS - Python Mock Server
Flask server returning mock intent data for the Android launcher.

Run:
    pip install flask
    python server.py

Then in Android Emulator, the app connects via 10.0.2.2:5000
(which maps to your Mac's localhost:5000)
"""

from flask import Flask, jsonify, request
from datetime import datetime
import random

app = Flask(__name__)

# ─── Mock Data Store ──────────────────────────────────────────────────────────

INTENT_DATA = {
    "calendar": {
        "intent":    "calendar",
        "title":     "📅 Meeting at 3 PM",
        "subtitle":  "Sync with the Product Team · Google Meet · Room 4B",
        "emoji":     "📅",
        "action":    "Open Calendar",
        "timestamp": "Today, 3:00 PM"
    },
    "travel": {
        "intent":    "travel",
        "title":     "✈️ Flight to NYC",
        "subtitle":  "AA2847 · JFK → LAX · Gate B12 · On Time",
        "emoji":     "✈️",
        "action":    "View Boarding Pass",
        "timestamp": "Tomorrow, 7:45 AM"
    },
    "pizza": {
        "intent":    "pizza",
        "title":     "🍕 Ordering Pizza...",
        "subtitle":  "Domino's · Pepperoni Large + Cheesy Bread · ETA 35 min",
        "emoji":     "🍕",
        "action":    "Track Order",
        "timestamp": "Now"
    },
    "unknown": {
        "intent":    "unknown",
        "title":     "🤔 Intent Not Recognized",
        "subtitle":  "Aura couldn't understand that — try Calendar, Travel, or Pizza",
        "emoji":     "🤔",
        "action":    "Try Again",
        "timestamp": datetime.now().strftime("%I:%M %p")
    }
}

# ─── Routes ───────────────────────────────────────────────────────────────────

@app.route("/ping", methods=["GET"])
def ping():
    """Health check — confirms Python server is reachable."""
    return jsonify({"status": "ok", "server": "Aura OS Python Bridge", "version": "1.0"})


@app.route("/intent", methods=["GET"])
def get_intent():
    """
    Returns mock intent response for a given keyword.
    GET /intent?keyword=pizza
    """
    keyword = request.args.get("keyword", "").lower().strip()

    # Try exact match first
    if keyword in INTENT_DATA:
        response = INTENT_DATA[keyword].copy()
    else:
        # Fuzzy: check if any key is a substring of the keyword
        matched = next(
            (k for k in INTENT_DATA if k in keyword or keyword in k),
            None
        )
        response = INTENT_DATA.get(matched, INTENT_DATA["unknown"]).copy()

    # Inject live timestamp
    response["server_time"] = datetime.now().strftime("%I:%M:%S %p")
    response["source"]      = "python_server"

    print(f"[Aura] Intent request: '{keyword}' → '{response['intent']}'")
    return jsonify(response)


@app.route("/intents", methods=["GET"])
def list_intents():
    """Returns all available mock intents."""
    return jsonify({"available_intents": list(INTENT_DATA.keys())})


@app.route("/", methods=["GET"])
def index():
    return jsonify({
        "welcome": "Aura OS Python Bridge",
        "endpoints": ["/ping", "/intent?keyword=<keyword>", "/intents"],
        "version": "1.0.0"
    })


# ─── Main ─────────────────────────────────────────────────────────────────────

if __name__ == "__main__":
    print("=" * 50)
    print("  🌌 Aura OS Python Bridge Server v1.0")
    print("  Listening on http://localhost:5000")
    print("  Android Emulator connects via 10.0.2.2:5000")
    print("=" * 50)
    app.run(host="0.0.0.0", port=5000, debug=True)
