package fr.nemoisbae.realtimecodebarreader

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.TextureView
import androidx.annotation.RequiresApi

/**
 *
 * https://proandroiddev.com/android-camerax-preview-analyze-capture-1b3f403a9395
 *
 */
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var imageDimensions: Size = Size(0, 0)

    val textureViewSurfaceListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture?,
            width: Int,
            height: Int
        ) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraDevice: CameraDevice? = null

        findViewById<TextureView>(R.id.cameraWrapperTextureView)?.let { textureView ->
            textureView.surfaceTextureListener = textureViewSurfaceListener
        }
    }

    private fun openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cameraManager: CameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId: String = cameraManager.cameraIdList[0]

            val cameraCharacteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)

            val streamConfigurationMap: StreamConfigurationMap? = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)



        } else {
            Log.e(this.javaClass.simpleName, "SDK ERROR")
        }
    }
}
