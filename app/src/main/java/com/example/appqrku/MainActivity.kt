package com.example.appqrku

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.appqrku.databinding.ActivityMainBinding
import com.maxkeppeler.sheets.info.InfoSheet


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var codeScanner : CodeScanner
    private lateinit var viewModel : MainVIewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Get permision
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        //view model
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainVIewModel::class.java]

        viewModel.result.observe(this){resultQR->
            binding.txtResultScann.text = resultQR.toString()
        }
        //QR
        codeScanner = CodeScanner(this, binding.scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread{
                viewModel.setData(it.text)
                infoSheet(it.text)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

//        binding.scannerView.setOnClickListener{
//            codeScanner.startPreview()
//        }

        binding.btnScann.setOnClickListener {
            codeScanner.startPreview()
            viewModel.setData("...")
        }
    }

//    private fun sheet(){
//        InputSheet().show(this){
//            title("Result QR Code")
//            with(InputEditText{
//                required()
//                label("Result")
//                defaultValue("AAAAA")
//
//            })
//        }
//    }

    private fun infoSheet(msg : String){
        InfoSheet().show(this){
            title("Go To Result QR Scan")
            content("Result QR : $msg")

            onPositive("Go") {
                move(msg)
            }

            onNegative ("Cancel") {
                Toast.makeText(this@MainActivity, "Not action", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun move(content : String){
        val move : Intent = Uri.parse(content.toString()).let { web ->
            Intent(Intent.ACTION_VIEW, web)
        }

        try {
            startActivity(move)
        }catch (e : ActivityNotFoundException){
            Toast.makeText(this, "Message error ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

//    override fun onResume() {
//        super.onResume()
//        codeScanner.startPreview()
//    }

    override fun onDestroy() {
        codeScanner.releaseResources()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}