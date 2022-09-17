package com.example.appqrku.presentation

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.appqrku.R
import com.example.appqrku.databinding.ActivityMainBinding
import com.example.appqrku.helper.ViewModelFactory
import com.maxkeppeler.sheets.info.InfoSheet
import com.maxkeppeler.sheets.options.DisplayMode
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import org.koin.androidx.viewmodel.ext.android.viewModel
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var codeScanner : CodeScanner
//    private lateinit var viewModel : MainVIewModel
    val viewModel : MainVIewModel by viewModel<MainVIewModel>( )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        hideSystemUI()

        //Get permision
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


        //view model
//        val factory = ViewModelFactory.getInstance()
//        viewModel = ViewModelProvider(this, factory)[MainVIewModel::class.java]

        viewModel.result.observe(this){resultQR->
            binding.txtResultScann.text = resultQR.result
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
//                infoSheet(it.text)
                optionSheet(it.text)
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


    private fun infoSheet(msg : String){
        InfoSheet().show(this){
            title("Go To Result QR Scan")
            content("Result QR : $msg")

            onPositive("Go") {
                move(msg)
            }

            onNegative ("COPY") {
               copy(msg)
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

    private fun copy(msg: String){
        val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoard.setPrimaryClip(ClipData.newPlainText("", msg))
    }

    private fun optionSheet(msg : String){
        OptionsSheet().show(this){
            title("Go To Result QR Scan")
            displayMode(DisplayMode.LIST)
            preventIconTint(true)
            with(
                Option(R.drawable.ic_baseline_content_copy_24, "COPY"),
                Option(R.drawable.ic_baseline_send_24, "GO")
            )

            onPositive{Index : Int, option: Option ->
                when(Index){
                    0 -> {
                        copy(msg)
                        toast(info)
                    }
                    1 -> {
                        move(msg)
                    }
                }
            }
        }
    }

    private fun toast(type : String){

        when(type){
            succes -> {
                MotionToast.createToast(this,
                    "Hurray success 😍",
                    "Upload Completed successfully!",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }
            info -> {
                MotionToast.createToast(this,
                    "INFO",
                    "Succes Copy!",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }
            warning -> {
                MotionToast.createToast(this,
                    "Warning",
                    "Note Have Permision",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }

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
                toast(warning)
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    //for hide top bar
    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            }
        }
//        else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.hide()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val succes = "SUCCES"
        const val info = "INFO"
        const val warning = "WARNING"
    }
}