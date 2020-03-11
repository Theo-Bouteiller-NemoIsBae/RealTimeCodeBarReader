package fr.nemoisbae.realtimecodebarreader.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import fr.nemoisbae.realtimecodebarreader.R
import fr.nemoisbae.realtimecodebarreader.shared.view.PatternView

class ModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)

        findViewById<PatternView>(R.id.patternView)?.let {
            it.setPattern(arrayListOf(7, 4, 1, 5, 3, 6, 9))
            it.setOnResultListener {
                if (it) {
                    findViewById<TextView>(R.id.resultPatternTextView)?.text = "OUI"
                } else {
                    findViewById<TextView>(R.id.resultPatternTextView)?.text = "NON"
                }
            }
        }
    }
}
