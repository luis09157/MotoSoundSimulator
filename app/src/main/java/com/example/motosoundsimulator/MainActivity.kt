
package com.example.motosoundsimulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motosoundsimulator.MotoView.MotoView

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoView = MotoView(this)
        setContentView(motoView)
    }
}