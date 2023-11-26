
package com.example.motosoundsimulator

import android.media.MediaPlayer
import android.os.Bundle
import android.os.ConditionVariable
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var variable = 0
    private lateinit var textViewResultado: Button
    private val vueltas = 18
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResultado = findViewById(R.id.btn_contar)
        mediaPlayer = MediaPlayer.create(this, R.raw.moto)




        textViewResultado.setOnClickListener {
            variable = 0
            for (i in 0..vueltas) {
                val delay = (i * (mediaPlayer.duration / vueltas))
               // Log.e("NestorOP","vuelta ${i}")
                incrementarVariableHasta100(delay)

            }
        }
    }

    fun calcularDuracion(position : Int) : Int {
        var result = 0
        val duration = mediaPlayer.duration
        Log.e("NestorOP","duration: $duration ms")
        result = duration / vueltas * position
        Log.e("NestorOP","result: $result ms")

        return result + 1
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun changeSound() {
        if (!mediaPlayer.isPlaying)     {
            mediaPlayer.start()
        }
    }

    private fun incrementarVariableHasta100(variable: Int) {

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.moto)
            Log.e("NestorOP","duration: $variable ms")
            mediaPlayer.seekTo(variable)
            mediaPlayer.start()
            Thread.sleep((mediaPlayer.duration/vueltas).toLong())
            mediaPlayer.stop()
        } catch (ex: InterruptedException) {
            // Manejar la excepción
            ex.printStackTrace()
        }


       // changeSound()
    }

}