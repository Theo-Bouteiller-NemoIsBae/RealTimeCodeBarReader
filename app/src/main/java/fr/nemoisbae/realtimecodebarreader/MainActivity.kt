package fr.nemoisbae.realtimecodebarreader

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.math.BigDecimal
import java.math.RoundingMode

/**
 *
 * https://proandroiddev.com/android-camerax-preview-analyze-capture-1b3f403a9395
 *
 * https://www.youtube.com/watch?v=zBh1YQlF-i0
 *
 */
class MainActivity : AppCompatActivity() {

    private var root: ViewGroup? = null

    private var captureManager: CaptureManager? = null

    private val CODE: String = "27WCUZ3PYOC433"

    private var nbrGoodTry: Int = 0
    private var nbrTry: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = this.layoutInflater.inflate(R.layout.activity_main, null, false) as ViewGroup

        setContentView(root)

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
                nbrTry += 1

                if (CODE == it.text) {
                    nbrGoodTry += 1
                }


                val average: Double = nbrGoodTry.toDouble() / nbrTry.toDouble()
                textView.text = "Result ${it.result.text} FormatName: ${it.barcodeFormat.name} "



                root!!.findViewById<TextView>(R.id.statScan)?.text = "Accuracy : ${BigDecimal((average * 100)).setScale(
                    2,
                    RoundingMode.HALF_EVEN
                )}% " +
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
