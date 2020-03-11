package fr.nemoisbae.realtimecodebarreader.shared.view

import android.content.ComponentCallbacks
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import fr.nemoisbae.realtimecodebarreader.R
import fr.nemoisbae.realtimecodebarreader.shared.getColorSafe
import fr.nemoisbae.realtimecodebarreader.shared.getDrawableSafe

class PatternView (
    context: Context,
    attributeSet: AttributeSet?
): View (context, attributeSet) {

    private val RADIUS: Float = 70f

    private val SIZE: Float = 70f

    private val LINE_WIDTH: Float = 25f

    private var drawableSelected: Drawable? = null
    private var drawableUnSelected: Drawable? = null


    private var callback: ((Boolean)->(Unit))? = null
    private var pattern: ArrayList<Int> = arrayListOf()

    private var circles: ArrayList<Circle>? = null

    private val paintCircleSelected: Paint = Paint()
    private val paintCircleUnSelected: Paint = Paint()
    private val paintLine: Paint = Paint()

    private var viewPastInOrder: ArrayList<Circle> = arrayListOf()

    private var lineX: Float? = null
    private var lineY: Float? = null

    init {
        paintCircleSelected.color = context.getColorSafe(R.color.colorAccent) ?: 0
        paintCircleSelected.isAntiAlias = true

        paintCircleUnSelected.color = context.getColorSafe(R.color.colorDotBareCode) ?: 0
        paintCircleUnSelected.isAntiAlias = true

        paintLine.color = context.getColorSafe(R.color.colorLinePattern) ?: 0
        paintLine.isAntiAlias = true
        paintLine.strokeWidth = LINE_WIDTH

        drawableSelected = this.context.getDrawableSafe(R.drawable.dot_selected)
        drawableUnSelected = this.context.getDrawableSafe(R.drawable.dot_unselected)
    }

    fun setOnResultListener(callback: (Boolean)->(Unit)) {
        this.callback = callback
    }

    fun setPattern(pattern: ArrayList<Int>) {
        this.pattern = pattern
    }

    override fun onDraw(canvas: Canvas?) {

        if (null == canvas) {
            return
        }

        for (i: Int in 0 until viewPastInOrder.size) {
            if (viewPastInOrder.size > i + 1) {
                canvas.drawLine(viewPastInOrder[i].cX, viewPastInOrder[i].cY, viewPastInOrder[i + 1].cX, viewPastInOrder[i + 1].cY, paintLine)
            }
        }

        if (null != lineX && null != lineY && viewPastInOrder.isNotEmpty()) {
            viewPastInOrder[viewPastInOrder.size - 1].let {
                canvas.drawLine(it.cX, it.cY, lineX!!, lineY!!, paintLine)
            }
        }

        if (null == circles) {
            initCircles(width = width.toFloat(), height = height.toFloat())
        }

        if (null != circles) {
            for (circle: Circle in circles!!) {
                canvas.drawCircle(circle.cX, circle.cY, circle.radius, circle.paint)
            }
        }

        super.onDraw(canvas)
    }

    private fun initCircles(width: Float, height: Float) {

        if (null == circles) {
           circles = arrayListOf()
        }

        val spacingWidth: Float = (width - (RADIUS * 3f)) / 4f
        val spacingHeight: Float = (height - (RADIUS * 3f)) / 4f


        var number: Int = 0

        if (null != circles) {
            for (y: Int in 1..3) {
                for (x: Int in 1..3) {
                    number += 1
                    circles!!.add(
                        Circle(
                            cX = (RADIUS * (x - 1)) + (spacingWidth * x) + (RADIUS / 2),
                            cY = (RADIUS * (y - 1)) + (spacingHeight * y) + (RADIUS / 2),
                            radius = RADIUS,
                            paint = paintCircleUnSelected,
                            number = number
                        )
                    )
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (null == event) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewPastInOrder = arrayListOf()
            }

            MotionEvent.ACTION_MOVE -> {

                val pos: IntArray = IntArray(2)
                this.getLocationOnScreen(pos)

                val x: Float = event.rawX - pos[0].toFloat()
                val y: Float = event.rawY - pos[1].toFloat()

                if (null != circles && isFingerInView(event.rawX, event.rawY, pos[0].toFloat(), pos[1].toFloat())) {
                    lineX = x
                    lineY = y

                    circles!!.forEach { circle: Circle ->
                        if (!isAlreadyOnArrayList(circle)) {
                            if (isFingerInCircle(
                                    x,
                                    y,
                                    pos[0].toFloat(),
                                    pos[1].toFloat(),
                                    circle
                                )
                            ) {
                                circle.paint = paintCircleSelected
                                viewPastInOrder.add(circle)
                            } else {
                                circle.paint = paintCircleUnSelected
                            }
                        }
                    }
                    this.invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                lineX = null
                lineY = null

                if (null != callback) {
                    callback!!(isMatching())
                }
            }
        }

        return true
    }

    private fun isMatching(): Boolean {
        if (pattern.isEmpty() || viewPastInOrder.isEmpty()) {
            return false
        }

        if (viewPastInOrder.size != pattern.size) {
            return false
        }

        for (i: Int in 0 until viewPastInOrder.size) {
            if (pattern[i] != viewPastInOrder[i].number) {
                return false
            }
        }

        return true
    }

    private fun isFingerInView(fingerX: Float, fingerY: Float, viewX: Float, viewY: Float): Boolean {
        if (fingerX < viewX) {
            return false
        }

        if (viewX + this.width < fingerX) {
            return false
        }

        if (fingerY < viewY) {
            return false
        }

        if (viewY + this.height < fingerY) {
            return false
        }

        return true
    }

    private fun isFingerInCircle(fingerX: Float, fingerY: Float, viewX: Float, viewY: Float, circle: Circle): Boolean {
        if (fingerX < circle.cX - (circle.radius)) {
            return false
        }

        if (circle.cX + (circle.radius) < fingerX) {
            return false
        }

        if (fingerY < circle.cY - (circle.radius)) {
            return false
        }

        if (circle.cY + (circle.radius) < fingerY) {
            return false
        }

//        if (fingerX < (viewX + circle.cX) - (circle.radius)) {
//            return false
//        }
//
//        if (viewX + circle.cX + (circle.radius) < fingerX) {
//            return false
//        }
//
//        if (fingerY < (viewY + circle.cY) - (circle.radius)) {
//            return false
//        }
//
//        if (viewY + circle.cY + (circle.radius) < fingerY) {
//            return false
//        }

        return true
    }

    private fun isAlreadyOnArrayList(circle: Circle): Boolean {
        viewPastInOrder.forEach {
            if (circle.number == it.number) {
                return true
            }
        }

        return false
    }

}