package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode

import android.content.Context
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.application.BarcodeScannerApplication
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback

class BarcodeScannerManager private constructor(){

    class Builder(
        private val barcodeScannerCallback: BarcodeScannerCallback,
        private val preview: PreviewView,
        private val surficeProvider: Preview.SurfaceProvider,
        private val context: Context
    ){
        fun build() : BarcodeScannerApplication {
            return BarcodeScannerApplication(
                barcodeScannerCallback = this.barcodeScannerCallback,
                preview = this.preview,
                surficeProvider = this.surficeProvider,
                context = this.context
            )
        }
    }

}