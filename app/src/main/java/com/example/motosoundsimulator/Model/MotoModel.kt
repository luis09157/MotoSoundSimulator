package com.example.motosoundsimulator.Model

import android.graphics.Bitmap

class MotoModel(
 val id: String,
 val resourceId: Int,
 val bitmap: Bitmap,
 val scale: Float,
 var xPosition: Float,
 var yPosition: Float,
 var rotationDegrees: Float = 0f,
 var pivotX: Float = 0f,
 var pivotY: Float = 0f
)