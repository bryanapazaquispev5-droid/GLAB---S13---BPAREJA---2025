package com.example.glab_s13_bpareja_2025.components.videojuego

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.sin

object BgmManager {
    private var isPlaying = false
    private var audioTrack: AudioTrack? = null
    private var bgmThread: Thread? = null

    @Volatile
    var isRageMode = false

    fun start() {
        if (isPlaying) return
        isPlaying = true
        isRageMode = false // Resetear al iniciar partida
        bgmThread = Thread {
            val sampleRate = 22050 // Tasa de muestreo óptima para sintetizador en segundo plano
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_FLOAT
            )

            val track = try {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
                return@Thread
            }

            audioTrack = track
            track.play()

            // Línea de bajo retro espacial y progresiva
            val bassline = floatArrayOf(
                110.00f, 110.00f, 130.81f, 110.00f, 
                146.83f, 146.83f, 130.81f, 146.83f,
                164.81f, 164.81f, 196.00f, 164.81f, 
                146.83f, 130.81f, 110.00f, 110.00f
            )

            // Melodía de sintetizador espacial arpegiada (2ª Voz)
            val lead = floatArrayOf(
                220.00f, 261.63f, 329.63f, 261.63f,
                293.66f, 349.23f, 440.00f, 349.23f,
                329.63f, 392.00f, 493.88f, 392.00f,
                293.66f, 329.63f, 220.00f, 220.00f
            )

            var step = 0
            var phaseBass = 0.0
            var phaseLead = 0.0

            while (isPlaying) {
                // Modificar el tempo en tiempo real si el jefe está enfurecido
                val stepDurMs = if (isRageMode) 160 else 250 // Más rápido en modo furia (~187 BPM)
                val samplesPerStep = (stepDurMs * sampleRate) / 1000
                val writeBuffer = FloatArray(samplesPerStep)

                val freqBass = bassline[step % bassline.size]
                val freqLead = lead[step % lead.size]

                for (i in 0 until samplesPerStep) {
                    // Acumular fase continuamente para transiciones suaves sin clics de audio
                    phaseBass += 2.0 * Math.PI * freqBass / sampleRate
                    phaseLead += 2.0 * Math.PI * freqLead / sampleRate

                    // Sintetizar bajo (Onda cuadrada profunda)
                    val waveBass = if (sin(phaseBass) > 0.0) 0.05f else -0.05f

                    // Sintetizar melodía líder (Onda de pulso angosto para estilo cyber-retro)
                    val waveLead = if ((phaseLead % (2.0 * Math.PI)) < 0.22 * (2.0 * Math.PI)) 0.035f else -0.035f

                    // Envolvente de decaimiento suave para cada nota de la melodía principal
                    val progress = i.toFloat() / samplesPerStep
                    val env = 1f - progress * 0.45f

                    writeBuffer[i] = waveBass + waveLead * env
                }

                try {
                    track.write(writeBuffer, 0, writeBuffer.size, AudioTrack.WRITE_BLOCKING)
                } catch (e: Exception) {
                    break
                }
                step++
            }

            try {
                track.stop()
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        bgmThread?.start()
    }

    fun stop() {
        isPlaying = false
        audioTrack = null
        bgmThread = null
    }
}
