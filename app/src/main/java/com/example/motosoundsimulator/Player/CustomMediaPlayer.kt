package com.example.motosoundsimulator.Player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler

class CustomMediaPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    init {
        // Inicializar el MediaPlayer
        mediaPlayer = MediaPlayer()

        // Configurar atributos de audio para Android 21 y versiones posteriores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            mediaPlayer?.setAudioAttributes(audioAttributes)
        } else {
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    fun playSection(resourceId: Int, startPositionMs: Int, endPositionMs: Int) {
        try {
            // Cargar el archivo desde el recurso de Android
            mediaPlayer = MediaPlayer.create(context, resourceId)

            // Reproducir una sección específica
            mediaPlayer?.seekTo(startPositionMs)
            mediaPlayer?.start()

            // Detener la reproducción después de la duración de la sección
            stopAfterDuration(endPositionMs - startPositionMs)
        } catch (e: Exception) {
            e.printStackTrace()
            // Manejar la excepción
        }
    }

    private fun stopAfterDuration(duration: Int) {
        val stopHandler = Handler()
        stopHandler.postDelayed({
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }, duration.toLong())
    }

    fun release() {
        mediaPlayer?.release()
    }
}