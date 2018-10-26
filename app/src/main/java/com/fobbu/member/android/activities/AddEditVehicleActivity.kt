package com.fobbu.member.android.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_add_edit_vehicle.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddEditVehicleActivity : AppCompatActivity() {

    private lateinit var webServiceApi: WebServiceApi

    var imageFrom = ""
    var vehicleType = ""

    private var dataList: ArrayList<Any> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_vehicle)
        addClicks()
    }

    private fun addClicks() {

        webServiceApi = getEnv().getRetrofitMulti()

        ivBack.setOnClickListener { finish() }

        tvAddEditVehicle.setOnClickListener {

        }

        ivImage1.setOnClickListener {
            imageFrom = "1"
            showDocPopup()
        }

        ivImage2.setOnClickListener {
            imageFrom = "2"
            showDocPopup()
        }

        ivImage3.setOnClickListener {
            imageFrom = "3"
            showDocPopup()
        }

        ivImage4.setOnClickListener {
            imageFrom = "4"
            showDocPopup()
        }

        ivBike.setOnClickListener {
            vehicleType = "2wheeler"
            ivBike.setImageResource(R.drawable.scooter_red)
            ivCar.setImageResource(R.drawable.car_blue)
        }

        ivCar.setOnClickListener {
            vehicleType = "4wheeler"
            ivBike.setImageResource(R.drawable.scooter_blue)
            ivCar.setImageResource(R.drawable.car_blue)
        }

        tvAddEditVehicle.setOnClickListener {
            addVehicleApi()
        }
    }

    private fun showDocPopup() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Upload Car Images")
        alertDialog.setMessage("Please select from where you want to choose")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery") { dialogInterface, i ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23) {
                //phone state
                val permission1 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
                    makeRequest2()
                } else {
                    openGallery()
                }
            } else {
                openGallery()
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera") { dialogInterface, i ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23) {
                //phone state

                val permission1 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                val permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED
                    || permission3 != PackageManager.PERMISSION_GRANTED
                ) {
                    makeRequest1()
                } else {
                    takePicture()
                }
            } else {
                takePicture()
            }
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    private fun makeRequest1() {
        ActivityCompat.requestPermissions(
            this@AddEditVehicleActivity,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            1
        )
    }


    private fun makeRequest2() {
        ActivityCompat.requestPermissions(
            this@AddEditVehicleActivity,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            2
        )
    }


    private val imageCameraRequest = 100

    private val imageCameraCaptureRequest = 200
    private val placeAutoCompleteResult = 300
    private val REQUEST_PICK_CONTACT = 400

    private var mFileTemp: File? = null

    private fun takePicture() {

        createFileAndDeleteOldFile()

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val apiLevel = android.os.Build.VERSION.SDK_INT

        val mImageCaptureUri: Uri

        mImageCaptureUri = if (apiLevel >= 24) {
            FileProvider.getUriForFile(
                this@AddEditVehicleActivity,
                this.applicationContext.packageName + ".provider", mFileTemp!!
            )
        } else {
            Uri.fromFile(mFileTemp)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
        try {
            startActivityForResult(intent, imageCameraCaptureRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createFileAndDeleteOldFile() {
        mFileTemp =
                File(Environment.getExternalStorageDirectory(), "vehicle_images" + System.currentTimeMillis() + ".jpg")
        if (mFileTemp!!.exists()) {
            mFileTemp!!.delete()
        }
    }

    private fun openGallery() {

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, imageCameraRequest)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", "Permission has been denied by user")
                    showMessageDialog(
                        "You need to allow the permissions from\n" +
                                "Phone Settings -> Apps --> Fobbu Vendor --> Permissions\n" +
                                "to allow the permissions"
                    )

                } else {
                    Log.i("", "Permission has been granted by user")

                    takePicture()
                }
                return
            }
            2 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", "Permission has been denied by user")
                    showMessageDialog(
                        "You need to allow the permissions from\n" +
                                "Phone Settings -> Apps --> Fobbu Vendor --> Permissions\n" +
                                "to allow the permissions"
                    )
                } else {
                    Log.i("", "Permission has been granted by user")

                    openGallery()
                }
                return
            }
        }
    }

    @SuppressLint("Recycle")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {

            imageCameraRequest ->

                try {
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(data!!.data, projection, null, null, null)
                    val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val path = cursor.getString(columnIndex)
                    val imgFile = compressImage(File(path))
                    //val imgFile = File(path)
                    println("IMAGE FILE 1$imgFile")

                    dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage1)
                            "2" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage2)
                            "3" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage3)
                            "4" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage4)
                        }

                    }
                } catch (e: Exception) {
                    Log.e("tag", "Error while creating temp file", e)
                }

            imageCameraCaptureRequest ->

                if (requestCode == imageCameraCaptureRequest && resultCode == RESULT_OK) {

                    val imgFile = compressImage(File(mFileTemp!!.path))
                    println("IMAGE FILE 2  $imgFile")

                    dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage1)
                            "2" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage2)
                            "3" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage3)
                            "4" -> Glide.with(this@AddEditVehicleActivity)
                                .load(imgFile)
                                .into(ivImage4)
                        }
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showMessageDialog(message: String) {
        val alertDialog = AlertDialog.Builder(
            this@AddEditVehicleActivity!!
            , R.style.MyDialogTheme
        ).create()
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Ok"
        ) { dialog
            , _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        alertDialog.show()

        val messageText: TextView = alertDialog!!.findViewById(android.R.id.message)!!

        messageText.gravity = Gravity.LEFT

    }

    private fun compressImage(imgFile: File): File {

        var bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        var myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos)

        val bitmapdata = bos.toByteArray()

        var f = imgFile
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f

    }

    private fun imageRotation(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        var rotatedBitmap: Bitmap = bitmap
        println("orientation $orientation")

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_NORMAL ->
                rotatedBitmap = bitmap
        }

        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }

    //////////////////ADD EDIT VEHICLE API  /////////////////////////
    fun addVehicleApi() {

        val fileList = ArrayList<MultipartBody.Part>()

        for (i in 0 until dataList.size) {
            var imgProfile: MultipartBody.Part? = null
            val file = File(dataList[i].toString())
            // create RequestBody instance from file
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            // MultipartBody.Part is used to send also the actual file name
            imgProfile = MultipartBody.Part.createFormData("photos", file.name, requestFile)
            fileList.add(imgProfile!!)
        }

        /*for (i in dataList.indices)
        {
            var imgProfile: MultipartBody.Part? = null
             val file = File(dataList[i])
            // create RequestBody instance from file
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), dataList[i])
            // MultipartBody.Part is used to send also the actual file name
            imgProfile = MultipartBody.Part.createFormData("photos",  requestFile)
            fileList.add(imgProfile!!)
        }

        docArr.forEach {

            if (it != "") {
                var imgProfile: MultipartBody.Part? = null
               // val file = File(it)
                // create RequestBody instance from file
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), dataList[it])
                // MultipartBody.Part is used to send also the actual file name
                imgProfile = MultipartBody.Part.createFormData("photos",  requestFile)
                fileList.add(imgProfile!!)
            }

            docArrCount++
        }*/


        rlLoader.visibility = View.VISIBLE

        val jsonDoc = JSONObject()

        jsonDoc.put("user_id", "5bbddf7972b7278825487a84"/*CommonClass(this, this).getString("user_id")*/)
        jsonDoc.put("vehicle_brand", etBrand.text.toString().trim())
        jsonDoc.put("vehicle_registration_number", etRegNumber.text.toString().trim())
        jsonDoc.put("vehicle_sub_model", etSubModel.text.toString().trim())
        jsonDoc.put("make_of_year", etYearsOfMake.text.toString().trim())
        jsonDoc.put("vehicle_type", vehicleType)

        val partnerJsonBody = RequestBody.create(MediaType.parse("text/plain"), jsonDoc.toString())

        val map = HashMap<String, RequestBody>()
        map["vehicle"] = partnerJsonBody


        val validateUserApi = webServiceApi.addVehicle(map, fileList)

        validateUserApi.enqueue(object : Callback<MainPojo> {
            override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {
                t!!.stackTrace
                rlLoader.visibility = View.GONE
                println("api failed")
            }

            override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {
                rlLoader.visibility = View.GONE

                try {
                    val mainPojo = response!!.body()

                    println("main pojo data $mainPojo")

                    if (mainPojo!!.success == "true") {

                        println("Success")
                        showMessageDialog(mainPojo.message)
                    } else {
                        CommonClass(
                            this@AddEditVehicleActivity,
                            this@AddEditVehicleActivity
                        ).showToast(mainPojo.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })


    }


    private fun getEnv(): MyApplication {
        return application as MyApplication
    }
}