
package com.example.motosoundsimulator

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.motosoundsimulator.MotoView.MotoView
import com.example.motosoundsimulator.Player.AudioPlayer

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoView = MotoView(this)
        setContentView(motoView)
    }

    /*private lateinit var audioPlayer: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproductor)

        audioPlayer = AudioPlayer(this, R.raw.moto) // Reemplaza con tu archivo de sonido

        // Configura el botón para iniciar la reproducción progresiva del sonido
        val playButton: View = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            audioPlayer.startProgressivePlayback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.release()
    }*/
}