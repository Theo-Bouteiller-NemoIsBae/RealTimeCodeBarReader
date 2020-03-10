package fr.nemoisbae.realtimecodebarreader.scan

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.camera.CameraSettings
import fr.nemoisbae.realtimecodebarreader.R
import fr.nemoisbae.realtimecodebarreader.shared.VibratorManager

/**
 *
 * http://www.gomaro.ch/codeean.htm
 *
 */

class scanActivity : AppCompatActivity() {
    private var root: ViewGroup? = null

    private var captureManager: CaptureManager? = null

    private var vibratorManager: VibratorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = this.layoutInflater.inflate(R.layout.activity_scan, null, false) as ViewGroup

        setContentView(root)

        vibratorManager =
            VibratorManager(
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            )

        if (null != root) {

            root!!.findViewById<DecoratedBarcodeView>(R.id.barcodeView)?.let { decoratedBarcodeView ->
                val cameraSettings: CameraSettings = CameraSettings()
                cameraSettings.isMeteringEnabled = true
                cameraSettings.isContinuousFocusEnabled = true

                decoratedBarcodeView.cameraSettings = cameraSettings

                root!!.findViewById<ImageView>(R.id.flashImageViewOn)?.setOnClickListener {
                    decoratedBarcodeView.setTorchOff()
                    root!!.findViewById<ImageView>(R.id.flashImageViewOff)?.visibility = View.VISIBLE
                    it.visibility = View.GONE
                }

                root!!.findViewById<ImageView>(R.id.flashImageViewOff)?.setOnClickListener {
                    decoratedBarcodeView.setTorchOn()
                    root!!.findViewById<ImageView>(R.id.flashImageViewOn)?.visibility = View.VISIBLE
                    it.visibility = View.GONE
                }

                captureManager = CaptureManager(this, decoratedBarcodeView)

                if (null != captureManager) {
                    captureManager!!.initializeFromIntent(intent, savedInstanceState)

                    processScan(decoratedBarcodeView)

                    root!!.findViewById<ImageView>(R.id.takePictureImageView)?.let {
                        it.visibility = View.INVISIBLE
                        it.setOnClickListener {
                            processScan(decoratedBarcodeView)
                        }
                    }
                }

            }
        }
    }

    private fun getSafeDrawable(id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getDrawable(R.drawable.ic_flash_on_black_24dp)
        } else {
            this.resources.getDrawable(R.drawable.ic_flash_on_black_24dp)
        }
    }

    private fun processScan(decoratedBarcodeView: DecoratedBarcodeView) {
        decoratedBarcodeView.decodeContinuous {

            Log.w("BarCode", "Test : ${it.result.text}")

//            decoratedBarcodeView.pause()
//
//            vibratorManager?.makeOneShotVibration(VibratorManager.TOUCH_VIBRATION_DURATION_IN_MS)
//
//            root!!.findViewById<LinearLayout>(R.id.resultPopup)?.visibility = View.VISIBLE
//            root!!.findViewById<Button>(R.id.closePopUp)?.setOnClickListener {
//                root!!.findViewById<LinearLayout>(R.id.resultPopup)?.visibility = View.GONE
//                decoratedBarcodeView.resume()
//            }
//
//            root!!.findViewById<TextView>(R.id.resultScan)?.text = "Result ${it.result.text} FormatName: ${it.barcodeFormat.name}"
//
//            if (BarCodeManager.isValidEan13(it.result.text)) {
//                Log.w("barcode", "Its work")
//            } else {
//                Log.w("barcode", "Its not work")
//            }

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
