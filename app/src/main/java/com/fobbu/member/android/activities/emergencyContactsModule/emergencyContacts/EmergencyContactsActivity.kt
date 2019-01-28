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


class EmergencyContactsActivity :ContactListView, AppCompatActivity(),ActivityView,ContactsView
{
    var dataList=ArrayList<HashMap<String,Any>>()

    private var relationShip= arrayOf("Relationship","Father","Mother","Sister","Brother","Uncle","Aunt","Son","Daughter","Wife","Husband","Neighbor")

    lateinit var commonClass: CommonClass

    private val contactList=ArrayList<HashMap<String,String>>()

    private var sortedList=ArrayList<HashMap<String,String>>()

    private var editedList=HashMap<String,Any>()

    lateinit var listHandler:ListHandler

    private lateinit var emergencyHandler:EmergencyHandler

    private lateinit var spinnerAdapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_emergency_contacts)

        initView()

        clicks()
    }


    //function for initialising all  the views of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility= View.INVISIBLE

        spinnerAdapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,relationShip)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEmergency.adapter=spinnerAdapter

        spinner2ndEmergency.adapter= spinnerAdapter

        spinnerEditEmergency.adapter=spinnerAdapter

        listHandler=ListPresenter(this,this)

        if (intent.hasExtra("sorted_list"))
        {
            tvHeaderEmergency.text=resources.getString(R.string.edit_emergency_contacts)

            sortedList= intent.getSerializableExtra("sorted_list") as ArrayList<HashMap<String, String>>

            etEditNameEmergency.setText(sortedList[0]["name"],TextView.BufferType.EDITABLE)

            etEditMobileEmergency.setText(sortedList[0]["mobile_number"],TextView.BufferType.EDITABLE)

            Handler().postDelayed({
                tvEditRelationship.setTextColor(resources.getColor(R.color.color_grey))

                tvEditRelationship.text=sortedList[0]["relationship"].toString()
            },1000)

            llAddContactComplete.visibility=View.GONE

            llEditContactComplete.visibility=View.VISIBLE

            tvAddContacts.setTextColor(resources.getColor(R.color.color_grey))

            viewAdd.visibility=View.INVISIBLE

            tvEditContacts.setTextColor(resources.getColor(R.color.red))

            viewEdit.visibility=View.VISIBLE

            tvAddEmergencyContacts.text= getString(R.string.edit)
        }
        else
        {
            tvHeaderEmergency.text=resources.getString(R.string.add_emergency_contacts)

            tvAddContacts.setTextColor(resources.getColor(R.color.red))

            viewAdd.visibility=View.VISIBLE

            tvEditContacts.setTextColor(resources.getColor(R.color.color_grey))

            viewEdit.visibility=View.INVISIBLE

            llAddContactComplete.visibility=View.VISIBLE

            llEditContactComplete.visibility=View.GONE

            tvAddEmergencyContacts.text= getString(R.string.add)
        }
        emergencyHandler=EmergencyPresenter(this,this,this)
    }

    override fun onResume() {
        super.onResume()
        getContact()
    }

    private fun getContact()
    {
        if (commonClass.checkInternetConn(this))
            listHandler.getContacts(commonClass.getString("x_access_token"))
        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable))
    }

    // function for handling clicks of the class
    private fun clicks()
    {
        ivCheck.setOnClickListener {
            if (ivCheck.drawable.constantState==resources.getDrawable(R.drawable.checkbox_checked).constantState)
            {
                ivCheck.setImageResource(R.drawable.checkbox_uncheck)
            }else{
                ivCheck.setImageResource(R.drawable.checkbox_checked)
            }
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
            startActivity(Intent(this,ContactListActicvity::class.java))
        }

        rlEditRelationShipEmergency.setOnClickListener {

            spinnerEditEmergency.performClick()
        }

        spinnerEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tvRelationship.text=spinnerEmergency.selectedItem.toString()

                if (tvRelationship.text.toString()=="Relationship")
                    tvRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tvRelationship.setTextColor(resources.getColor(R.color.color_grey))

            } // to close the onItemSelected
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinnerEditEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tvEditRelationship.text=spinnerEditEmergency.selectedItem.toString()

                if (tvEditRelationship.text.toString()=="Relationship")
                    tvEditRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tvEditRelationship.setTextColor(resources.getColor(R.color.color_grey))
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        spinner2ndEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                tv2ndRelationship.text=spinner2ndEmergency.selectedItem.toString()

                if (tv2ndRelationship.text.toString()=="Relationship")
                    tv2ndRelationship.setTextColor(resources.getColor(R.color.view_grey))

                else
                    tv2ndRelationship.setTextColor(resources.getColor(R.color.color_grey))
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        tvAddEmergencyContacts.setOnClickListener {
            if (tvAddEmergencyContacts.text==resources.getString(R.string.add))
            {
                saveToLocalList(etFullNameEmergency.text.toString(),
                    etMobileEmergency.text.toString(),tvRelationship.text.toString())

                saveToLocalList(et2ndFullNameEmergency.text.toString(),
                    et2ndMobileEmergency.text.toString(),tv2ndRelationship.text.toString())

                if (contactList.size<1)
                    Toast.makeText(this,"Please provide atleast one emergency contact", Toast.LENGTH_SHORT).show()
                else
                {
                    if (dataList.size>=5)
                        Toast.makeText(this,"You have reached the maximum limit of contacts that can be added. Please remove few to add more.", Toast.LENGTH_SHORT).show()

                    else
                        postEmergencyContacts()
                }
            }
            else
            {
                when
                {
                    etEditNameEmergency.text.isEmpty()->   Toast.makeText(this,"Please provide name.", Toast.LENGTH_SHORT).show()

                    etEditMobileEmergency.text.isEmpty()->   Toast.makeText(this,"Please provide number.", Toast.LENGTH_SHORT).show()

                    tvEditRelationship.text== getString(R.string.relationship)->   Toast.makeText(this,"Please provide relationship.", Toast.LENGTH_SHORT).show()

                    else->
                    {
                        editedList["name"]=etEditNameEmergency.text.toString()

                        editedList["mobile_number"]=etEditMobileEmergency.text.toString()

                        editedList["relationship"]=tvEditRelationship.text.toString()

                        editContacts()
                    }
                }
            }
        }
    }

    // method for saving prices and total number of services in hashmap
    private fun saveToLocalList(name:String,mobileNumber:String,relatonship:String)
    {
        val hashMap =HashMap<String,String>()

        var position=0

        var isPresent=false

        if (name.isNotEmpty() && mobileNumber.isNotEmpty() && relatonship.isNotEmpty())
        {
            for (i in contactList.indices)
            {
                if(mobileNumber==contactList[i]["mobile_number"] )
                {
                    isPresent=true

                    position=i

                    break
                }
            }

            when{
                isPresent->
                {
                    contactList[position]["name"]=name

                    contactList[position]["mobile_number"]=mobileNumber

                    contactList[position]["relationship"]=relatonship
                }
                else->
                {
                    hashMap["name"]=name

                    hashMap["mobile_number"]=mobileNumber

                    hashMap["relationship"]=relatonship

                    contactList.add(hashMap)

                }
            }
            println("SAVED LIST $contactList")
        }
    }

    //##################POST emergency contacts API####################//

  // function for posting emergency contacts
    private fun postEmergencyContacts()
    {
        if (commonClass.checkInternetConn(this))
            emergencyHandler.postEmergencyContracts(contactList,
                commonClass.getString("x_access_token"))

        else
            Toast.makeText(this,resources.getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
    }


    // function for handling the response of the POST emergency contacts API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            startActivity(Intent(this,ContactListActicvity::class.java))

            finish()
        }
        else
        {
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
        }
    }


    // ######################## EDIT CONTACTS API####################################//

    // function for hitting edit contacts API
    private fun editContacts()
    {
        if (commonClass.checkInternetConn(this))
            emergencyHandler.editContacts(editedList,sortedList[0]["_id"].toString(),commonClass.getString("x_access_token"))
        else
            Toast.makeText(this,resources.getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
    }

    // function to handle the response of the edit contacts API
    override fun editContactsSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            startActivity(Intent(this,ContactListActicvity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

            finish()
        }



        else
            commonClass.showToast(mainPojo.message)
    }


    override fun showLoader()
    {
        rlLoader.visibility=View.GONE

        avi.show()
    }

    override fun hideLoader()
    {
        rlLoader.visibility=View.GONE

        avi.hide()
    }

    override fun SuccessReport(mainPojo: MainPojo) {
        if (mainPojo.success=="true")
        {
            dataList= mainPojo.list
        }
    }

    override fun deleteContactSuccessReport(mainPojo: MainPojo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
