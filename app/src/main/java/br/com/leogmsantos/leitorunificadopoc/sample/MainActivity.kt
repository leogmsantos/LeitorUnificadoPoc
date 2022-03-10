package br.com.leogmsantos.leitorunificadopoc.sample

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import br.com.leogmsantos.leitorunificadopoc.sample.databinding.ActivityMainBinding
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.BarcodeScannerManager
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.application.BarcodeScannerApplication
import br.com.leogmsantos.leitorunificadopoc.sdk_scannercode.callback.BarcodeScannerCallback
import com.google.mlkit.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var scannerApplication: BarcodeScannerApplication
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val multiPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.entries.size < 1) {
                Toast.makeText(this, "Por favor, aceite as permissões para continuar.", Toast.LENGTH_SHORT).show()
            } else {
                binding.viewFinder.post {
                    scannerApplication.startBarcodeScanner()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestAllPermissions()
        openModule()
        onClick()
    }

    private fun requestAllPermissions() {
        multiPermissionCallback.launch(
            REQUIRED_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openModule(){
        scannerApplication = BarcodeScannerManager.Builder(
            barcodeScannerCallback = object : BarcodeScannerCallback{
                override fun onScannerResult(result: MutableList<Barcode>) {
                    if (result[0].format == Barcode.FORMAT_QR_CODE) {
                        binding.lblCopiedCode.text = "QR Code copiado:"
                        binding.textCopy.text = result[0].rawValue
                        binding.textCopy.visibility = View.VISIBLE
                    } else {
                        binding.lblCopiedCode.text = "Código de Barras copiado:"
                        binding.textCopy.text = result[0].rawValue
                        binding.textCopy.visibility = View.VISIBLE
                    }
                }
            },
            binding.viewFinder,
            binding.viewFinder.surfaceProvider,
            this@MainActivity
        ).build()

        if (allPermissionsGranted()){
            binding.viewFinder.post {
                scannerApplication.startBarcodeScanner()
            }
        }else{
            requestAllPermissions()
        }
    }

    private fun onClick(){
        binding.textCopy.setOnClickListener {
            val myClipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip = ClipData.newPlainText("barcode data", binding.textCopy.text.toString())
            myClipboard.setPrimaryClip(myClip)
            Toast.makeText(this, "Código copiado.", Toast.LENGTH_SHORT).show()
        }
    }

}