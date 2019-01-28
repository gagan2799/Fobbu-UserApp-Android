package com.fobbu.member.android.activities.emergencyContactsModule.contactList

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.adapter.ContactsAdapter
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter.ListHandler
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter.ListPresenter
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.view.ContactListView
import com.fobbu.member.android.interfaces.DeleteVehicleClickListener
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.toolbar.*

class ContactListActicvity : AppCompatActivity(),ContactListView,DeleteVehicleClickListener
{
    lateinit var commonClass: CommonClass

    var dataList=ArrayList<HashMap<String,Any>>()

    private lateinit var listHandler:ListHandler

    private lateinit var contactAdapter:ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact_list)

        initView()

        clicks()
    }

    //function for initialisng all the variablies of the class
    @SuppressLint("SetTextI18n")
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility= View.INVISIBLE

        listHandler=ListPresenter(this,this)

        getContact()
    }

    //function for setting up recycler
    private fun setupRecycler()
    {
        contactAdapter= ContactsAdapter(this,dataList)

        rvContacts.layoutManager=LinearLayoutManager(this)

        rvContacts.adapter=contactAdapter
    }

    override fun onViewClick(contactId: String)
    {
     deleteContactDialog(contactId)
    }


    ///DELETE CONTACT  POPUP
    private fun deleteContactDialog(id: String)
    {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle(resources.getString(R.string.Delete))

        alertDialog.setMessage("Are you sure you want to delete this contact from list")

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()
            deleteContact(id)
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }


    //function for handling all the clicks of the class
    private fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }
    }

    //***********************GET CONTACTS API*****************//

    //function for getting contact list
    private fun getContact()
    {
        if (commonClass.checkInternetConn(this))
            listHandler.getContacts(commonClass.getString("x_access_token"))
        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable))
    }

    override fun SuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success== "true")
        {
            dataList=mainPojo.list

            if (dataList.size==0)
            {
                rvContacts.visibility=View.GONE

                tvNodata.visibility=View.VISIBLE

                tvNodata.text=getString(R.string.currently_no_data_is_available)
            }
            else
            {
                tvNodata.visibility=View.GONE

                rvContacts.visibility=View.VISIBLE

                setupRecycler()

            }
        }
        else
            commonClass.showToast(mainPojo.message)
    }


    // ####################### DELETE CONTACT API###################//

    // function for delete api
    private fun deleteContact(contactId: String)
    {
        if (commonClass.checkInternetConn(this))
            listHandler.deleteContact(contactId,commonClass.getString("x_access_token"))
        else
            commonClass.showToast(resources.getString(R.string.internet_is_unavailable))
    }


    override fun showLoader()
    {
        rlLoaderVehicleList.visibility=View.VISIBLE

        avi.show()
    }

    override fun hideLoader()
    {
        rlLoaderVehicleList.visibility=View.GONE

        avi.hide()
    }
    override fun deleteContactSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
            getContact()
        else
            commonClass.showToast(mainPojo.message)
    }



}
