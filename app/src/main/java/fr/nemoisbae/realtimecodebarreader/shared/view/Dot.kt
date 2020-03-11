package fr.nemoisbae.realtimecodebarreader.shared.view

import android.graphics.drawable.Drawable

data class Dot (
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    var drawable: Int,
    val number: Int
)