package com.example.motosoundsimulator.MotoView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.motosoundsimulator.Model.MotoModel
import com.example.motosoundsimulator.R
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.thread

class MotoView(context: Context) : View(context) {

    private val motoModels = mutableListOf<MotoModel>()
    private val MAX_FINGERS = 10
    private var touchStartTime: Long = 0
    private var touchDuration: Long = 0
    private var isTouchingAcelerador = false
    private var aceleradorRotationTimer: Timer? = null
    private var isOscillating = false
    private var oscillationDirection = 1

    private var mSoundPool: SoundPool? = null
    private val soundIds = intArrayOf(
        R.raw.moto_1,
        R.raw.moto_5,
        R.raw.moto_10
    )

    init {

        initSoundPool()
        motoModels.add(
            MotoModel(
                "Tablero", R.raw.moto_4, loadBitmap(R.drawable.tablero_moto),
                0.7f, setXPosPercentage(50f), setYPosPercentage(66f), 0f, 0f, 0f
            )
        )
        motoModels.add(
            MotoModel(
                "Abuja", R.raw.moto_4, loadBitmap(R.drawable.abuja_roja),
                0.4f, setXPosPercentage(44f), setYPosPercentage(78f), -180f, 0f, 0f
            )
        )
        motoModels.add(
            MotoModel(
                "Acelerador", R.raw.moto_4, loadBitmap(R.drawable.acelerador),
                0.4f, setXPosPercentage(90f), setYPosPercentage(50f), 0f, 0f, 0f
            )
        )

        setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> handleMultiTouchDown(event)
                MotionEvent.ACTION_UP -> handleTouchUp(event)
            }
            true
        }
    }

    private fun loadBitmap(resId: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resId)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (drum in motoModels) {
            drawDrum(canvas, drum)
        }
    }

    private fun drawRotatedBitmap(
        canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, degrees: Float, pivotX: Float, pivotY: Float
    ) {
        val matrix = Matrix()
        matrix.postTranslate(-pivotX, -pivotY)
        matrix.postRotate(degrees)
        matrix.postTranslate(x, y)
        canvas.drawBitmap(bitmap, matrix, null)
    }

    private fun drawDrum(canvas: Canvas, drum: MotoModel) {
        val scaledBitmap = Bitmap.createScaledBitmap(
            drum.bitmap,
            (drum.bitmap.width * drum.scale).toInt(),
            (drum.bitmap.height * drum.scale).toInt(),
            true
        )
        if (drum.id == "Abuja") {
            drum.pivotX = (drum.bitmap.width * drum.scale / 2f)
            drum.pivotY = (drum.bitmap.height * drum.scale / 2f)
        }
        drawRotatedBitmap(
            canvas,
            scaledBitmap,
            drum.xPosition - scaledBitmap.width / 2f,
            drum.yPosition - scaledBitmap.height / 2f,
            drum.rotationDegrees,
            drum.pivotX,
            drum.pivotY
        )
    }

    private fun handleMultiTouchDown(event: MotionEvent) {
        val pointerCount = event.pointerCount
        for (i in 0 until minOf(pointerCount, MAX_FINGERS)) {
            val touchX = event.getX(i)
            val touchY = event.getY(i)
            for (drum in motoModels) {
                if (isTouchInsideDrum(touchX, touchY, drum)) {
                    touchStartTime = System.currentTimeMillis()
                    touchDuration = 0
                    println("Tocaste el tambor ${drum.id}")
                    if (drum.id == "Acelerador") {
                        isTouchingAcelerador = true
                        val abujaModel = motoModels.find { it.id == "Abuja" }
                        if (aceleradorRotationTimer == null) {
                            abujaModel?.let { startAceleradorRotationTimer(it) }
                        }
                    }
                }
            }
        }
    }

    private fun isTouchInsideDrum(touchX: Float, touchY: Float, drum: MotoModel): Boolean {
        val scaledWidth = drum.bitmap.width * drum.scale
        val scaledHeight = drum.bitmap.height * drum.scale
        val leftX = drum.xPosition - scaledWidth / 2f
        val rightX = drum.xPosition + scaledWidth / 2f
        val topY = drum.yPosition - scaledHeight / 2f
        val bottomY = drum.yPosition + scaledHeight / 2f
        return touchX in leftX..rightX && touchY >= topY && touchY <= bottomY
    }

    fun setXPosPercentage(percentage: Float): Float {
        val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
        return (screenWidth * percentage / 100).coerceIn(0f, screenWidth - width)
    }

    fun setYPosPercentage(percentage: Float): Float {
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        return (screenHeight * percentage / 100).coerceIn(0f, screenHeight - height)
    }

    private fun handleTouchUp(event: MotionEvent) {
        touchDuration = System.currentTimeMillis() - touchStartTime
        println("Duración del toque: $touchDuration milisegundos")
        isTouchingAcelerador = false
        stopAceleradorRotationTimer()
        postInvalidate()
    }

    private fun startAceleradorRotationTimer(motoModel: MotoModel) {
        aceleradorRotationTimer?.cancel()
        aceleradorRotationTimer?.purge()
        aceleradorRotationTimer = Timer()
        aceleradorRotationTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                rotateAcelerador(motoModel)
            }
        }, 0, 50)
    }

    private fun stopAceleradorRotationTimer() {
        aceleradorRotationTimer?.cancel()
        aceleradorRotationTimer?.purge()
        aceleradorRotationTimer = null
    }

    private fun rotateAcelerador(aceleradorModel: MotoModel) {
        if (isTouchingAcelerador) {
            if (aceleradorModel.rotationDegrees < 80f) {
                aceleradorModel.rotationDegrees += 10f
                val segment = (aceleradorModel.rotationDegrees / 80f * soundIds.size).toInt()
                playSoundForSegment(segment)
            } else if (aceleradorModel.rotationDegrees >= 80f) {
                isOscillating = true
            }
            if (isOscillating) {
                aceleradorModel.rotationDegrees += 5 * oscillationDirection
                if (aceleradorModel.rotationDegrees >= 80f || aceleradorModel.rotationDegrees <= 75f) {
                    oscillationDirection *= -1
                }
            }
        } else {
            isOscillating = false
            aceleradorModel.rotationDegrees -= 10f
            if (aceleradorModel.rotationDegrees < -180f) {
                aceleradorModel.rotationDegrees = -180f
            }
        }
        postInvalidate()
    }

    private fun playSoundForSegment(segment: Int) {
        mSoundPool?.play(soundIds[segment], 1f, 1f, 1, 0, 1f)
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mSoundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(5)
            .build()

        soundIds.forEach {
            mSoundPool?.load(context, it, 1)
        }
    }
}
