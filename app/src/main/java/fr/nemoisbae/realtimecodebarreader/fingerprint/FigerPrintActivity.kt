package fr.nemoisbae.realtimecodebarreader.fingerprint

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import fr.nemoisbae.realtimecodebarreader.R
import java.util.Date.from
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FigerPrintActivity : AppCompatActivity() {

    private val dialogInterface: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->

    }

    private var context: Context? = null


    @RequiresApi(Build.VERSION_CODES.P)
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            result?.let {
                it.cryptoObject?.let { cryptoObject ->

                }
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
        }
    }

    private val cancellationSignal: CancellationSignal = CancellationSignal()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_figer_print)

        context = this

        val executor: Executor = Executors.newSingleThreadExecutor()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometricPrompt: BiometricPrompt= BiometricPrompt.Builder(this)
                .setTitle("Title")
                .setSubtitle("Subtitle")
                .setDescription("Description")
                .setNegativeButton("Cancel", executor, dialogInterface)
                .build()


            biometricPrompt.authenticate(cancellationSignal, executor, authenticationCallback)
        } else {
            Log.w("VERSION", "Version pas bonne")
        }
    }
}
