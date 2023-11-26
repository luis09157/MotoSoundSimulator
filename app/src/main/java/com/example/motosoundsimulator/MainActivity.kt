
package com.example.motosoundsimulator

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(){

    private lateinit var mediaPlayer: MediaPlayer
    private var variable = 0
    private lateinit var textViewResultado: Button
    private val vueltas = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResultado = findViewById(R.id.btn_contar)
        mediaPlayer = MediaPlayer.create(this, R.raw.moto)


        textViewResultado.setOnClickListener {
            Handler().postDelayed({
                variable = 0
                incrementarVariableHasta100()
            }, 0)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun calcularDuracion(position : Int) : Int {
        var result = 0
        val duration = mediaPlayer.duration
        println("Duración total del audio: $duration ms")
        result = duration / vueltas * position
        println("Duración total del audio result: $result ms")

        return result
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

    private fun incrementarVariableHasta100() {
        val handler = Handler()
        val incremento = 1
        handler.post(object : Runnable {
            override fun run() {

                if (variable < vueltas) {
                    variable += incremento

                    mediaPlayer.seekTo(calcularDuracion(variable))
                    changeSound()

                    handler.postDelayed(this, (mediaPlayer.duration / vueltas).toLong())
                }
            }
        })
    }
}