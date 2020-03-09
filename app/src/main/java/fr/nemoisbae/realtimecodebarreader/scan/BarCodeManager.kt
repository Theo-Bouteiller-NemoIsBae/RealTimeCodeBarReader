package fr.nemoisbae.realtimecodebarreader.scan

class BarCodeManager {
    companion object {

        fun checkEan13(ean13Code: String) {

            var barCode: ArrayList<Int> = ArrayList()

            for (i: Int in 0..ean13Code.length - 2) {
                barCode.add(ean13Code[i].toInt())
            }

            var isMultiplication: Boolean = false

            var sumBarCode: Int = 0

            for (current: Int in barCode) {
                sumBarCode += if (isMultiplication) {
                    current * 3
                } else {
                    current
                }

                isMultiplication != isMultiplication
            }

            var dividingRest: Int = 0
            var dividingResult: Int = 0

        }

    }
}