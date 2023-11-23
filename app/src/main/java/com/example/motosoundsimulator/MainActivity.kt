package com.example.motosoundsimulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.motosoundsimulator.Player.CustomMediaPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var customMediaPlayer: CustomMediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Crear una instancia de CustomMediaPlayer
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    fun init(){
        val resourceName = "moto_sound" // Reemplaza 'tu_archivo_wav' con el nombre real de tu archivo WAV
        val resourceId = resources.getIdentifier(resourceName, "raw", packageName)

        if (resourceId != 0) {
            // Crear una instancia de CustomMediaPlayer
            customMediaPlayer = CustomMediaPlayer(this)

            Log.e("reproductor", "android.resource://${packageName}/${R.raw.moto_sound}")
            // Construir la ruta del archivo WAV utilizando la notación 'android.resource'
            val audioFilePath = "android.resource://${packageName}/${R.raw.moto_sound}"

            // Reproducir una sección específica
            val startPositionMs = 1000 // posición de inicio en milisegundos
            val endPositionMs = 5000   // posición de fin en milisegundos
            customMediaPlayer.playSection(R.raw.moto_sound, startPositionMs, endPositionMs)
        } else {
            // Manejar caso en el que el archivo no existe
            Toast.makeText(this, "Archivo WAV no encontrado", Toast.LENGTH_SHORT).show()
        }
    }
    fun isRawResourceExist(resourceName: String): Boolean {
        val resourceId = resources.getIdentifier(resourceName, "raw", packageName)
        return resourceId != 0
    }
    override fun onDestroy() {
        // Liberar recursos al destruir la actividad
        customMediaPlayer.release()
        super.onDestroy()
    }
}