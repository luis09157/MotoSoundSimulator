
package com.example.motosoundsimulator

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa el MediaPlayer con el archivo de sodo inicial
        mediaPlayer = MediaPlayer.create(this, R.raw.moto)

        // Inicializa el SeekBar
        seekBar = findViewById(R.id.seekBar)
        seekBar.max = 100
        seekBar.progress = 0

        // Configura el OnSeekBarChangeListener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Cambia el archivo de sonido según el rango del progreso
                changeSound(R.raw.moto, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Pausa la reproducción cuando se toca el SeekBar
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Reanuda la reproducción cuando se suelta el SeekBar
                mediaPlayer.start()
            }
        })
    }

    private fun changeSound(soundResource: Int,position : Int) {
        // Cambia el archivo de sonido y reinicia la reproducción
        mediaPlayer.release()

        mediaPlayer = MediaPlayer.create(this, soundResource)
        mediaPlayer.seekTo(calcularDuracion(position))
        mediaPlayer.start()
    }
    fun calcularDuracion(position : Int) : Int {
        var result = 0
        val duration = mediaPlayer.duration
        println("Duración total del audio: $duration ms")
        result = duration / 100 * position
        println("Duración total del audio result: $result ms")

        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos del MediaPlayer
        mediaPlayer.release()
    }
}