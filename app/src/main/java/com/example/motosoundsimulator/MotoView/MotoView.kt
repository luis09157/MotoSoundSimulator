package com.example.motosoundsimulator.MotoView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.View
import com.example.motosoundsimulator.Model.MotoModel
import com.example.motosoundsimulator.R

class MotoView(context: Context) : View(context) {

    private val motoModels = mutableListOf<MotoModel>()
    private val MAX_FINGERS = 10
    private var touchStartTime: Long = 0
    private var touchDuration: Long = 0

    init {
        // Carga las imágenes desde recursos y establece las escalas y posiciones 37
        motoModels.add(MotoModel("Acelerador", R.raw.moto_4, loadBitmap(R.drawable.tablero_moto),
           0.7f, setXPosPercentage(50f),setYPosPercentage(66f),0f,0f,0f))
        motoModels.add(MotoModel("Abuja", R.raw.moto_4, loadBitmap(R.drawable.abuja_con_fondo),
            0.2f, setXPosPercentage(37f),setYPosPercentage(58f),0f,0f,0f))

        // Agrega más imágenes, escalas y posiciones según sea necesario
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

        // Dibuja los tambores en las posiciones especificadas
        for (drum in motoModels) {
            drawDrum(canvas, drum)
        }
    }
    private fun drawRotatedBitmap(canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, degrees: Float, pivotX: Float, pivotY: Float) {
        val matrix = Matrix()

        // Configura la matriz para la rotación y el pivote
        matrix.postTranslate(-pivotX, -pivotY)
        matrix.postRotate(degrees)
        matrix.postTranslate(x, y)

        // Dibuja el bitmap con la matriz de transformación aplicada
        canvas.drawBitmap(bitmap, matrix, null)
    }
    private fun drawDrum(canvas: Canvas, drum: MotoModel) {
        // Ajusta el tamaño del tambor con el parámetro de escala asociado
        val scaledBitmap = Bitmap.createScaledBitmap(
            drum.bitmap,
            (drum.bitmap.width * drum.scale).toInt(),
            (drum.bitmap.height * drum.scale).toInt(),
            true
        )
        if(drum.id == "Abuja"){
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

            // ... Resto del código ...

            for (drum in motoModels) {
                if (isTouchInsideDrum(touchX, touchY, drum)) {
                    touchStartTime = System.currentTimeMillis()
                    touchDuration = 0

                    println("Tocaste el tambor ${drum.id}")
                    //UtilPlayer.playWav(context, drum.midiResourceId)
                    if (drum.id == "Abuja") {
                         rotateAbuja(drum, touchX, touchY)
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
        val xPos = (screenWidth * percentage / 100).coerceIn(0f, screenWidth - width)
        return xPos
    }
    fun setYPosPercentage(percentage: Float): Float {
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        val yPos = (screenHeight * percentage / 100).coerceIn(0f, screenHeight - height)
        return yPos
    }
    private fun handleTouchUp(event: MotionEvent) {
        touchDuration = System.currentTimeMillis() - touchStartTime
        println("Duración del toque: $touchDuration milisegundos")
    }
    private fun rotateAbuja(abujaModel: MotoModel, touchX: Float, touchY: Float) {
        // Calcula el punto de pivote en el centro del bitmap
        val pivotX = abujaModel.xPosition + (abujaModel.bitmap.width * abujaModel.scale / 2f)
        val pivotY = abujaModel.yPosition + (abujaModel.bitmap.height * abujaModel.scale / 2f)

        // Ajusta el punto de pivote en el modelo
        abujaModel.pivotX = (abujaModel.bitmap.width * abujaModel.scale / 2f)
        abujaModel.pivotY = (abujaModel.bitmap.height * abujaModel.scale / 2f)
        // Ajusta la rotación de la "Abuja" sin cambiar las coordenadas
        abujaModel.rotationDegrees += 20f

        // Solicita un redibujo de la vista para reflejar la rotación
        invalidate()
    }


}