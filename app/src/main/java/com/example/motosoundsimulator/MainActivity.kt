
package com.example.motosoundsimulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motosoundsimulator.MotoView.MotoView

class MainActivity : AppCompatActivity()/*, SoundPool.OnLoadCompleteListener  */{
    /*var listIdSound : ArrayList<Int> = arrayListOf()
    var mSoundPool: SoundPool? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoView = MotoView(this)
        setContentView(motoView)
    }

/*    override fun onLoadComplete(p0: SoundPool?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun playSound(){
        thread {
            listIdSound.forEach {it->
                mSoundPool!!.play(it!!, 1f, 1f, 1, 0, 1f);
                Thread.sleep(516)
            }

            // Si necesitas actualizar la interfaz de usuario, puedes usar un Handler
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                // Actualiza la interfaz de usuario aquí
            }
        }
    }
    fun initSoundPool(){
        val spb = SoundPool.Builder()
        spb.setMaxStreams(5)
        mSoundPool = spb.build()


        listIdSound.add(mSoundPool?.load(this, R.raw.moto_1, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_2, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_3, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_4, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_5, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_6, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_7, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_8, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_9, 1)!!)
        listIdSound.add(mSoundPool?.load(this, R.raw.moto_10, 1)!!)
    }*/
}