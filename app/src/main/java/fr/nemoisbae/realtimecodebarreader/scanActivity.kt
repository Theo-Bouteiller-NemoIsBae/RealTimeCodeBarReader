package fr.nemoisbae.realtimecodebarreader

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.math.BigDecimal
import java.math.RoundingMode

class scanActivity : AppCompatActivity() {
    private var root: ViewGroup? = null

    private var captureManager: CaptureManager? = null

    private val CODE: String = "27WCUZ3PYOC433"

    private var nbrGoodTry: Int = 0
    private var nbrTry: Int = 0

    private var vibratorManager: VibratorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = this.layoutInflater.inflate(R.layout.activity_scan, null, false) as ViewGroup

        setContentView(root)

        vibratorManager = VibratorManager(getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)

        if (null != root) {

            root!!.findViewById<DecoratedBarcodeView>(R.id.barcodeView)?.let { decoratedBarcodeView ->
                captureManager = CaptureManager(this, decoratedBarcodeView)

                if (null != captureManager) {
                    captureManager!!.initializeFromIntent(intent, savedInstanceState)

                    processScan(decoratedBarcodeView)


                    root!!.findViewById<ImageView>(R.id.takePictureImageView)?.setOnClickListener {
                        processScan(decoratedBarcodeView)
                    }
                }

            }
        }
    }

    private fun processScan(decoratedBarcodeView: DecoratedBarcodeView) {

        root!!.findViewById<TextView>(R.id.resultScan)?.let { textView ->
            textView.text = "scan"
            decoratedBarcodeView.decodeSingle {

                vibratorManager?.makeOneShotVibration(VibratorManager.TOUCH_VIBRATION_DURATION_IN_MS)

                nbrTry += 1

                if (CODE == it.text) {
                    nbrGoodTry += 1
                }


                val average: Double = nbrGoodTry.toDouble() / nbrTry.toDouble()
                textView.text = "Result ${it.result.text} FormatName: ${it.barcodeFormat.name} "



                root!!.findViewById<TextView>(R.id.statScan)?.text =
                    "Accuracy : ${BigDecimal((average * 100)).setScale(2, RoundingMode.HALF_EVEN)}% " +
                            "Good try : $nbrGoodTry " +
                            "Bad try : ${nbrTry - nbrGoodTry} " +
                            "Total ty : $nbrTry"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        captureManager?.onPause()
    }

    override fun onResume() {
        super.onResume()
        captureManager?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager?.onDestroy()
    }
}
