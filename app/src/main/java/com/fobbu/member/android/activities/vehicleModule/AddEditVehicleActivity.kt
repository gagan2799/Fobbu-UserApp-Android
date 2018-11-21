package com.fobbu.member.android.activities.vehicleModule

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
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditActivityHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditVehiclePresenter
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite

import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_add_edit_vehicle.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap

class AddEditVehicleActivity : AppCompatActivity(),
    AddEditVehicleAcivityView {

    private lateinit var webServiceApi: WebServiceApi
    lateinit var addEditActivityHandler: AddEditActivityHandler

    private var imageFrom = ""
    private var vehicleType = ""

    private var isImageOn1 = false
    private var isImageOn2 = false
    private var isImageOn3 = false
    private var isImageOn4 = false

    private var file1: File? = null
    private var file2: File? = null
    private var file3: File? = null
    private var file4: File? = null

    private var dataList: ArrayList<Any> = ArrayList()

    var fromWhere = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_vehicle)
        addEditActivityHandler=
                AddEditVehiclePresenter(this, this)
        initialise()
        addClicks()
    }

    // all the initialization of the class is done here
    private fun initialise() {

        webServiceApi = getEnv().getRetrofitMulti()

        if (intent.hasExtra("from_where")) {
            fromWhere = "RSA"
            ivBack.visibility = View.GONE
            tvEdit.visibility = View.GONE
            ivList.visibility = View.GONE
            tvEdit.visibility = View.GONE
            tvSkip.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (fromWhere == "RSA")
            startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

        finish()
    }

    // all the clicks  are handled in this method
    private fun addClicks() {

        ivList.setOnClickListener {
            startActivity(Intent(this, VehicleListActivity::class.java))

        }

        tvSkip.setOnClickListener {
            if (fromWhere == "RSA") {
                startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))
                finish()
            }
        }

        imgClose.setOnClickListener {

            rlBigProfile.visibility = View.GONE
        }

        ivBack.setOnClickListener {
            if (rlBigProfile.visibility == View.VISIBLE)
                rlBigProfile.visibility = View.GONE
            else
                finish()
        }

        llPhoto1.setOnClickListener {

            if (isImageOn1) {
                showPopupViewDelete("1")
            } else {
                imageFrom = "1"
                uploadImagesPopup()
            }

        }

        llPhoto2.setOnClickListener {

            if (isImageOn2) {
                showPopupViewDelete("2")
            } else {
                imageFrom = "2"
                uploadImagesPopup()
            }
        }

        llPhoto3.setOnClickListener {
            if (isImageOn3) {
                showPopupViewDelete("3")
            } else {
                imageFrom = "3"
                uploadImagesPopup()
            }
        }

        llPhoto4.setOnClickListener {
            if (isImageOn4) {
                showPopupViewDelete("4")
            } else {
                imageFrom = "4"
                uploadImagesPopup()
            }
        }

        ivBike.setOnClickListener {
            vehicleType = "2wheeler"
            ivBike.setImageResource(R.drawable.scooter_blue)
            ivCar.setImageResource(R.drawable.car_gray)
        }

        ivCar.setOnClickListener {
            vehicleType = "4wheeler"
            ivBike.setImageResource(R.drawable.scooter_gray)
            ivCar.setImageResource(R.drawable.car_blue)
        }

        tvAddEditVehicle.setOnClickListener {

            when {
                etBrand.text.toString() == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_brand),
                    Toast.LENGTH_SHORT
                ).show()
                etRegNumber.text.toString() == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_reg_no),
                    Toast.LENGTH_SHORT
                ).show()
                etSubModel.text.toString() == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_sub_model),
                    Toast.LENGTH_SHORT
                ).show()
                etYearsOfMake.text.toString() == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_year),
                    Toast.LENGTH_SHORT
                ).show()
                vehicleType == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_vehicle_type),
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    if (file1 != null && file1!!.exists())
                        dataList.add(file1!!)

                    if (file2 != null && file2!!.exists())
                        dataList.add(file2!!)

                    if (file3 != null && file3!!.exists())
                        dataList.add(file3!!)

                    if (file4 != null && file4!!.exists())
                        dataList.add(file4!!)

                    addVehicleApi()
                }
            }

        }
    }


    //Method for  deleting selected images
    private fun showPopupViewDelete(s: String) {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Car Images")
        alertDialog.setMessage(null)
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View") { _, _ ->

            when (s) {
                "1" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@AddEditVehicleActivity)
                        .load(file1)
                        .into(imgBig)
                }
                "2" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@AddEditVehicleActivity)
                        .load(file2)
                        .into(imgBig)
                }
                "3" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@AddEditVehicleActivity)
                        .load(file3)
                        .into(imgBig)
                }
                "4" -> {
                    rlBigProfile.visibility = View.VISIBLE
                    Glide.with(this@AddEditVehicleActivity)
                        .load(file4)
                        .into(imgBig)
                }
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete") { _, _ ->
            when (s) {
                "1" -> {
                    file1 = null
                    isImageOn1 = false
                    ivImage1.setImageResource(R.drawable.photo_camera)
                }
                "2" -> {
                    file2 = null
                    isImageOn2 = false
                    ivImage2.setImageResource(R.drawable.photo_camera)
                }
                "3" -> {
                    file3 = null
                    isImageOn3 = false
                    ivImage3.setImageResource(R.drawable.photo_camera)
                }
                "4" -> {
                    file4 = null
                    isImageOn4 = false
                    ivImage4.setImageResource(R.drawable.photo_camera)
                }
            }
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    //Method for uploading car images
    private fun uploadImagesPopup() {

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


    // Method for opening camera and capturing images
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

    //Method for creating files and deleting old ones
    private fun createFileAndDeleteOldFile() {
        mFileTemp =
                File(Environment.getExternalStorageDirectory(), "vehicle_images" + System.currentTimeMillis() + ".jpg")
        if (mFileTemp!!.exists()) {
            mFileTemp!!.delete()
        }
    }

    //Method for opening gallery
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

                    //dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage1)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                            "2" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage2)
                                isImageOn2 = true
                                file2 = imgFile
                            }
                            "3" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage3)
                                isImageOn3 = true
                                file3 = imgFile
                            }
                            "4" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage4)
                                isImageOn4 = true
                                file4 = imgFile
                            }
                        }

                    }
                } catch (e: Exception) {
                    Log.e("tag", "Error while creating temp file", e)
                }

            imageCameraCaptureRequest ->

                if (requestCode == imageCameraCaptureRequest && resultCode == RESULT_OK) {

                    val imgFile = compressImage(File(mFileTemp!!.path))
                    println("IMAGE FILE 2  $imgFile")

                    // dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage1)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                            "2" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage2)
                                isImageOn2 = true
                                file2 = imgFile
                            }
                            "3" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage3)
                                isImageOn3 = true
                                file3 = imgFile
                            }
                            "4" -> {
                                Glide.with(this@AddEditVehicleActivity)
                                    .load(imgFile)
                                    .into(ivImage4)
                                isImageOn4 = true
                                file4 = imgFile
                            }
                        }
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    //Method to dislpay message to the user about the permission required by the application
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
            finish()
        }

        alertDialog.show()

        val messageText: TextView = alertDialog!!.findViewById(android.R.id.message)!!

        messageText.gravity = Gravity.LEFT

    }


    //Method for compressing image
    private fun compressImage(imgFile: File): File {
        val bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        val myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos)

        val bitmapdata = bos.toByteArray()

        val f = imgFile
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f

    }

    // method for changing the orientation of the images
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


    // Method for rotating image
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }

    //////////////////ADD EDIT VEHICLE API  /////////////////////////

    //Add vehicle API (API-users/vehicles)
    private fun addVehicleApi() {

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

     //  rlLoader.visibility = View.VISIBLE

        val jsonDoc = JSONObject()

        jsonDoc.put("user_id", CommonClass(this, this).getString("_id"))
        jsonDoc.put("vehicle_brand", etBrand.text.toString().trim())
        jsonDoc.put("vehicle_registration_number", etRegNumber.text.toString().trim())
        jsonDoc.put("vehicle_sub_model", etSubModel.text.toString().trim())
        jsonDoc.put("make_of_year", etYearsOfMake.text.toString().trim())
        jsonDoc.put("vehicle_type", vehicleType)

        val partnerJsonBody = RequestBody.create(MediaType.parse("text/plain"), jsonDoc.toString())

        val map = HashMap<String, RequestBody>()
        map["vehicle"] = partnerJsonBody

        val tokenHeader = CommonClass(this, this).getString("x_access_token")

        addEditActivityHandler.sendAddEditData(map,fileList,tokenHeader)

    }

    // Add Vehicle Api Response (API-users/vehicles)
    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        //rlLoader.visibility = View.GONE

        if (mainPojo.success =="true")
        {

            if (fromWhere == "RSA") {
                callUpdateVehicle(mainPojo.getData()._id)
            } else {

                println("Success")
                CommonClass(
                    this@AddEditVehicleActivity,
                    this@AddEditVehicleActivity
                ).showToast(mainPojo.message)

                finish()
            }
        }else{

            CommonClass(
                this@AddEditVehicleActivity,
                this@AddEditVehicleActivity
            ).showToast(mainPojo.message)
        }
    }

    //////////////////UPDATE FOBBU VEHICLE API /////////////////////////

    //Update FOBBU Vehicle API (API-users/requests)
    private fun callUpdateVehicle(id: String) {

        if (CommonClass(this, this).checkInternetConn(this)) {

            val token = CommonClass(this@AddEditVehicleActivity, this@AddEditVehicleActivity)
                .getString("x_access_token")

            val requestId = CommonClass(this@AddEditVehicleActivity, this@AddEditVehicleActivity)
                .getString("fobbu_request_id")
           // rlLoader.visibility = View.VISIBLE
            val hash = HashMap<String, String>()
            hash["request_id"] = requestId
            hash["vehicle"] = id

            addEditActivityHandler.findFobbuRequestUpdateVehicle(hash,token)

        } else {

            CommonClass(this, this).internetIssue(this)
        }
    }


    // Update FOBBU Vehicle API Response (API-users/requests)
    override fun onRequestSuccessUpdateVehicle(mainPojo: MainPojo) {
       // rlLoader.visibility = View.GONE
        if (mainPojo!!.success == "true") {

            startActivity(
                Intent(
                    this@AddEditVehicleActivity
                    , WaitingScreenWhite::class.java
                ).putExtra("from_where", "new_vehicle_added")
            )
            finish()

        } else {

            CommonClass(this@AddEditVehicleActivity, this@AddEditVehicleActivity)
                .showToast(mainPojo.message)
        }

    }

    override fun showLoader() {
        rlLoader.visibility=View.VISIBLE
    }

    override fun hideLoader() {
        rlLoader.visibility=View.GONE
    }



    private fun getEnv(): MyApplication {
        return application as MyApplication
    }
}