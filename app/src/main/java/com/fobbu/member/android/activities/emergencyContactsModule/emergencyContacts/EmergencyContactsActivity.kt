package com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.activity_emergency_contacts.*
import kotlinx.android.synthetic.main.toolbar.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.ContactListActicvity
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter.ListHandler
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter.ListPresenter
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.view.ContactListView
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.presenter.EmergencyHandler
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.presenter.EmergencyPresenter
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.view.ContactsView
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView


@Suppress("DEPRECATION")
class EmergencyContactsActivity : ContactListView, AppCompatActivity(), ActivityView, ContactsView
{
    var dataList = ArrayList<HashMap<String, Any>>()

    private var relationShip = arrayOf(
        "Relationship",
        "Father",
        "Mother",
        "Sister",
        "Brother",
        "Uncle",
        "Aunt",
        "Son",
        "Daughter",
        "Wife",
        "Husband",
        "Neighbor"
    )

    lateinit var commonClass: CommonClass

    var  selectedContactId=""

    private val contactList = ArrayList<HashMap<String, String>>()

    private var sortedList = ArrayList<HashMap<String, String>>()

    private var editedList = HashMap<String, Any>()

    private lateinit var listHandler: ListHandler

    private lateinit var emergencyHandler: EmergencyHandler

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_emergency_contacts)

        initView()      //function for initialising all  the views of the class

        clicks()        // function for handling clicks of the class
    }


    //function for initialising all  the views of the class
    private fun initView()
    {
        commonClass = CommonClass(this, this)

        ivSearchToolbar.visibility = View.INVISIBLE

        spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, relationShip)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEmergency.adapter = spinnerAdapter

        spinner2ndEmergency.adapter = spinnerAdapter

        spinnerEditEmergency.adapter = spinnerAdapter

        listHandler = ListPresenter(this, this)

        if (intent.hasExtra("sorted_list"))
        {
            tvHeaderEmergency.text = resources.getString(R.string.edit_emergency_contacts)

            sortedList = intent.getSerializableExtra("sorted_list") as ArrayList<HashMap<String, String>>

            etEditNameEmergency.setText(sortedList[0]["name"], TextView.BufferType.EDITABLE)

            etEditMobileEmergency.setText(sortedList[0]["mobile_number"], TextView.BufferType.EDITABLE)

            selectedContactId=sortedList[0]["_id"].toString()

            Handler().postDelayed({
                tvEditRelationship.setTextColor(resources.getColor(R.color.color_grey))

                tvEditRelationship.text = sortedList[0]["relationship"].toString()
            }, 1000)

            llAddContactComplete.visibility = View.GONE

            llEditContactComplete.visibility = View.VISIBLE

            tvAddContacts.setTextColor(resources.getColor(R.color.color_grey))

            viewAdd.visibility = View.INVISIBLE

            tvEditContacts.setTextColor(resources.getColor(R.color.red))

            viewEdit.visibility = View.VISIBLE

            tvAddEmergencyContacts.text = getString(R.string.edit)
        }
        else
        {
            tvHeaderEmergency.text = resources.getString(R.string.add_emergency_contacts)

            tvAddContacts.setTextColor(resources.getColor(R.color.red))

            viewAdd.visibility = View.VISIBLE

            tvEditContacts.setTextColor(resources.getColor(R.color.color_grey))

            viewEdit.visibility = View.INVISIBLE

            llAddContactComplete.visibility = View.VISIBLE

            llEditContactComplete.visibility = View.GONE

            tvAddEmergencyContacts.text = getString(R.string.add)
        }

        emergencyHandler = EmergencyPresenter(this, this, this)
    }

    // function for handling clicks of the class
    private fun clicks()
    {
        ivCheck.setOnClickListener {
            if (ivCheck.drawable.constantState == resources.getDrawable(R.drawable.checkbox_checked).constantState)
                ivCheck.setImageResource(R.drawable.checkbox_uncheck)

            else
                ivCheck.setImageResource(R.drawable.checkbox_checked)
        }

        ivBackButton.setOnClickListener {
            finish()
        }

        rlRelationShipEmergency.setOnClickListener {
            spinnerEmergency.performClick()
        }

        rl2ndRelationShipEmergency.setOnClickListener {
            spinner2ndEmergency.performClick()
        }

        ivContactList.setOnClickListener {
            startActivity(Intent(this, ContactListActicvity::class.java))
        }

        rlEditRelationShipEmergency.setOnClickListener {
            spinnerEditEmergency.performClick()
        }

        spinnerEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tvRelationship.text = spinnerEmergency.selectedItem.toString()

                if (tvRelationship.text.toString() == "Relationship")
                    tvRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tvRelationship.setTextColor(resources.getColor(R.color.color_grey))

            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerEditEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tvEditRelationship.text = spinnerEditEmergency.selectedItem.toString()

                if (tvEditRelationship.text.toString() == "Relationship")
                    tvEditRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tvEditRelationship.setTextColor(resources.getColor(R.color.color_grey))
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        spinner2ndEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                tv2ndRelationship.text = spinner2ndEmergency.selectedItem.toString()

                if (tv2ndRelationship.text.toString() == "Relationship")
                    tv2ndRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tv2ndRelationship.setTextColor(resources.getColor(R.color.color_grey))
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        tvAddEmergencyContacts.setOnClickListener {
            if (tvAddEmergencyContacts.text == resources.getString(R.string.add))
            {
                if (dataList.size >= 5)
                    CommonClass(this,this).showToast(resources.getString(R.string.max_limit_contact_msg), rlEmergencyContacts)

                else if (etFullNameEmergency.text.toString().trim().isEmpty() && et2ndFullNameEmergency.text.toString().trim().isEmpty())
                    CommonClass(this, this).showToast(resources.getString(R.string.atleast_one_contact_msg),rlEmergencyContacts)

                else if ((etMobileEmergency.text.trim().toString() == et2ndMobileEmergency.text.trim().toString())&&etMobileEmergency.text.trim().isNotEmpty()
                    && et2ndMobileEmergency.text.trim().isNotEmpty())
                    commonClass.showToast(resources.getString(R.string.msg_diff_contact_num),rlEmergencyContacts)

                else if (ifEmpty(etFullNameEmergency.text.toString().trim(), etMobileEmergency.text.toString().trim(), tvRelationship.text.toString()))
                {
                    when
                    {
                        etFullNameEmergency.text.toString().trim().isEmpty() ->
                            CommonClass(this, this).showToast(resources.getString(R.string.provide_name_msg),rlEmergencyContacts)

                        etMobileEmergency.text.toString().trim().isEmpty() ->
                            CommonClass(this, this).showToast(resources.getString(R.string.provide_number_msg),rlEmergencyContacts)

                        etMobileEmergency.text.toString().trim().length < 10 ->
                            CommonClass(this, this).showToast(resources.getString(R.string.correct_number_1st_contact_msg),rlEmergencyContacts)

                        tvRelationship.text.toString() == "Relationship" ->
                            CommonClass(this, this).showToast(resources.getString(R.string.relationship_status_1st_msg),rlEmergencyContacts)

                        else ->
                        {
                            // method for saving prices and total number of services in list
                            saveToLocalList(etFullNameEmergency.text.toString().trim(), etMobileEmergency.text.toString(), tvRelationship.text.toString(),"1")
                        }
                    }
                }
                else if (ifEmpty(et2ndFullNameEmergency.text.toString().trim(), et2ndMobileEmergency.text.toString().trim(), tv2ndRelationship.text.toString()))
                {
                    // function for validating the texts of the 2nd contact block
                    check2ndBlock()
                }
            }
            else
            {
                when
                {
                    etEditNameEmergency.text.toString().trim().isEmpty() ->
                        CommonClass(this,this).showToast( resources.getString(R.string.provide_name_msg), rlEmergencyContacts)

                    etEditMobileEmergency.text.toString().trim().isEmpty() ->
                        CommonClass(this,this).showToast( resources.getString(R.string.provide_number_msg),rlEmergencyContacts)

                    etEditMobileEmergency.text.trim().isNotEmpty() && etEditMobileEmergency.text.trim().length<10->
                        CommonClass(this,this).showToast(resources.getString(R.string.correct_num),rlEmergencyContacts)

                    tvEditRelationship.text == getString(R.string.relationship) ->
                        CommonClass(this,this).showToast(resources.getString(R.string.relationship_status_1st_msg), rlEmergencyContacts)

                    else ->
                    {
                        var isPresent=false

                        var selectedId=false

                        for (i in dataList.indices)
                        {
                            if(dataList[i]["mobile_number"]==etEditMobileEmergency.text.trim().toString())
                            {
                                isPresent=true

                                if (dataList[i]["_id"]==selectedContactId)
                                {
                                    editedList=dataList[i]

                                    selectedId=true

                                }else
                                    selectedId=false

                                break
                            }
                        }

                        if (isPresent)
                        {
                            if (selectedId)
                            {
                                editedList["name"] = etEditNameEmergency.text.toString().trim()

                                editedList["relationship"] = tvEditRelationship.text.toString().trim()

                                editContacts()           // implementing emergencycontacts API (PUT)
                            }
                            else
                                commonClass.showToast("This number already exists. Please try with another one",rlEmergencyContacts)
                        }
                        else
                        {
                            editedList["name"] = etEditNameEmergency.text.toString().trim()

                            editedList["mobile_number"] = etEditMobileEmergency.text.toString().trim()

                            editedList["relationship"] = tvEditRelationship.text.toString().trim()

                            editContacts()              // implementing emergencycontacts API (PUT)
                        }
                    }
                }
            }
        }
    }

    override fun onResume()
    {
        super.onResume()

        getContact()        // implementing emergencycontacts API (GET)
    }

    // function for validating the texts of the 2nd contact block
    private fun check2ndBlock() = when
    {
        et2ndFullNameEmergency.text.toString().trim().isEmpty() ->
            CommonClass(this, this).showToast(getString(R.string.provide_2nd_contact_name_msg),rlEmergencyContacts)

        et2ndMobileEmergency.text.trim().toString().isEmpty() ->
            CommonClass(this, this).showToast(getString(R.string.provide_2nd_number_msg),rlEmergencyContacts)

        et2ndMobileEmergency.text.toString().trim().length < 10 ->
            CommonClass(this, this).showToast(getString(R.string.provide_2nd_contact_correct_number_msg),rlEmergencyContacts)

        tv2ndRelationship.text.toString() == "Relationship" ->
            CommonClass(this, this).showToast(getString(R.string.relationship_status_1st_msg),rlEmergencyContacts)

        else ->
        {
            // method for saving prices and total number of services in list
            saveToLocalList(et2ndFullNameEmergency.text.toString().trim(), et2ndMobileEmergency.text.toString().trim(), tv2ndRelationship.text.toString(),"2")
        }
    }

    // method for saving prices and total number of services in list
    private fun saveToLocalList(name: String, mobileNumber: String, relationship: String,from:String)
    {
        var isPresentInMainList = false

        var firstBlockRepetitionCheck= false

        var position=0

        for (i in dataList.indices)
        {
            if (dataList[i]["mobile_number"] == mobileNumber)
            {
                isPresentInMainList = true

                break
            }
        }

        for(i in contactList.indices)
        {
            if (contactList[i]["mobile_number"]==mobileNumber)
            {
                firstBlockRepetitionCheck=true

                position=i

                break
            }
        }

        if (isPresentInMainList)
            commonClass.showToast(resources.getString(R.string.user_exists_msg),rlEmergencyContacts)

        else
        {
            if (!firstBlockRepetitionCheck) {
                val hashMap = HashMap<String, String>()

                hashMap["name"] = name

                hashMap["mobile_number"] = mobileNumber

                hashMap["relationship"] = relationship

                contactList.add(hashMap)

                if (from == "1")
                {
                    if (ifEmpty(et2ndFullNameEmergency.text.toString().trim(), et2ndMobileEmergency.text.toString().trim(), tv2ndRelationship.text.toString()))
                        check2ndBlock()                // function for validating the texts of the 2nd contact block

                    else
                        postEmergencyContacts()        // implementing emergencycontacts API (POST)
                }
                else
                    postEmergencyContacts()           // implementing emergencycontacts API (POST)
            }
            else
            {
                contactList[position]["name"]= name

                contactList[position]["mobile_number"]= mobileNumber

                contactList[position]["relationship"]= relationship

                if (from == "1") {
                    if (ifEmpty(
                            et2ndFullNameEmergency.text.toString().trim(),
                            et2ndMobileEmergency.text.toString().trim(),
                            tv2ndRelationship.text.toString()
                        )
                    )
                        check2ndBlock()                // function for validating the texts of the 2nd contact block

                    else
                        postEmergencyContacts()        // implementing emergencycontacts API (POST)
                } else
                    postEmergencyContacts()           // implementing emergencycontacts API (POST)
            }
        }
    }

    //##################POST emergency contacts API####################//

    // implementing emergencycontacts API (POST)
    private fun postEmergencyContacts() {
        if (commonClass.checkInternetConn(this))
        {
            if (contactList.size > 0)
            {
                emergencyHandler.postEmergencyContracts(contactList, commonClass.getString("x_access_token"))

                contactList.clear()
            }
        }
        else
            CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable), rlEmergencyContacts)
    }


    // function for handling the response of the POST emergency contacts API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            startActivity(Intent(this, ContactListActicvity::class.java))

            finish()
        }
        else
            CommonClass(this,this).showToast(mainPojo.message, rlEmergencyContacts)

    }


    // ######################## EDIT CONTACTS API####################################//

    // implementing emergencycontacts API (PUT)
    private fun editContacts()
    {
        if (commonClass.checkInternetConn(this))
            emergencyHandler.editContacts(editedList, sortedList[0]["_id"].toString(), commonClass.getString("x_access_token"))

        else
            CommonClass(this,this).showToast( resources.getString(R.string.internet_is_unavailable),rlEmergencyContacts)
    }

    // function to handle the response of the edit contacts API
    override fun editContactsSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            startActivity(Intent(this, ContactListActicvity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

            finish()
        }
        else
            commonClass.showToast(mainPojo.message,rlEmergencyContacts)
    }

    override fun showLoader()
    {
        rlLoader.visibility = View.GONE

        avi.show()
    }

    override fun hideLoader()
    {
        rlLoader.visibility = View.GONE

        avi.hide()
    }


    // **************************  emergencycontacts API (GET)  ********************//

    // implementing emergencycontacts API (GET)
    private fun getContact()
    {
        if (commonClass.checkInternetConn(this))
            listHandler.getContacts(commonClass.getString("x_access_token"))

        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable),rlEmergencyContacts)
    }

    // handling response of emergencycontacts API (GET)
    override fun SuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
            dataList = mainPojo.list
    }

    override fun deleteContactSuccessReport(mainPojo: MainPojo) {}

    // function for checking whether the  data in parameters is empty or present
    private fun ifEmpty(name: String, number: String, relationshipString: String): Boolean
    {
        var isProvided = false

        if (name.isNotEmpty())
            isProvided = true

        if (number.isNotEmpty())
            isProvided = true

        if (relationshipString != ("Relationship"))
            isProvided = true

        return isProvided
    }


}
