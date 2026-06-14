package com.example.glab_s13_bpareja_2025.components.videojuego

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

object SoundManager {
    private const val SAMPLE_RATE = 44100

    private fun playSynthSamples(samples: FloatArray) {
        try {
            val bufferSize = samples.size * 4 // 4 bytes per Float
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size, AudioTrack.WRITE_NON_BLOCKING)
            audioTrack.play()

            // Liberar memoria del canal una vez termine de reproducir
            Thread {
                try {
                    val durationMs = (samples.size.toFloat() / SAMPLE_RATE * 1000).toLong()
                    Thread.sleep(durationMs + 100)
                    audioTrack.stop()
                    audioTrack.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Sonido de disparo láser del jugador (Square wave pitch slide down)
    fun playLaser() {
        Thread {
            val duration = 0.12f // 120 ms
            val numSamples = (duration * SAMPLE_RATE).toInt()
            val samples = FloatArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toFloat() / numSamples
                val freq = 1000f - progress * 700f
                val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                // Onda cuadrada retro 8-bit con decaimiento de volumen
                samples[i] = (if (sin(angle) > 0f) 0.12f else -0.12f) * (1f - progress)
            }
            playSynthSamples(samples)
        }.start()
    }

    // Sonido de explosión del impacto al jefe (White noise burst)
    fun playExplosion() {
        Thread {
            val duration = 0.22f // 220 ms
            val numSamples = (duration * SAMPLE_RATE).toInt()
            val samples = FloatArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toFloat() / numSamples
                // Ruido blanco puro con decaimiento lineal
                val noise = (Math.random().toFloat() * 2f - 1f) * 0.18f
                samples[i] = noise * (1f - progress)
            }
            playSynthSamples(samples)
        }.start()
    }

    // Sonido de impacto al jugador (Low-freq buzzer pitch slide)
    fun playPlayerHit() {
        Thread {
            val duration = 0.25f // 250 ms
            val numSamples = (duration * SAMPLE_RATE).toInt()
            val samples = FloatArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toFloat() / numSamples
                val freq = 220f - progress * 130f
                val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                // Onda cuadrada de baja frecuencia para zumbido de daño
                samples[i] = (if (sin(angle) > 0f) 0.18f else -0.18f) * (1f - progress)
            }
            playSynthSamples(samples)
        }.start()
    }

    // Sonido de recoger item de poder / Power-up (Fast ascending major arpeggio)
    fun playPowerUp() {
        Thread {
            val noteDurSec = 0.08f
            val numSamples = (noteDurSec * SAMPLE_RATE * 4).toInt()
            val samples = FloatArray(numSamples)
            val noteDuration = numSamples / 4
            val freqs = floatArrayOf(440.00f, 554.37f, 659.25f, 880.00f) // Arpegio La Mayor (A, C#, E, A)
            for (note in 0 until 4) {
                val freq = freqs[note]
                val startOffset = note * noteDuration
                for (i in 0 until noteDuration) {
                    val progressInNote = i.toFloat() / noteDuration
                    val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                    samples[startOffset + i] = (if (sin(angle) > 0f) 0.12f else -0.12f) * (1f - progressInNote * 0.2f)
                }
            }
            playSynthSamples(samples)
        }.start()
    }

    // Sonido de curación (Soft ascending synthesizer sweep)
    fun playHeal() {
        Thread {
            val durationSec = 0.25f
            val numSamples = (durationSec * SAMPLE_RATE).toInt()
            val samples = FloatArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toFloat() / numSamples
                val freq = 330.0f + progress * 440.0f // Sweep from 330Hz to 770Hz
                val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                samples[i] = (sin(angle).toFloat() * 0.18f) * (1f - progress)
            }
            playSynthSamples(samples)
        }.start()
    }

    // Arpegio ascendente de victoria (C -> E -> G -> C de 8 bits)
    fun playVictory() {
        Thread {
            val noteDurSec = 0.10f
            val numSamples = (noteDurSec * SAMPLE_RATE * 4).toInt()
            val samples = FloatArray(numSamples)
            val noteDuration = numSamples / 4
            val freqs = floatArrayOf(523.25f, 659.25f, 783.99f, 1046.50f) // Notas musicales
            for (note in 0 until 4) {
                val freq = freqs[note]
                val startOffset = note * noteDuration
                for (i in 0 until noteDuration) {
                    val progressInNote = i.toFloat() / noteDuration
                    val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                    samples[startOffset + i] = (if (sin(angle) > 0f) 0.12f else -0.12f) * (1f - progressInNote * 0.3f)
                }
            }
            playSynthSamples(samples)
        }.start()
    }

    // Secuencia descendente triste de derrota
    fun playDefeat() {
        Thread {
            val noteDurSec = 0.14f
            val numSamples = (noteDurSec * SAMPLE_RATE * 4).toInt()
            val samples = FloatArray(numSamples)
            val noteDuration = numSamples / 4
            val freqs = floatArrayOf(392.00f, 311.13f, 261.63f, 196.00f) // Notas tristes descendentes
            for (note in 0 until 4) {
                val freq = freqs[note]
                val startOffset = note * noteDuration
                for (i in 0 until noteDuration) {
                    val progressInNote = i.toFloat() / noteDuration
                    val angle = 2.0 * PI * freq * (i.toFloat() / SAMPLE_RATE)
                    samples[startOffset + i] = (if (sin(angle) > 0f) 0.15f else -0.15f) * (1f - progressInNote * 0.5f)
                }
            }
            playSynthSamples(samples)
        }.start()
    }
}
