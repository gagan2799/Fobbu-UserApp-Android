package com.fobbu.member.android.activities.selectVehicleActivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.selectVehicleActivity.adapter.Vehicle2WheelerAdapter
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListPresenter
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_select_vehicle.*
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.ArrayList


class SelectVehicleActivity : AppCompatActivity(),ActivityView,Filterable
{

    private var dataListMain: ArrayList<HashMap<String, Any>> = ArrayList()

    private var dataListTwo: ArrayList<HashMap<String, Any>> = ArrayList()

    private var dataListFour: ArrayList<HashMap<String, Any>> = ArrayList()

    lateinit var commonClass:CommonClass

    var vehicleType="2wheeler"

    lateinit var searchtoolbar: Toolbar

    lateinit var vehicle2WheelerAdapter:Vehicle2WheelerAdapter

    private lateinit var search_menu: Menu

    private lateinit var item_search: MenuItem

    private lateinit var vehicleHandler: VehicleListHandler

    lateinit var filteredLanguageList:ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_select_vehicle)

        initView()

        clicks()
    }


    // function for initialising all the variables of the class
    private fun initView()
    {
        setSupportActionBar(toolbar)

        setsearchtoolbar()

        commonClass= CommonClass(this,this)

        vehicleHandler= VehicleListPresenter(this, this)

        vehicleListApi()

        filteredLanguageList= ArrayList()
        
        fobbu_icon.visibility= View.GONE

        toolbarHeading.visibility=View.VISIBLE

        setupTabIconsAndText()

        set2WheelerRecycler()

        toolbarHeading.text=resources.getString(R.string.select_vehicle)
    }

    // function for setting 2 wheeler recycler
    private fun set2WheelerRecycler()
    {
        vehicle2WheelerAdapter= Vehicle2WheelerAdapter(this,filteredLanguageList)

        rvWheelerVehicle.layoutManager=LinearLayoutManager(this)

        rvWheelerVehicle.adapter=vehicle2WheelerAdapter
    }

    // function for handling all the clicks of the class
    private fun clicks()
    {
        // search
        ivSearchToolbar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                circleReveal(R.id.searchtoolbar, 1, true, true)
            }
            else
                searchtoolbar.visibility = View.VISIBLE

            item_search.expandActionView()
        }

        rvWheelerVehicle.addOnItemTouchListener(RecyclerItemClickListener(this,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
                commonClass.putString(RsaConstants.Ods.vehicleNumberSelect,filteredLanguageList[position]["vehicle_registration_number"].toString())

                commonClass.putString(RsaConstants.Ods.vehicleTypeSelect,vehicleType)

              finish()
            }

        }))


        ivBackButton.setOnClickListener { finish() }

        tlVehicle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
               when(p0?.position)
               {
                   0->
                   {
                       vehicleType="2wheeler"
                       vehicleListApi()
                   }
                   1->
                   {
                       vehicleType="4wheeler"
                       vehicleListApi()
                   }
               }
            }

        })

    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setsearchtoolbar()
    {
        searchtoolbar = findViewById(R.id.searchtoolbar)

        searchtoolbar.inflateMenu(R.menu.menu_search)

        search_menu = searchtoolbar.menu

        searchtoolbar.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            //searchtoolbar!!.visibility = View.VISIBLE
                circleReveal(R.id.searchtoolbar, 1, true, false)
            else
                searchtoolbar.visibility = View.GONE
        }

        item_search = search_menu.findItem(R.id.action_filter_search)

        MenuItemCompat.setOnActionExpandListener(item_search, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    circleReveal(R.id.searchtoolbar, 1, true, false)
                } else
                    searchtoolbar.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                return true
            }
        })

        initSearchView()


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun initSearchView()
    {
        val searchView = search_menu.findItem(R.id.action_filter_search).actionView as SearchView

        // Enable/Disable Submit button in the keyboard

        searchView.isSubmitButtonEnabled = false

        searchView.setBackgroundResource(R.drawable.et_blue_base)

        val searchplate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate) as View

        searchplate.setBackgroundResource(R.drawable.et_white_base)

        // Change search close button image

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView

        closeButton.setImageResource(R.drawable.cross_white)

        // set hint and the text colors

        val txtSearch = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText

        txtSearch.hint = "Search.."

        txtSearch.setTextColor(resources.getColor(R.color.white))

        txtSearch.setHintTextColor(resources.getColor(R.color.white))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                //Do searching
                filter.filter(query)
            }

        })

    }


    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(viewID: Int, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean) {
        val myView = findViewById<View>(viewID)

        var width = myView.width

        if (posFromRight > 0)
            width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
                R.dimen.abc_action_button_min_width_material
            )
        if (containsOverflow)
            width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

        val cx = width + 9
        val cy = myView.height / 2

        val anim: Animator
        anim = if (isShow)
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, width.toFloat())
        else
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView.visibility = View.INVISIBLE
                }
            }
        })

        // make the view visible and start the animation
        if (isShow)
            myView.visibility = View.VISIBLE

        // start the animation
        anim.start()
    }

    private fun setupTabIconsAndText() {

        tlVehicle.addTab(tlVehicle.newTab().setText("2 wheeler"),true)
        tlVehicle.addTab(tlVehicle.newTab().setText("4 wheeler"))
        
        tlVehicle.getTabAt(0)?.setIcon(R.drawable.scooter_gray)
        tlVehicle.getTabAt(1)?.setIcon(R.drawable.car_gray)
        tlVehicle.getTabAt(0)?.text = "2 wheeler"
        tlVehicle.getTabAt(1)?.text = "4 wheeler"
    }


    override fun getFilter(): Filter {

        return object : Filter()
        {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults
            {
                val charString = charSequence.toString()

                if (charString.isEmpty())
                {
                    filteredLanguageList=dataListMain
                }
                else
                {
                    val filteredList = ArrayList<HashMap<String,Any>>()
                    for (row in dataListMain)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row["vehicle_brand"].toString().toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(row)
                        }
                    }
                    filteredLanguageList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filteredLanguageList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults)
            {
                filteredLanguageList = filterResults.values as ArrayList<HashMap<String,Any>>

                set2WheelerRecycler()
            }
        }
    }

    //******************* vehicle list API ********************//
    /// Vehicle List API  (API - users/vehicles)
    private fun vehicleListApi() {

        // rlLoaderVehicleList.visibility = View.VISIBLE

        if (CommonClass(this, this).checkInternetConn(this)) {

            val tokenHeader = CommonClass(this, this).getString("x_access_token")

            val userId = CommonClass(this, this).getString("_id")

            vehicleHandler.sendVehicleData(tokenHeader,userId)
        }
    }

    // handling response of the vehicle list API
    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        // rlLoaderVehicleList.visibility = View.GONE
        if (mainPojo.success == "true")
        {
            dataListMain.clear()

            dataListFour.clear()

            dataListTwo.clear()

            for (i in mainPojo.vehicles.indices)
            {
                if (mainPojo.vehicles[i]["vehicle_type"] == "4wheeler")
                    dataListFour.add(mainPojo.vehicles[i])
                else
                    dataListTwo.add(mainPojo.vehicles[i])
            }

            if (vehicleType=="4wheeler")
            {
                dataListMain.addAll(dataListFour)

                //vehicleAdapter.notifyDataSetChanged()

                if(dataListMain.size>0)
                {
                    tvNodataVehcile.visibility=View.GONE

                    rvWheelerVehicle.visibility=View.VISIBLE
                }
                else
                {
                    tvNodataVehcile.visibility=View.VISIBLE

                    tvNodataVehcile.text=getString(R.string.no_data_msg)

                    rvWheelerVehicle.visibility=View.GONE
                }
            }
            else
            {
                dataListMain.addAll(dataListTwo)

                //vehicleAdapter.notifyDataSetChanged()

                if(dataListMain.size>0)
                {
                    tvNodataVehcile.visibility=View.GONE

                    rvWheelerVehicle.visibility=View.VISIBLE

                }
                else
                {
                    tvNodataVehcile.visibility=View.VISIBLE

                    tvNodataVehcile.text=getString(R.string.no_data_msg)

                    rvWheelerVehicle.visibility=View.GONE
                }
            }

            filter.filter("")
        }
    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }
}
