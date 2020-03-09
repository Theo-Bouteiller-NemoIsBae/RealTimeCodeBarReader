package fr.nemoisbae.realtimecodebarreader

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fr.nemoisbae.realtimecodebarreader.fingerprint.FigerPrintActivity
import fr.nemoisbae.realtimecodebarreader.nfc.nfcActivity
import fr.nemoisbae.realtimecodebarreader.scan.scanActivity

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

        findViewById<Button>(R.id.figer_print_button)?.setOnClickListener {
            this.startActivity(Intent(this, FigerPrintActivity::class.java))
        }
    }
}
