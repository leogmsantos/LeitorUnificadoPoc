package br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback

import com.google.mlkit.vision.barcode.Barcode

interface BarcodeScannerCallback {
    fun onScannerResult(result: MutableList<Barcode>)
}