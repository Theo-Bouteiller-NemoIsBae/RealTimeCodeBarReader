package fr.nemoisbae.realtimecodebarreader.shared

import android.content.res.Resources
import android.util.TypedValue

fun Number.getPxForDp(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
}

fun Number.getDpForPx(): Float {
    return (this.toFloat() / Resources.getSystem().displayMetrics.density)
}