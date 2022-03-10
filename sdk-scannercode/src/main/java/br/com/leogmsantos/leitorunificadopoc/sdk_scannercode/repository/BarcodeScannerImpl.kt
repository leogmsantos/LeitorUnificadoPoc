package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.repository

import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.commons.AnalyzerBarcodeScanner
import com.google.mlkit.vision.barcode.Barcode
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BarcodeScannerImpl : BarcodeScannerRepository {

    private val RATIO_4_3_VALUE = 4.0 / 3.0
    private val RATIO_16_9_VALUE = 16.0 / 9.0

    private lateinit var cameraInfo: CameraInfo
    private lateinit var cameraControl: CameraControl
    private lateinit var barcodeResult: MutableList<Barcode>
    private lateinit var context: Context

    private val executor by lazy {
        Executors.newSingleThreadExecutor()
    }

    override fun startCamera(
        barcodeScannerCallback: BarcodeScannerCallback,
        preview: PreviewView,
        surficeProvider: Preview.SurfaceProvider,
        context: Context
    ) {
        val metrics = DisplayMetrics().also { preview.display.getRealMetrics(it) }
        val screenAspectRatio =
            aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = preview.display.rotation

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

            preview.setSurfaceProvider(surficeProvider)

            val textQRcodeAnalyzer = initializeAnalyzer(screenAspectRatio, rotation, barcodeScannerCallback)
            cameraProvider.unbindAll()

            try {
                val camera = cameraProvider.bindToLifecycle(
                    context as AppCompatActivity, cameraSelector, preview, textQRcodeAnalyzer
                )

                cameraControl = camera.cameraControl
                cameraInfo = camera.cameraInfo
                cameraControl.setLinearZoom(0.5f)

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun initializeAnalyzer(screenAspectRatio: Int, rotation: Int, callback: BarcodeScannerCallback): UseCase {
        return ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(executor, AnalyzerBarcodeScanner(callback))
            }
    }
}