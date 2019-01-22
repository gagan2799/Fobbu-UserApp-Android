package com.fobbu.member.android.activities.emergencyContacts

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_emergency_contacts.*
import kotlinx.android.synthetic.main.toolbar.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.fobbu.member.android.activities.emergencyContacts.presenter.EmergencyHandler
import com.fobbu.member.android.activities.emergencyContacts.presenter.EmergencyPresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView


class EmergencyContactsActivity : AppCompatActivity(),ActivityView
{


    var relationShip= arrayOf("Relationship","Father","Mother","Sister","Brother","Uncle","Aunt","Son","Daughter","Wife","Husband","Neighbor")

    lateinit var commonClass:CommonClass

    private val contactList=ArrayList<HashMap<String,String>>()

    lateinit var emergencyHandler:EmergencyHandler

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

        val spinnerAdapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,relationShip)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEmergency.adapter=spinnerAdapter

        spinner2ndEmergency.adapter= spinnerAdapter

        emergencyHandler=EmergencyPresenter(this,this)
    }

    // fucntion for handling clicks of the class
    private fun clicks()
    {
        rlRelationShipEmergency.setOnClickListener {
            spinnerEmergency.performClick()
        }

        rl2ndRelationShipEmergency.setOnClickListener {
            spinner2ndEmergency.performClick()
        }

        spinnerEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                val selectedItem = parent.getItemAtPosition(position).toString()
                tvRelationship.text=spinnerEmergency.selectedItem.toString()
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        spinner2ndEmergency.onItemSelectedListener = object : OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                val selectedItem = parent.getItemAtPosition(position).toString()
                tv2ndRelationship.text=spinner2ndEmergency.selectedItem.toString()
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        tvAddEmergencyContacts.setOnClickListener {
            when {

                etFullNameEmergency.text.toString().isEmpty() -> Toast.makeText(this,"Provide name", Toast.LENGTH_SHORT).show()

                et2ndFullNameEmergency.text.toString().isEmpty() -> Toast.makeText(this,"Provide name", Toast.LENGTH_SHORT).show()

                etMobileEmergency.text.toString().isEmpty() -> Toast.makeText(this,"Provide Number", Toast.LENGTH_SHORT).show()

                et2ndFullNameEmergency.text.toString().isEmpty() -> Toast.makeText(this,"Provide Number", Toast.LENGTH_SHORT).show()

                tvRelationship.text.toString()=="Relationship" -> Toast.makeText(this,"Provide us the information about the relation with the person", Toast.LENGTH_SHORT).show()

                tv2ndRelationship.text.toString()=="Relationship" -> Toast.makeText(this,"Provide us the information about the relation with the person", Toast.LENGTH_SHORT).show()

                else -> {
                    saveToLocalList(etFullNameEmergency.text.toString(),
                        etMobileEmergency.text.toString(),tvRelationship.text.toString())

                    saveToLocalList(et2ndFullNameEmergency.text.toString(),
                        et2ndFullNameEmergency.text.toString(),tv2ndRelationship.text.toString())

                    if (contactList.size<1)
                        Toast.makeText(this,"Please provide atleast one emergency contact", Toast.LENGTH_SHORT).show()
                    else
                        postEmergencyContacts()
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
            else->{
                hashMap["name"]=name
                hashMap["mobile_number"]=mobileNumber
                hashMap["relationship"]=relatonship
                //deleteRowFromList(saveToLocalList)
                contactList.add(hashMap)
            }
        }
        println("SAVED LIST $contactList")
    }

    //##################POST emergency contacts API####################//
    fun postEmergencyContacts()
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
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoader()
    {
    rlLoader.visibility=View.VISIBLE

        avi.show()
    }

    override fun hideLoader()
    {
        rlLoader.visibility=View.GONE

        avi.hide()
    }



}
