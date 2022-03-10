package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.application

import android.content.Context
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.repository.BarcodeScannerImpl
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.repository.BarcodeScannerRepository


class BarcodeScannerApplication(
    private val barcodeScannerCallback: BarcodeScannerCallback,
    private val preview: PreviewView,
    private val surficeProvider: Preview.SurfaceProvider,
    private val context: Context
) {

    private val barcodeScannerRepository: BarcodeScannerRepository

    init {
        barcodeScannerRepository = BarcodeScannerImpl()
    }

    fun startBarcodeScanner(){
        barcodeScannerRepository.startCamera(
            barcodeScannerCallback = this.barcodeScannerCallback,
            preview = this.preview,
            surficeProvider = this.surficeProvider,
            context = this.context
        )
    }

}