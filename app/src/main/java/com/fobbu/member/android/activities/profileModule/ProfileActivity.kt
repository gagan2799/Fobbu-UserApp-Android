package com.fobbu.member.android.activities.profile

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import com.fobbu.member.android.R.drawable.filter
import com.fobbu.member.android.activities.profile.presenter.ProfileHandler
import com.fobbu.member.android.activities.profile.presenter.ProfilePresenter
import com.fobbu.member.android.activities.profileModule.langauage.LanguageActivity
import com.fobbu.member.android.activities.profile.view.ProfileView
import com.fobbu.member.android.activities.profileModule.profile.adapter.ProfileLangaugeAdapter
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class ProfileActivity : AppCompatActivity(),ActivityView,ProfileView
{
    private val fileList = ArrayList<MultipartBody.Part>()

    private var first=""

    private var last=""

    private var hasPic=false

    ////////////////FOR  IMAGE/////////////////////////
    private var isImageOn1 = false

    private var file1: File? = null

    private var dataList: ArrayList<Any> = ArrayList()

    private var imageFrom = ""

    private var checKBasic=true

    lateinit var  selectedLanguageList:ArrayList<HashMap<String,Any>>

    private var mFileTemp: File? = null

    lateinit var gender:Array<String>

    private val imageCameraRequest = 100

    private lateinit var spinnerAdapter:ArrayAdapter<String>

    private val imageCameraCaptureRequest = 200

    lateinit var commonClass:CommonClass


    private val blockCharacterSet = "~#^|$%&*!"

    lateinit var inputFilter:InputFilter

    private lateinit var profileHandler:ProfileHandler

    private lateinit var profileAdapter: ProfileLangaugeAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        initView()

        clicks()

        chooseImagesClick()
    }


    override fun onResume()
    {
        super.onResume()

        if (commonClass.getStringList(RsaConstants.Constants.selectedLanguageList).isNotEmpty())
        {
            selectedLanguageList=commonClass.getStringList(RsaConstants.Constants.selectedLanguageList)

            setSelectedLanguageRecycler()
        }
    }

    private fun initView()
    {
        inputFilter= InputFilter { p0, p1, p2, p3, p4, p5 ->
            if (p0 != null && blockCharacterSet.contains(("" + p0))) {
                return@InputFilter ""
            }
            ""
        }

        commonClass= CommonClass(this,this)

        selectedLanguageList= ArrayList()

        gender=arrayOf(this.resources.getString(R.string.selectGender),this.resources.getString(R.string.male),
            this.resources.getString(R.string.female),this.resources.getString(R.string.not_specified))

        civPicProfile.isEnabled=false

        setDataInView()

        profileHandler=ProfilePresenter(this,this,this)

        ivSearchToolbar.visibility= View.INVISIBLE

        tvEditToolbar.visibility= View.VISIBLE

        spinnerAdapter= ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,gender)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerProfile.adapter=spinnerAdapter
    }

    private fun setSelectedLanguageRecycler()
    {
        rvSelectedLangauges.visibility=View.VISIBLE

     profileAdapter= ProfileLangaugeAdapter(this,selectedLanguageList)

        rvSelectedLangauges.layoutManager=GridLayoutManager(this,2)

        rvSelectedLangauges.adapter=profileAdapter
    }


    // function for setting data form preference into view
    private fun setDataInView()
    {
        val fullName=commonClass.getString("first_name")+" "+commonClass.getString("last_name")

        etNameProfile.setText(fullName)

        etNameKycProfile.setText(fullName)

        etNumberProfile.setText(commonClass.getString("mobile_number"))

        etEmailProfile.setText(commonClass.getString("email"))

        if(CommonClass(this,this).getString("user_image")!="")
        {
            hasPic=true

            val urlProfile = CommonClass(this,this).getString("user_url")+
                    CommonClass(this,this).getString("user_image")

            Picasso.get().load(urlProfile)
                .error(R.drawable.dummy_pic)
                .into(civPicProfile)
        }

        Handler().postDelayed({
            if (commonClass.getString("gender")==resources.getString(R.string.other))
            tvGenderProfile.text=resources.getString(R.string.not_specified)

            else
            tvGenderProfile.text=commonClass.getString("gender")
        },1000)
    }

    fun clicks()
    {
        tvEditToolbar.setOnClickListener {
            tvEditToolbar.visibility=View.GONE

            etNameProfile.isEnabled=true

            etEmailProfile.isEnabled=true

            rlGenderProfile.isEnabled=true

            etNameKycProfile.isEnabled=true

            tvDobKycProfile.isEnabled=true

            etPANProfile.isEnabled=true

           // etPANProfile.filters = object: InputFilter[] { filter }

            etAdharProfile.isEnabled=true

            civPicProfile.isEnabled=true

            tvLanguageProfile.isEnabled=true

            tvAddDetailsProfile.visibility=View.VISIBLE
        }

        ivBackButton.setOnClickListener {
            finish()
        }

        rlGenderProfile.setOnClickListener {
            spinnerProfile.performClick()
        }

        tvLanguageProfile.setOnClickListener {
           startActivity(Intent(this,LanguageActivity::class.java))
        }

        spinnerProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tvGenderProfile.text=spinnerProfile.selectedItem.toString()

                if (tvGenderProfile.text.toString()=="Relationship")
                    tvGenderProfile.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tvGenderProfile.setTextColor(resources.getColor(R.color.color_grey))

            } // to close the onItemSelected
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        tvDobKycProfile.setOnClickListener {
            commonClass.openDatePicker(this,"Start", tvDobKycProfile)

            tvDobKycProfile.setTextColor(resources.getColor(R.color.black))
        }


        llBasicProfile.setOnClickListener {
            tvBasicProfile.setTextColor(resources.getColor(R.color.red))

            viewBasic.visibility=View.VISIBLE

            rlBasicProfile.visibility=View.VISIBLE

            checKBasic=true

            civPicProfile.visibility=View.VISIBLE

            rlKycProfile.visibility=View.GONE

            tvKycProfile.setTextColor(resources.getColor(R.color.color_grey))

            viewKyc.visibility=View.INVISIBLE
        }

        llKycProfile.setOnClickListener {
            tvBasicProfile.setTextColor(resources.getColor(R.color.color_grey))

            viewBasic.visibility=View.INVISIBLE

            checKBasic=false

            rlBasicProfile.visibility=View.GONE

            civPicProfile.visibility=View.INVISIBLE

            tvDobKycProfile.setTextColor(resources.getColor(R.color.color_grey))

            rlKycProfile.visibility=View.VISIBLE

            tvKycProfile.setTextColor(resources.getColor(R.color.red))

            viewKyc.visibility=View.VISIBLE
        }

        tvAddDetailsProfile.setOnClickListener {
            if (checKBasic)
                updateUser()
            else
                updateKyc()
        }
    }

    // method for capturing images
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ClickableViewAccessibility", "NewApi")
    private fun chooseImagesClick() {
        civPicProfile.setOnClickListener {
            imageFrom = "1"

            showDocPopup()
        }
    }

    // method for uploading images either by capturing from camera or from gallery
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun showDocPopup() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.upload_pics))
        alertDialog.setMessage(getString(R.string.please_select_from_where_you_want_to_choose))
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera") { _, _ ->
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


    @RequiresApi(Build.VERSION_CODES.M)
    private fun makeRequest2() {
        requestPermissions(
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            2
        )
    }

    // Method for opening gallery
    private fun openGallery() {

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, imageCameraRequest)
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun makeRequest1() {
        requestPermissions(
            arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
            ),
            1
        )
    }


    // Method for  capturing images via Camera
    private fun takePicture() {

        createFileAndDeleteOldFile()

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val apiLevel = android.os.Build.VERSION.SDK_INT

        val mImageCaptureUri: Uri

        mImageCaptureUri = if (apiLevel >= 24) {
            FileProvider.getUriForFile(
                this,
                this.packageName + ".provider", mFileTemp!!
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


    // Method for creating new files and deleting old files
    private fun createFileAndDeleteOldFile() {
        mFileTemp =
                File(Environment.getExternalStorageDirectory(), "profie_pic" + System.currentTimeMillis() + ".jpg")
        if (mFileTemp!!.exists()) {
            mFileTemp!!.delete()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", getString(R.string.permission_denied))
                    showMessageDialog(
                        resources.getString(R.string.permission_message)
                    )

                } else {
                    Log.i("", resources.getString(R.string.permission_granted))

                    takePicture()
                }
                return
            }
            2 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    || grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("", resources.getString(R.string.permission_denied))
                    showMessageDialog(
                        getString(R.string.permission_message)
                    )
                } else {
                    Log.i("", getString(R.string.permission_granted))

                    openGallery()
                }
                return
            }

        }
    }


    //Method to dislpay message to the user about the permission required by the application
    @SuppressLint("RtlHardcoded")
    private fun showMessageDialog(message: String)
    {
        val alertDialog = android.support.v7.app.AlertDialog.Builder(
            this
            , R.style.MyDialogTheme
        ).create()

        alertDialog.setMessage(message)

        alertDialog.setButton(
            android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Ok"
        )
        { dialog
          , _ ->
            dialog.dismiss()

            finish()
        }

        alertDialog.show()

        alertDialog.setCancelable(false)

        val messageText: TextView = alertDialog!!.findViewById(android.R.id.message)!!

        messageText.gravity = Gravity.LEFT

    }


    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            imageCameraRequest ->

                try {
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = this.contentResolver.query(
                        data!!.data,
                        projection, null, null, null
                    )
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
                                Glide.with(this)
                                    .load(imgFile)
                                    .into(civPicProfile)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                        }

                    }
                } catch (e: Exception) {
                    Log.e("tag", "Error while creating temp file", e)
                }

            imageCameraCaptureRequest ->

                if (requestCode == imageCameraCaptureRequest && resultCode == AppCompatActivity.RESULT_OK) {

                    val imgFile = compressImage(File(mFileTemp!!.path))
                    println("IMAGE FILE 2  $imgFile")

                    // dataList.add(imgFile)
                    if (imgFile.exists()) {
                        when (imageFrom) {
                            "1" -> {
                                Glide.with(this)
                                    .load(imgFile)
                                    .into(civPicProfile)
                                isImageOn1 = true
                                file1 = imgFile
                            }
                        }
                    }
                }
        }
    }


    // Method for compressing image
    private fun compressImage(imgFile: File): File {
        val bos = ByteArrayOutputStream()

        // var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        val myBitmap = imageRotation(BitmapFactory.decodeFile(imgFile.absolutePath), imgFile.absolutePath)

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bos)

        val bitmapdata = bos.toByteArray()

        val fos = FileOutputStream(imgFile)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return imgFile

    }

    // Method for rotating images
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




    //###################### UPDATE USER API########################//

    // function for hitting update user API
    private fun updateUser()
    {
        if (commonClass.checkInternetConn(this))
        {
            if (file1 != null && file1!!.exists())
                dataList.add(file1!!)

            for (i in 0 until dataList.size)
            {
                hasPic =true
                var imgProfile: MultipartBody.Part? = null

                val file = File(dataList[i].toString())

                // create RequestBody instance from file
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

                // MultipartBody.Part is used to send also the actual file name
                imgProfile = MultipartBody.Part.createFormData("photos", file.name, requestFile)

                fileList.add(imgProfile!!)
            }

            when{
                !hasPic -> commonClass.showToast(getString(R.string.select_image_first))

                etNameProfile.text.isEmpty()->commonClass.showToast(getString(R.string.provide_name))

                etNumberProfile.text.isEmpty()->commonClass.showToast("Please provide mobile number")

                //etNumberProfile.text.toString().length<10->commonClass.showToast("Please provide correct mobile number")

                etEmailProfile.text.isEmpty()->commonClass.showToast("Please provide email id")

                !commonClass.isEmailValid(etEmailProfile.text.toString()) ->commonClass.showToast("Please provide correct email id")

                tvGenderProfile.text.toString()=="Select Gender"->commonClass.showToast("Please select gender")

                else->
                {
                    if (etNameProfile.text.toString().contains("\\\\s+"))
                    {
                        first=getName(etNameProfile.text.toString())

                        last=getSurname(etNameProfile.text.toString())
                    }
                    else
                    {
                        first = etNameProfile.text.toString()
                        last=""
                    }

                    val gender= if (tvGenderProfile.text.toString()==resources.getString(R.string.not_specified))
                        resources.getString(R.string.other)
                    else
                        tvGenderProfile.text.toString()

                    profileHandler.updateUser(RequestBody.create(MediaType.parse("text/plain"),etEmailProfile.text.toString()),
                        RequestBody.create(MediaType.parse("text/plain"),etNumberProfile.text.toString()),
                        RequestBody.create(MediaType.parse("text/plain"),first),
                        RequestBody.create(MediaType.parse("text/plain"),last),
                        RequestBody.create(MediaType.parse("text/plain"),gender),
                        fileList,
                        commonClass.getString("x_access_token"))
                }
            }
        }
        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable))
    }


    // function for handling the response of the update Request API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            etNameKycProfile.setText(etNameProfile.text.toString())

            tvBasicProfile.setTextColor(resources.getColor(R.color.color_grey))

            viewBasic.visibility=View.INVISIBLE

            checKBasic=false

            rlBasicProfile.visibility=View.GONE

            civPicProfile.visibility=View.INVISIBLE

            tvDobKycProfile.setTextColor(resources.getColor(R.color.color_grey))

            rlKycProfile.visibility=View.VISIBLE

            tvKycProfile.setTextColor(resources.getColor(R.color.red))

            viewKyc.visibility=View.VISIBLE

            commonClass.putString("first_name",first)

            commonClass.putString("last_name",last)

            val displayName = if(last!="") {
                "$first $last"
            } else
                first

            commonClass. putString("display_name",displayName)

            commonClass.putString("mobile_number",etNumberProfile.text.toString())

            commonClass.putString("gender",tvGenderProfile.text.toString())

            commonClass.putString("email",etEmailProfile.text.toString())

            val urlProfile = mainPojo.getData().profile

            println("PROFILE URL >>>>>>>> $urlProfile")

            commonClass.putString("user_image",urlProfile)

        }
        commonClass.showToast(mainPojo.message)
    }

    override fun showLoader()
    {
        rlLoaderProfile.visibility=View.VISIBLE

        aviPro.show()
    }

    override fun hideLoader()
    {
        rlLoaderProfile.visibility=View.GONE

        aviPro.hide()
    }

    private fun getName(fullName: String): String {
        return fullName.split(" (?!.* )".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    private fun getSurname(fullName: String): String {
        return fullName.split(" (?!.* )".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    // ########################### UPDATE KYC API#######################//

    // function for hitting updateKyc API
    private fun updateKyc()
    {
        if (commonClass.checkInternetConn(this))
        {
            val map=HashMap<String,Any>()
            when{
                tvDobKycProfile.text.toString()==getString(R.string.date_of_birth)->commonClass.showToast("Please provide date of birth")

                etPANProfile.text.isEmpty()->commonClass.showToast("Please provide PAN number.")

                etAdharProfile.text.isEmpty()->commonClass.showToast("Please provide Aadhaar number.")

                else->
                {
                    map["date_of_birth"]=tvDobKycProfile.text.toString()

                    map["pan_card"]=etPANProfile.text.toString()

                    map["aadhar_number"]=etAdharProfile.text.toString()

                    profileHandler.updateKyc(map,
                        commonClass.getString("x_access_token"))
                }
            }
        }
        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable))
    }

    //function for handling the response of the update Kyc API
    override fun updateKycSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            commonClass.putString("date_of_birth",tvDobKycProfile.text.toString())

            commonClass.putString("pan_card",etPANProfile.text.toString())

            commonClass.putString("aadhar_number",etAdharProfile.text.toString())

            startActivity(Intent(this,WaitingScreenWhite::class.java)
                .putExtra("from_where","profile"))
        }
        else{
            commonClass.showToast(mainPojo.message)
        }
    }


}
