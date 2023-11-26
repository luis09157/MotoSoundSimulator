
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
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscopeSensor: Sensor
    private val handler = Handler()
    private var variable = 0
    private lateinit var textViewResultado: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResultado = findViewById(R.id.btn_contar)
        // Inicializa el MediaPlayer con el archivo de sonido inicial
        mediaPlayer = MediaPlayer.create(this, R.raw.moto)

        // Inicializa el SensorManager y el giroscopio
       /* sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        if (gyroscopeSensor == null) {
            Toast.makeText(this, "El dispositivo no tiene giroscopio", Toast.LENGTH_SHORT).show()
            finish()
        }*/

        textViewResultado.setOnClickListener {
            Handler().postDelayed({
                variable = 1
                incrementarVariableHasta100()
            }, 0)
        }

    }

    override fun onResume() {
        super.onResume()
        // Registra el listener del giroscopio cuando la actividad se reanuda
       // sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        // Desregistra el listener del giroscopio cuando la actividad se pausa
      //  sensorManager.unregisterListener(this)
    }

    fun calcularDuracion(position : Int) : Int {
        var result = 0
        val duration = mediaPlayer.duration
        println("Duración total del audio: $duration ms")
        result = duration / 10 * position
        println("Duración total del audio result: $result ms")

        return result
    }
    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos del MediaPlayer
        mediaPlayer.release()
    }

    private fun changeSound() {
        // Cambia el archivo de sonido y reinicia la reproducción si es necesario
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun incrementarVariableHasta100() {
        val handler = Handler()
        val incremento = 1

        // Actualizar la variable cada 100 milisegundos hasta llegar a 100
        handler.post(object : Runnable {
            override fun run() {

                if (variable < 100) {
                    variable += incremento
                    // Puedes realizar alguna acción con la variable aquí
                    // En este ejemplo, simplemente imprimo el valor
                    println("Variable: $variable")
                    Log.e("Variable","Track Info: ${ mediaPlayer.duration}")
                    mediaPlayer.seekTo(calcularDuracion(variable))
                    changeSound()


                    // Llamar recursivamente después de un retraso de 100 milisegundos
                    handler.postDelayed(this, (mediaPlayer.duration / 10).toLong())
                }
            }
        })
    }
}