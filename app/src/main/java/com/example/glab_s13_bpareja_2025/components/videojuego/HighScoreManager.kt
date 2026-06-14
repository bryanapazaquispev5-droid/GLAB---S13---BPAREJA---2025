package com.example.glab_s13_bpareja_2025.components.videojuego

import android.content.Context

object HighScoreManager {
    private const val PREFS_NAME = "cyber_battle_prefs"
    private const val KEY_HIGH_SCORE = "high_score"

    // Obtener la puntuación máxima guardada en SharedPreferences
    fun getHighScore(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_HIGH_SCORE, 0)
    }

    // Guardar la puntuación máxima si supera el récord actual
    fun saveHighScore(context: Context, score: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentHigh = prefs.getInt(KEY_HIGH_SCORE, 0)
        if (score > currentHigh) {
            prefs.edit().putInt(KEY_HIGH_SCORE, score).apply()
            return true // Devuelve true si es un nuevo récord
        }
        return false
    }
}
