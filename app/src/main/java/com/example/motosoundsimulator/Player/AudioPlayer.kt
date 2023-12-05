package com.example.motosoundsimulator.Player
import android.content.Context
import android.media.MediaPlayer

class AudioPlayer {

class AudioPlayer(private val context: Context, private val audioResource: Int) {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentVolume = 0.0f
    private val maxVolume = 1.0f
    private val numberOfSteps = 10 // Número de pasos para aumentar el volumen
    private var audioDuration: Int = 0 // Almacenar la duración del audio

    init {
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context, audioResource)
        mediaPlayer.setOnCompletionListener {
            resetPlayer()
        }

        // Almacenar la duración del audio después de cargarlo
        audioDuration = mediaPlayer.duration
    }

    private fun resetPlayer() {
        currentVolume = 0.0f
        mediaPlayer.seekTo(0)
        mediaPlayer.stop()
        mediaPlayer.prepare()
    }

    fun setPlaybackRange(startMillis: Int, endMillis: Int) {
        mediaPlayer.seekTo(startMillis)
        mediaPlayer.setNextMediaPlayer(mediaPlayer)
    }

    fun startProgressivePlayback() {
        currentVolume = 0.0f
        mediaPlayer.seekTo(0)
        mediaPlayer.start()

        val volumeIncrement = maxVolume / numberOfSteps
        val timer = java.util.Timer()
        val timerTask = object : java.util.TimerTask() {
            override fun run() {
                if (currentVolume <= maxVolume) {
                    mediaPlayer.setVolume(currentVolume, currentVolume)
                    currentVolume += volumeIncrement
                } else {
                    timer.cancel()
                }
            }
        }

        timer.schedule(timerTask, 0, 100)
    }

    fun release() {
        mediaPlayer.release()
    }

    fun getDuration(): Int {
        return audioDuration
        }
    }
}