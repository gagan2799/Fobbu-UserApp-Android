package com.fobbu.member.android.activities.vehicleModule

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
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
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.adapter.BigProfileViewAdapter
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditActivityHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditVehiclePresenter
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite

import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CardsPagerTransformerBasic
import com.fobbu.member.android.utils.CommonClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_edit_vehicle.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddEditVehicleActivity : AppCompatActivity(),
    AddEditVehicleAcivityView,DatePickerDialog.OnDateSetListener, NumberPicker.OnValueChangeListener
{
    private lateinit var webServiceApi: WebServiceApi

    lateinit var addEditActivityHandler: AddEditActivityHandler

    private var imageFrom = ""

    private var vehicleType = ""

    private var isImageOn1 = false

    private var isImageOn2 = false

    lateinit var viewAdapter:BigProfileViewAdapter

    private var isImageOn3 = false

    var bigProfileList=ArrayList<Any>()

    private var isImageOn4 = false

    private var file1: File? = null

    private var file2: File? = null

    private var file3: File? = null

    private var file4: File? = null

    private var dataList: ArrayList<Any> = ArrayList()

    var year=0

    var month=0

    var day=0

    private var hashMapEdit = HashMap<String, Any>()

    private var fromWhere = ""

    private var listImagesEdit = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit_vehicle)

        initialise()

        addClicks()
    }

    // all the initialization of the class is done here
    private fun initialise()
    {
        addEditActivityHandler = AddEditVehiclePresenter(this, this)

        tvEdit.visibility=View.GONE

        webServiceApi = getEnv().getRetrofitMulti()

        if (intent.hasExtra("from_where"))
        {
            fromWhere = "RSA"

            ivBack.visibility = View.GONE

            tvEdit.visibility = View.GONE

            ivList.visibility = View.GONE

            tvEdit.visibility = View.GONE

            tvSkip.visibility = View.VISIBLE
        }

        if (intent.hasExtra("vehicle_edit"))
        {
            hashMapEdit = intent.getSerializableExtra("vehicle_edit") as HashMap<String, Any>

            etBrand.setText(hashMapEdit["vehicle_brand"].toString())

            etRegNumber.setText(hashMapEdit["vehicle_registration_number"].toString())

            etSubModel.setText(hashMapEdit["vehicle_sub_model"].toString())

            etYearsOfMake.setText(hashMapEdit["make_of_year"].toString())

            vehicleType = hashMapEdit["vehicle_type"].toString()

            if (vehicleType == "2wheeler")
            {
                ivBike.setImageResource(R.drawable.scooter_blue)

                ivCar.setImageResource(R.drawable.car_gray)
            }
            else
            {
                ivBike.setImageResource(R.drawable.scooter_gray)

                ivCar.setImageResource(R.drawable.car_blue)
            }

            //tvNew.setTextColor(resources.getColor(R.color.color_grey))

            tvEdit.visibility=View.VISIBLE

            tvEdit.setTextColor(resources.getColor(R.color.red))

            viewEdit.visibility = View.VISIBLE

            viewNew.visibility = View.GONE

            tvNew.visibility = View.GONE

            tvAddEditVehicle.text = resources.getString(R.string.save_changes)

            tvHeading.text = resources.getString(R.string.edit_vehicle)

            listImagesEdit = hashMapEdit["images"] as ArrayList<String>

            if (listImagesEdit.size>0)
            {
                for (i in listImagesEdit.indices)
                    bigProfileList.add(listImagesEdit[i])
            }

            for (i in listImagesEdit.indices)
            {
                when (i)
                {
                    0 ->
                    {
                        if (listImagesEdit[i] != "")
                            Picasso.get().load(listImagesEdit[i])

                                .fit().centerInside()

                                .error(R.drawable.photo_camera)

                                .into(ivImage1)

                        isImageOn1 = true
                    }

                    1 ->
                    {
                        if (listImagesEdit[i] != "")
                            Picasso.get().load(listImagesEdit[i])

                                .fit().centerInside()

                                .error(R.drawable.photo_camera)

                                .into(ivImage2)

                        isImageOn2 = true
                    }

                    2 ->
                    {
                        if (listImagesEdit[i] != "")
                            Picasso.get().load(listImagesEdit[i])

                                .fit().centerInside()

                                .error(R.drawable.photo_camera)

                                .into(ivImage3)

                        isImageOn3 = true
                    }

                    else ->
                    {
                        Picasso.get().load(listImagesEdit[i])

                            .fit().centerInside()

                            .error(R.drawable.photo_camera)

                            .into(ivImage4)

                        isImageOn4 = true
                    }
                }
            }
        }
    }

    override fun onBackPressed()
    {

        if (rlBigProfile.visibility==View.VISIBLE)
        {
            rlBigProfile.visibility=View.GONE

            bigProfileList.clear()
        }
        else{
            if (fromWhere == "RSA")
                startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

            if (intent.hasExtra("vehicle_edit"))
            {
                startActivity(Intent(this,VehicleListActivity::class.java)
                    .setFlags
                        (Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }

            finish()
        }

    }

    // all the clicks  are handled in this method
    private fun addClicks()
    {
        ivList.setOnClickListener {
            startActivity(Intent(this, VehicleListActivity::class.java))

            finish()
        }

        tvSkip.setOnClickListener {
            if (fromWhere == "RSA")
            {
                startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

                finish()
            }
        }

        imgClose.setOnClickListener {
            rlBigProfile.visibility = View.GONE

            bigProfileList.clear()
        }

        ivBack.setOnClickListener {

            if (intent.hasExtra("vehicle_edit"))
            {
                startActivity(Intent(this,VehicleListActivity::class.java)
                    .setFlags
                        (Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }
            finish()

        }

        etYearsOfMake.setOnClickListener{
            dialogYearShow()
        }


        llPhoto1.setOnClickListener {
            if (isImageOn1)
            {
                showPopupViewDelete("1")
            }
            else
            {
                imageFrom = "1"

                uploadImagesPopup()
            }

        }

        llPhoto2.setOnClickListener {
            if (isImageOn2)
            {
                showPopupViewDelete("2")
            }
            else
            {
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
            val brand= checkAndRemoveSpace(etBrand.text.toString())

            val regNo=  checkAndRemoveSpace(etRegNumber.text.toString())

            val subModel=checkAndRemoveSpace(etSubModel.text.toString())

            val year=checkAndRemoveSpace(etYearsOfMake.text.toString())
            when {
                brand == "" ->
                {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.add_brand),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                regNo == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_reg_no),
                    Toast.LENGTH_SHORT
                ).show()
                subModel == "" -> Toast.makeText(
                    this,
                    resources.getString(R.string.add_sub_model),
                    Toast.LENGTH_SHORT
                ).show()
                year == "" -> Toast.makeText(
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


                    if (CommonClass(this,this).checkInternetConn(this))
                    {
                        if (tvAddEditVehicle.text == resources.getString(R.string.save_changes))
                            editVehicleApi()

                        else
                            addVehicleApi()
                    }
                    else
                        CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable))
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
            println("list image edit::::::$listImagesEdit")
            println("list image edit size::::::${listImagesEdit.size}")
            when (s) {
                "1" -> {

                    if (tvHeading.text == resources.getString(R.string.edit_vehicle)) {
                        if (listImagesEdit.size<1)
                        {
                            addFileToList()
                        }

                    } else {
                        addFileToList()
                    }

                    setUpSlider(0)
                    println("big profile added 1:::: $bigProfileList")
                    rlBigProfile.visibility = View.VISIBLE

                }
                "2" -> {
                    if (tvHeading.text == resources.getString(R.string.edit_vehicle)) {
                        if (listImagesEdit.size<2)
                        {
                            addFileToList()
                        }
                    }
                    else
                    {
                        addFileToList()
                    }
                    setUpSlider(1)

                    println("big profile added 2:::: $bigProfileList")
                    rlBigProfile.visibility = View.VISIBLE
                }
                "3" -> {
                    if (tvHeading.text == resources.getString(R.string.edit_vehicle)) {
                        if (listImagesEdit.size<3)
                        {
                            addFileToList()
                        }
                    }
                    else
                    {
                        addFileToList()
                    }
                    setUpSlider(2)

                    println("big profile added 3:::: $bigProfileList")
                    rlBigProfile.visibility = View.VISIBLE
                }
                "4" -> {
                    if (tvHeading.text == resources.getString(R.string.edit_vehicle)) {
                        if (listImagesEdit.size<4)
                        {
                            addFileToList()
                        }
                    } else {
                        addFileToList()
                    }
                    setUpSlider(3)

                    println("big profile added 4:::: $bigProfileList")
                    rlBigProfile.visibility = View.VISIBLE
                }
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete") { _, _ ->
            when (s) {
                "1" -> {

                    if (tvHeading.text != resources.getString(R.string.edit_vehicle)) {
                        file1 = null
                    } else {
                        if (listImagesEdit.size<1)
                            file1=null
                        else
                        {
                            if (listImagesEdit.size>=1)
                                listImagesEdit.removeAt(0)
                        }

                    }
                    isImageOn1 = false
                    ivImage1.setImageResource(R.drawable.photo_camera)
                }
                "2" -> {
                    if (tvHeading.text != resources.getString(R.string.edit_vehicle)) {
                        file2 = null
                    } else {
                        if (listImagesEdit.size<2)
                            file2=null
                        else
                        {
                            if (listImagesEdit.size==2)
                                listImagesEdit.removeAt(1)
                            else
                                listImagesEdit.removeAt(0)
                        }
                    }
                    isImageOn2 = false
                    ivImage2.setImageResource(R.drawable.photo_camera)
                }
                "3" -> {
                    if (tvHeading.text != resources.getString(R.string.edit_vehicle)) {
                        file3 = null
                    } else {
                        if (listImagesEdit.size<3)
                            file3=null
                        else
                        {
                            if (listImagesEdit.size==3)
                                listImagesEdit.removeAt(2)
                            else
                                listImagesEdit.removeAt(0)
                        }
                    }
                    isImageOn3 = false
                    ivImage3.setImageResource(R.drawable.photo_camera)
                }
                "4" -> {
                    if (tvHeading.text != resources.getString(R.string.edit_vehicle)) {
                        file4 = null
                    } else {
                        if (listImagesEdit.size<4)
                            file1=null
                        else
                        {
                            if (listImagesEdit.size==4)
                                listImagesEdit.removeAt(3)
                            else
                                listImagesEdit.removeAt(0)
                        }
                    }
                    isImageOn4 = false
                    ivImage4.setImageResource(R.drawable.photo_camera)
                }
            }
            bigProfileList.clear()
            if (listImagesEdit.isNotEmpty())
            {
                for (i in listImagesEdit.indices)
                {
                    bigProfileList.add(listImagesEdit[i])
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
    private fun uploadImagesPopup()
    {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle("Upload Car Images")

        alertDialog.setMessage("Please select from where you want to choose")

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery") { dialogInterface, i ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23)
            {
                //phone state
                val permission1 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
                    makeRequest2()
                }
                else
                    openGallery()
            }
            else
                openGallery()
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera") { _, _ ->
            val apiLevel = android.os.Build.VERSION.SDK_INT

            if (apiLevel >= 23)
            {
                //phone state
                val permission1 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

                val permission2 =
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                val permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

                if (permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED
                    || permission3 != PackageManager.PERMISSION_GRANTED
                )
                    makeRequest1()
                else
                    takePicture()

            }
            else
                takePicture()
        }

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }

    private fun makeRequest1()
    {
        ActivityCompat.requestPermissions(
            this@AddEditVehicleActivity,
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",

                "android.permission.READ_EXTERNAL_STORAGE",

                "android.permission.CAMERA"
            ),
            1)
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
                                "Phone Settings -> Apps --> Fobbu  --> Permissions\n" +
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
    // Method for compressing image
    private fun compressImage(imgFile: File): File {
        //////old code
        val bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        val myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        val nh = (myBitmap.height * (512.0 / myBitmap.width)).toInt()

        val scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true)

        scaled.compress(Bitmap.CompressFormat.JPEG, 80, bos)

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
    private fun addVehicleApi()
    {
        val fileList = ArrayList<MultipartBody.Part>()

        for (i in 0 until dataList.size)
        {
            var imgProfile: MultipartBody.Part? = null

            val file = File(dataList[i].toString())

            // create RequestBody instance from file
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

            // MultipartBody.Part is used to send also the actual file name
            imgProfile = MultipartBody.Part.createFormData("photos", file.name, requestFile)

            fileList.add(imgProfile!!)
        }

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

        addEditActivityHandler.sendAddEditData(map, fileList, tokenHeader)
    }

    // Add Vehicle Api Response (API-users/vehicles)
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            if (fromWhere == "RSA")
            {
                callUpdateVehicle(mainPojo.getData()._id)
            }
            else
            {
                println("Success")
                CommonClass(
                    this@AddEditVehicleActivity,

                    this@AddEditVehicleActivity
                ).showToast(mainPojo.message)

                startActivity(Intent(this,VehicleListActivity::class.java)
                    .putExtra("vehicle_type",vehicleType))

                finish()
            }
        }
        else
        {
            CommonClass(
                this@AddEditVehicleActivity,
                this@AddEditVehicleActivity
            ).showToast(mainPojo.message)
        }
    }


    //EDIT vehicle API (API-users/vehicles)
    private fun editVehicleApi()
    {
        val fileList = ArrayList<MultipartBody.Part>()

        for (i in 0 until dataList.size)
        {
            var imgProfile: MultipartBody.Part? = null

            val file = File(dataList[i].toString())

            // create RequestBody instance from file
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

            // MultipartBody.Part is used to send also the actual file name
            imgProfile = MultipartBody.Part.createFormData("photos", file.name, requestFile)

            fileList.add(imgProfile!!)
        }

        val list=JSONArray()

        for (i in listImagesEdit.indices)
        {
            if(listImagesEdit[i].contains("vehicles/"))
            {
                val separated = listImagesEdit[i].split("vehicles/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val jsonObject = separated[1]

                list.put(jsonObject)
            }
        }

        val bodyUserId = RequestBody.create(MediaType.parse("text/plain"), CommonClass(this, this).getString("_id"))

        val bodyVehicleBrand = RequestBody.create(MediaType.parse("text/plain"), etBrand.text.toString().trim())

        val bodyVehicleRegistrationNumber = RequestBody.create(MediaType.parse("text/plain"), etRegNumber.text.toString().trim())

        val bodyVehicleSubModel = RequestBody.create(MediaType.parse("text/plain"), etSubModel.text.toString().trim())

        val bodyMakeOfYear = RequestBody.create(MediaType.parse("text/plain"), etYearsOfMake.text.toString().trim())

        val bodyVehicleType = RequestBody.create(MediaType.parse("text/plain"), vehicleType)

        val bodyVehicleId = RequestBody.create(MediaType.parse("text/plain"), hashMapEdit["_id"].toString())

        val bodyVehicleImages = RequestBody.create(MediaType.parse("text/plain"), list.toString())

        val map = HashMap<String, RequestBody>()

        map["user_id"] = bodyUserId

        map["vehicle_brand"] = bodyVehicleBrand

        map["vehicle_registration_number"] = bodyVehicleRegistrationNumber

        map["vehicle_sub_model"] = bodyVehicleSubModel

        map["make_of_year"] = bodyMakeOfYear

        map["vehicle_type"] = bodyVehicleType

        map["vehicle_id"] = bodyVehicleId

        map["images"] = bodyVehicleImages

        val tokenHeader = CommonClass(this, this).getString("x_access_token")

        addEditActivityHandler.sendEditData(map, fileList, tokenHeader)

    }


    override fun onRequestSuccessReportEdit(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            CommonClass(
                this@AddEditVehicleActivity,
                this@AddEditVehicleActivity
            ).showToast(mainPojo.message)

            startActivity(Intent(this,VehicleListActivity::class.java)
                .putExtra("vehicle_type",vehicleType))
            finish()
        } else {

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
                .getString(RsaConstants.ServiceSaved.fobbuRequestId)
            // rlLoader.visibility = View.VISIBLE
            val hash = HashMap<String, String>()
            hash["request_id"] = requestId
            hash["vehicle"] = id

            addEditActivityHandler.findFobbuRequestUpdateVehicle(hash, token)

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
                    .putExtra("vehicle_type",vehicleType)
            )
            finish()

        } else {

            CommonClass(this@AddEditVehicleActivity, this@AddEditVehicleActivity)
                .showToast(mainPojo.message)
        }

    }

    override fun showLoader() {
        rlLoader.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        rlLoader.visibility = View.GONE
    }

    private fun getEnv(): MyApplication {
        return application as MyApplication
    }

    override fun onDeleteVehicleSuccessUpdateVehicle(mainPojo: MainPojo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        year=p1
        month=p2
        day=p3
    }

    private fun checkAndRemoveSpace(text:String):String
    {
        if (text.startsWith(""))
            return text.trim()

        return text
    }

    private fun addFileToList()
    {

        if (file1!=null)
        {
            bigProfileList.add(file1.toString())
        }


        if (file2!=null)
        {
            bigProfileList.add(file2.toString())
        }


        if (file3!=null)
        {
            bigProfileList.add(file3.toString())
        }


        if (file4!=null)
        {
            bigProfileList.add(file4.toString())
        }
    }


    // functiom for setting up slider
    private fun  setUpSlider(pos:Int)
    {
        viewAdapter= BigProfileViewAdapter(this,bigProfileList)

        vpBigProfile.adapter=viewAdapter

        val density = resources.displayMetrics.density
        val partialWidth = (16 * density).toInt() // 16dp
        val pageMargin = (8 * density).toInt() // 8dp

        val viewPagerPadding = partialWidth + pageMargin

        vpBigProfile.pageMargin = pageMargin

        vpBigProfile.setPadding(viewPagerPadding, 0, viewPagerPadding, 0)

        vpBigProfile.setPageTransformer(true, CardsPagerTransformerBasic(2,5, 0.7F))

        circleIndicator.setViewPager(vpBigProfile)
        if (pos>=bigProfileList.size)
        {
            vpBigProfile.currentItem = bigProfileList.size-1
        }
        else
            vpBigProfile.currentItem = pos
    }

    @SuppressLint("SetTextI18n")
    fun dialogYearShow()
    {
        val d: Dialog =  Dialog(this)
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.inflate_dialog_year)
        val b1:TextView  =  d.findViewById(R.id.button1);
        val np: NumberPicker  =  d.findViewById(R.id.numberPicker1)
        np.maxValue = 2019
        np.minValue = 1970
        np.wrapSelectorWheel = false
        np.setOnValueChangedListener(this)
        b1.setOnClickListener {
            etYearsOfMake.setText(""+(np.value))
            d.dismiss()
        }
        d.show()
    }


    override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {
        Log.i("value is",""+p2)
    }

}
