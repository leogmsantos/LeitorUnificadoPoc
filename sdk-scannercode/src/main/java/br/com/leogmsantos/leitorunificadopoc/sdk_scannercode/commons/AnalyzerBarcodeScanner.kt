package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.commons

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class AnalyzerBarcodeScanner(private val callback: BarcodeScannerCallback) : ImageAnalysis.Analyzer{

    private val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_CODE_39,
        Barcode.FORMAT_CODE_93,
        Barcode.FORMAT_CODE_128,
        Barcode.FORMAT_CODABAR,
        Barcode.FORMAT_EAN_13,
        Barcode.FORMAT_EAN_8,
        Barcode.FORMAT_ITF,
        Barcode.FORMAT_UPC_A,
        Barcode.FORMAT_UPC_E,
        Barcode.FORMAT_QR_CODE
    ).build()

    private val scanner by lazy {
        BarcodeScanning.getClient(options)
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to the scanner and have it do its thing
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    if (barcodes.isEmpty().not()) {
                        callback.onScannerResult(barcodes)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnCompleteListener {
                    // It's important to close the imageProxy
                    imageProxy.close()
                }
        }
    }

}