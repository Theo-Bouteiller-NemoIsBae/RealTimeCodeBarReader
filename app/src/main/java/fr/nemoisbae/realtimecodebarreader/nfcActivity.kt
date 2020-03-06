package fr.nemoisbae.realtimecodebarreader

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*


class nfcActivity : AppCompatActivity() {


    private var nfcAdapter: NfcAdapter? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.w("NFC", "Action ${intent?.action.toString()}")

            intent?.action.let { action ->
                if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    override fun onResume() {
        super.onResume()

        enableNfcForegroundDispatch(this)
    }

    override fun onPause() {
        super.onPause()

        disableNfcForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let { intent ->
            Log.w("Intent", "New intent [flag=${intent.flags}, action=${intent.action}")

            when (intent.action) {
                NfcAdapter.ACTION_TECH_DISCOVERED -> {
                    onNfcTechDiscovered(intent)
                }

                NfcAdapter.ACTION_NDEF_DISCOVERED -> {

                }
            }

        }
    }

    /**
     * Enables the NFC foreground dispatch system for the given Activity.
     * @param targetActivity The Activity that is in foreground and wants to
     * have NFC Intents.
     * @see .disableNfcForegroundDispatch
     */
    private fun enableNfcForegroundDispatch(targetActivity: Activity) {
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
            val intent = Intent(
                targetActivity,
                targetActivity.javaClass
            ).addFlags(
                Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            val pendingIntent = PendingIntent.getActivity(
                targetActivity, 0, intent, 0
            )
            nfcAdapter!!.enableForegroundDispatch(
                targetActivity, pendingIntent, null, arrayOf(
                    arrayOf(
                        NfcA::class.java.name
                    )
                )
            )
        }
    }

    /**
     * Disable the NFC foreground dispatch system for the given Activity.
     * @param targetActivity An Activity that is in foreground and has
     * NFC foreground dispatch system enabled.
     * @see .enableNfcForegroundDispatch
     */
    private fun disableNfcForegroundDispatch(targetActivity: Activity?) {
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
            nfcAdapter!!.disableForegroundDispatch(targetActivity)
        }
    }

    /**
     * Convert an array of bytes into a string of hex values.
     * @param bytes Bytes to convert.
     * @return The bytes in hex string format.
     */
    private fun byte2HexString(bytes: ByteArray): String? {
        var result: String = ""
        bytes.forEach {
            result += String.format("%02X", it.toInt() and 0xFF)
        }

        return if (result.isEmpty() && result.isBlank()) {
            null
        } else {
            result
        }
    }

    private fun onNfcTechDiscovered(intent: Intent) {

        val resultWrite: Boolean = createNFCMessage(UUID.randomUUID().toString(), intent)
        if (resultWrite) {
            Log.w("WRITE", "SUCCESS WRITE")
        } else {
            Log.w("WRITE", "FAIL WRITE")
        }

        intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)?.let {
            Log.w("NFC", "TAg:")
            it.forEach {
                Log.w("NFC", String.format("%02X", it.toInt() and 0xFF))
            }
        }

        intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.let { it ->
            byte2HexString(it.id)?.also {
                Log.w("NFC", "Tag: $it")
                findViewById<TextView>(R.id.nfcTagTextView)?.text = it
            }
        }

        intent.getStringExtra(NfcAdapter.EXTRA_SECURE_ELEMENT_NAME)?.let {
            Log.w("NFC", "Secure: $it")
        }

        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessage ->
            Log.w("NFC", "NDEF")

            val message: List<NdefMessage> = rawMessage.map { it as NdefMessage }

            message.forEach {

                it.records.forEach {
                    Log.w("NFC", it.payload.toString(Charsets.UTF_8))
                }

                Log.w("NFC", "")
            }
        }
    }

    private fun createNFCMessage(payload: String, intent: Intent?): Boolean {

        val pathPrefix = "peterjohnwelcome.com:nfcapp"
        val nfcRecord = NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray()
        )
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }

                return if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    true
                } else {
                    //NFC tag is read-only
                    false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                return try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    true
                } catch (e: IOException) {
                    //Failed to format tag
                    false
                }
            }
            //NDEF is not supported
            return false

        } catch (e: Exception) {
            //Write operation has failed
        }
        return false
    }


    private fun onNdefDiscovered() {
        Log.w("NDEF", "HELLO WORLD")
    }
}
