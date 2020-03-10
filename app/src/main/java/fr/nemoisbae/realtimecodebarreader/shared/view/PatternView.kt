package fr.nemoisbae.realtimecodebarreader.shared.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import fr.nemoisbae.realtimecodebarreader.R
import fr.nemoisbae.realtimecodebarreader.shared.getColorSafe

class PatternView (
    context: Context,
    attributeSet: AttributeSet?
): View (context, attributeSet) {

    private val RADIUS: Float = 40f

    private val circles: ArrayList<Circle> = arrayListOf()

    init {
    }

    override fun onDraw(canvas: Canvas?) {

        if (null == canvas) {

            return
        }

        val paintCircle: Paint = Paint()
        paintCircle.color = context.getColorSafe(R.color.colorAccent) ?: 0



        val spacing: Float = (width - (RADIUS * 3f)) / 4f

        canvas.drawCircle(spacing, 150f, 30f, paintCircle)
        canvas.drawCircle(spacing + RADIUS + spacing, 150f, 30f, paintCircle)
        canvas.drawCircle(spacing + RADIUS + spacing + RADIUS + spacing, 150f, 30f, paintCircle)


        Log.w("TEST", "Size : ${this.width} x ${this.height}")

        super.onDraw(canvas)
    }
}