package fr.nemoisbae.realtimecodebarreader

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

/**
 *
 * https://proandroiddev.com/android-camerax-preview-analyze-capture-1b3f403a9395
 *
 * https://www.youtube.com/watch?v=zBh1YQlF-i0
 *
 */
class MainActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE: Int = 42

    var camera: Camera? = null

    var surfaceHolder: SurfaceHolder? = null

    val surfaceHolderCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder?) {
            camera = openCamera()

            camera?.let {
                Log.w("TRUC", "CAM CREE")
                val parameters: Camera.Parameters = it.parameters
//                parameters.setPreviewFpsRange(10, 60)
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE

                it.setDisplayOrientation(90)
                it.parameters = parameters
                if (null != surfaceHolder) {
                    it.setPreviewDisplay(surfaceHolder!!)
                }

                it.startPreview()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceHolder = findViewById<SurfaceView>(R.id.cameraWrapperSurfaceView)?.holder

        findViewById<Button>(R.id.closeButton)?.let {
            if (null != camera) {
                camera!!.stopPreview()
            }
        }


        if (null != surfaceHolder) {
            askPermission()
        }
    }

    private fun openCamera(): Camera? {
        return try {
            Camera.open()
        } catch (exception: Exception) {
            null
        }
    }

    private fun askPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            surfaceHolder?.let {
                it.addCallback(surfaceHolderCallback)
                it.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (CAMERA_REQUEST_CODE == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder?.let {
                    it.addCallback(surfaceHolderCallback)
                    it.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
                }
            } else {
                askPermission()
            }
        }
    }
}
