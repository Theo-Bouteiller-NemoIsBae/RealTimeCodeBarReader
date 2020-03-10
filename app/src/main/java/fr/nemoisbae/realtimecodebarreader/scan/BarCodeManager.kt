package fr.nemoisbae.realtimecodebarreader.scan

import android.util.Log

class BarCodeManager {
    companion object {

        fun isValidEan13(ean13Code: String): Boolean {

            val barCode: ArrayList<Int> = ArrayList()

            for (i: Int in 0..ean13Code.length - 2) {
                barCode.add(Character.getNumericValue(ean13Code[i]))
            }

            for (current: Int in barCode) {
                Log.w("barCode", current.toString())
            }

            var isMultiplication: Boolean = false

            var sumBarCode: Int = 0

            for (current: Int in barCode) {
                sumBarCode += if (isMultiplication) {
                    current * 3
                } else {
                    current
                }

                isMultiplication = !isMultiplication
            }

            val dividingRest: Int = sumBarCode % 10
            val dividingResult: Int = sumBarCode / 10

            Log.w("barCode", "Dividing Rest = $dividingRest")

            val checkSum: Int = 10 - dividingRest

            var calculatedBarCode: String = ""
            for (current: Int in barCode) {
                calculatedBarCode += current.toString()
            }

            calculatedBarCode += checkSum.toString()

            Log.w("barCode", "Calculated barcode: $calculatedBarCode")

            if (ean13Code == calculatedBarCode) {
                return true
            }

            return false
        }
    }
}