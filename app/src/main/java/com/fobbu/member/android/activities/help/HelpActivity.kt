package com.fobbu.member.android.activities.help

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.help.adapter.HelpAdapter
import com.fobbu.member.android.activities.help.presenter.HelpHandler
import com.fobbu.member.android.activities.help.presenter.HelpPresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.toolbar.*

class HelpActivity : AppCompatActivity(),ActivityView
{
    lateinit var helpAdapter:HelpAdapter

    private lateinit var commonClass: CommonClass

    var dataList= ArrayList<HashMap<String,Any>>()

    private lateinit var helpHandler:HelpHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help)

        initView()            //function for initialising all the variables of the class

        clicks()               // function for handling clicks of the class
    }

    //function for initialising all the variables of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        helpHandler=HelpPresenter(this,this)

        ivSearchToolbar.visibility=View.INVISIBLE

        getHelp()      // implementing get_helps API

    }

    //function for setting up recycler
    private fun setUpRecycler()
    {
        helpAdapter= HelpAdapter(this,dataList)

        rvHelp.layoutManager=LinearLayoutManager(this)

        rvHelp.adapter= helpAdapter
    }

    // function for handling clicks of the class
    fun clicks()
    {
        //back button
        ivBackButton.setOnClickListener {
            finish()
        }

        // recycler view clicks
        rvHelp.addOnItemTouchListener(RecyclerItemClickListener(this,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
                if (dataList[position]["selected"]=="1")
                    dataList[position]["selected"]="0"

                else
                    dataList[position]["selected"]="1"

                helpAdapter.notifyDataSetChanged()
            }
        }))
    }

    //#################### get_helps API ###################//

    //implementing get_helps API
    private fun getHelp()
    {
        if (commonClass.checkInternetConn(this))
            helpHandler.getHelp(commonClass.getString(commonClass.getString("x_access_token")))

        else
            Toast.makeText(this,resources.getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
    }

    // function for handling response of get help API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            for (i in mainPojo.list.indices)
            {
                val map=mainPojo.list[i]

                map["selected"]="0"

                dataList.add(map)
            }

            setUpRecycler()            //function for setting up recycler
        }

        else
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
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
