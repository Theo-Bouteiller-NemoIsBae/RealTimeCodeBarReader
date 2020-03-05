package fr.nemoisbae.realtimecodebarreader

import android.content.Intent
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.scan_button)?.setOnClickListener {
            this.startActivity(Intent(this, scanActivity::class.java))
        }

        findViewById<Button>(R.id.nfc_button)?.setOnClickListener {
            this.startActivity(Intent(this, nfcActivity::class.java))
        }
    }
}
