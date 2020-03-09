package fr.nemoisbae.realtimecodebarreader.shared

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorManager (
    private val vibrator: Vibrator
) {

    private val TAG = "VibratorManager"

    companion object {
        val TOUCH_VIBRATION_DURATION_IN_MS: Long = 65
    }

    fun makeOneShotVibration(vibrationDurationInMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        vibrationDurationInMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                return
            }
            vibrator.vibrate(vibrationDurationInMs)
        } catch (exception: Exception) {

        }
    }
}
