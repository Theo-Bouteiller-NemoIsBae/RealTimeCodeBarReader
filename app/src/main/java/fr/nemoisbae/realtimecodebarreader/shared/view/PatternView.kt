package fr.nemoisbae.realtimecodebarreader.shared.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import fr.nemoisbae.realtimecodebarreader.R
import fr.nemoisbae.realtimecodebarreader.shared.getColorSafe

class PatternView (
    context: Context,
    attributeSet: AttributeSet?
): View (context, attributeSet) {

    private val RADIUS: Float = 100f

    private val circles: ArrayList<Circle> = arrayListOf()

    private val paintCircleSelected: Paint = Paint()
    private val paintCircleUnSelected: Paint = Paint()

    init {
        paintCircleSelected.color = context.getColorSafe(R.color.colorAccent) ?: 0
        paintCircleUnSelected.color = context.getColorSafe(R.color.colorDotBareCode) ?: 0
    }

    override fun onDraw(canvas: Canvas?) {

        if (null == canvas) {

            return
        }

        initCircles(width = width.toFloat(), height = height.toFloat())

        for (circle: Circle in circles) {
            canvas.drawCircle(circle.cX, circle.cY, circle.radius, circle.paint)
        }


//        canvas.drawCircle(
//            spacingWidth + (RADIUS / 2),
//            spacingHeight + (RADIUS / 2),
//            RADIUS,
//            paintCircle
//        )
//        canvas.drawCircle(
//            spacingWidth + RADIUS + spacingWidth + (RADIUS / 2),
//            spacingHeight + RADIUS + spacingHeight + (RADIUS / 2),
//            RADIUS,
//            paintCircle
//        )
//        canvas.drawCircle(
//            spacingWidth + RADIUS + spacingWidth + RADIUS + spacingWidth + (RADIUS / 2),
//            spacingHeight + RADIUS + spacingHeight + RADIUS + spacingHeight + (RADIUS / 2),
//            RADIUS,
//            paintCircle
//        )


        Log.w("TEST", "Size : ${this.width} x ${this.height}")

        super.onDraw(canvas)
    }

    private fun initCircles(width: Float, height: Float) {
        val spacingWidth: Float = (width - (RADIUS * 3f)) / 4f
        val spacingHeight: Float = (height - (RADIUS * 3f)) / 4f

        for (y: Int in 1..3) {
            for (x:Int in 1..3) {
                circles.add(
                    Circle(
                        cX = (RADIUS * (x - 1)) + (spacingWidth * x) + (RADIUS / 2),
                        cY = (RADIUS * (y - 1)) + (spacingHeight * y) + (RADIUS / 2),
                        radius = RADIUS,
                        paint = paintCircleSelected
                    )
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (null == event) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val x: Float = event.rawX
                val y: Float = event.rawY

                if (isFingerInView(x, y)) {
                    Log.w("PatternView", "Touch : $x ; $y")

                    circles.forEach { circle: Circle ->
                        circle.paint = if (isFingerInCircle(x, y, circle)) {
                            paintCircleSelected
                        } else {
                            paintCircleUnSelected
                        }
                    }
                }
            }
        }



        return true
    }

    private fun isFingerInView(x: Float, y: Float): Boolean {
        if (x < this.x) {
            return false
        }

        if (this.x + this.width < x) {
            return false
        }

        if (y < this.y) {
            return false
        }

        if (this.y + this.height < y) {
            return false
        }

        return true
    }

    private fun isFingerInCircle(x: Float, y: Float, circle: Circle): Boolean {
        if (x < circle.cX - (circle.radius / 2)) {
            Log.w("Colide", "Not in circle")
            return false
        }

        if (circle.cX + (circle.radius / 2) < x) {
            Log.w("Colide", "Not in circle")
            return false
        }

        if (y < circle.cY - (circle.radius / 2)) {
            Log.w("Colide", "Not in circle")
            return false
        }

        if (circle.cY + (circle.radius / 2) < y) {
            Log.w("Colide", "Not in circle")
            return false
        }

        Log.w("Colide", "In circle")
        return true
    }
}