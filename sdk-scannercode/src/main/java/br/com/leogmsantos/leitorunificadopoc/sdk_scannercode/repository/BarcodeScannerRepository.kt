package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.repository

import android.content.Context
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback

interface BarcodeScannerRepository {
    fun startCamera(
        barcodeScannerCallback: BarcodeScannerCallback,
        preview: PreviewView,
        surficeProvider: Preview.SurfaceProvider,
        context: Context
    )
}