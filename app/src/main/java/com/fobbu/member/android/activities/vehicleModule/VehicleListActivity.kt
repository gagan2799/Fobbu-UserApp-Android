package com.fobbu.member.android.activities.vehicleModule

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.adapter.VehicleAdapter
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditActivityHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditVehiclePresenter
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListPresenter
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.interfaces.DeleteVehicleClickListener
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.HashMap

class VehicleListActivity : AppCompatActivity(),ActivityView,DeleteVehicleClickListener,AddEditVehicleAcivityView,Filterable
{
    private lateinit var webServiceApi: WebServiceApi

    lateinit var searchView:SearchView

    var filteredList=ArrayList<HashMap<String,Any>>()

    private var vehicleType = "2wheeler"

    private lateinit var vehicleHandler: VehicleListHandler

    private lateinit var addEditHandler: AddEditActivityHandler

    private var dataListMain: ArrayList<HashMap<String, Any>> = ArrayList()

    private var dataListTwo: ArrayList<HashMap<String, Any>> = ArrayList()

    private var dataListFour: ArrayList<HashMap<String, Any>> = ArrayList()

    var fromWhere = ""

    var vehicleID=""

    lateinit var searchtoolbar: Toolbar

    private lateinit var search_menu: Menu

    private lateinit var item_search: MenuItem

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vehicle_list)

        initialise()               //// All initialise in this method for this class

        addClicks()                  /// All click on buttons handled here
    }

    //// All initialise in this method for this class
    private fun initialise()
    {
        ivBackButton.setImageResource(R.drawable.cross_gray)

        setSupportActionBar(toolbar)

        setsearchtoolbar()

        vehicleHandler= VehicleListPresenter(this, this)

        addEditHandler= AddEditVehiclePresenter(this, this)

        webServiceApi = getEnv().getRetrofit()

        if (intent.hasExtra("from_where"))
            fromWhere = "RSA"
    }

    /// All click on buttons handled here
    @SuppressLint("SetTextI18n")
    private fun addClicks()
    {
        // search
        ivSearchToolbar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.searchtoolbar, 1, true, true)    //  adding circle reveal animation

            else
                searchtoolbar.visibility = View.VISIBLE

            item_search.expandActionView()
        }

        ivBackButton.setOnClickListener {
            startActivity(Intent(this,AddEditVehicleActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

            finish()
        }

        tvAddEditVehicle.setOnClickListener {
            startActivity(Intent(this,AddEditVehicleActivity::class.java))

            finish()
        }

        tvScooter.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            //searchtoolbar!!.visibility = View.VISIBLE
                circleReveal(R.id.searchtoolbar, 1, true, false)   //  adding circle reveal animation
            else
                searchtoolbar!!.visibility = View.VISIBLE

            if (searchView!=null && searchView!=null)
            {
                searchView.clearFocus()

                item_search.collapseActionView()
            }

            if(vehicleType!="2wheeler")
            {
                vehicleType ="2wheeler"

                manageLayout(vehicleType)         // function for managing layout

                filter.filter("")   // filter object for performing search operation
            }
        }

        tvCar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            //searchtoolbar!!.visibility = View.VISIBLE
                circleReveal(R.id.searchtoolbar, 1, true, false)    //  adding circle reveal animation

            else
                searchtoolbar!!.visibility = View.VISIBLE

            if (searchView!=null && searchView!=null)
            {
                searchView.clearFocus()

                item_search.collapseActionView()
            }

            if(vehicleType!="4wheeler")
            {
                vehicleType ="4wheeler"

                manageLayout(vehicleType)       // function for managing layout

                filter.filter("") // filter object for performing search operation
            }
        }
    }

    // function for setting up recycler
    private fun setupRecycler()
    {
        /// Vehicle Adapter handled here
        recyclerViewVehicles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        vehicleAdapter = VehicleAdapter(this, filteredList)

        recyclerViewVehicles.adapter = vehicleAdapter

        vehicleAdapter.notifyDataSetChanged()
    }

    // function for setting up search tool bar
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setsearchtoolbar()
    {
        searchtoolbar = findViewById(R.id.searchtoolbar)

        searchtoolbar.inflateMenu(R.menu.menu_search)

        search_menu = searchtoolbar.menu

        searchtoolbar.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            //searchtoolbar!!.visibility = View.VISIBLE
                circleReveal(R.id.searchtoolbar, 1, true, false)   //  adding circle reveal animation

            else
                searchtoolbar!!.visibility = View.GONE
        }

        item_search = search_menu.findItem(R.id.action_filter_search)

        MenuItemCompat.setOnActionExpandListener(item_search, object : MenuItemCompat.OnActionExpandListener
        {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean
            {
                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, false)         //  adding circle reveal animation

                else
                    searchtoolbar.visibility = View.GONE

                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean
            {
                // Do something when expanded
                return true
            }
        })
        initSearchView()        // function for initialising all the variables and views of the class
    }

    // function for initialising all the variables and views of the class
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun initSearchView()
    {
        searchView = search_menu.findItem(R.id.action_filter_search).actionView as SearchView

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

        // set the cursor
        val searchTextView = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView

        try
        {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")

            mCursorDrawableRes.isAccessible = true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean
            {
                callSearch(query)        // function for performing search operation

                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean
            {
                callSearch(newText)        // function for performing search operation

                return true
            }

            // function for performing search operation
            fun callSearch(query: String)
            {
                filter.filter(query)     // filter object for performing search operation
            }
        })
    }

    // function for adding circle reveal animation to the search tool bar
    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(viewID: Int, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean)
    {
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
        anim.addListener(object : AnimatorListenerAdapter()
        {
            override fun onAnimationEnd(animation: Animator)
            {
                if (!isShow)
                {
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


    override fun onBackPressed()
    {
        if (fromWhere == "RSA")
            startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

        else
            startActivity(Intent(this,AddEditVehicleActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

        finish()
    }


    // function for managing layout
    private fun manageLayout(vehcicleTypeIntent:String)
    {
        when (vehcicleTypeIntent)
        {
            "4wheeler" ->
            {
                tvScooter.setImageResource(R.drawable.scooter_gray)

                view_scooter.visibility = View.GONE

                tvCar.setImageResource(R.drawable.car_red)

                view_car.visibility = View.VISIBLE

                tvCar.requestFocus()

                dataListMain.clear()

                dataListMain.addAll(dataListFour)

                filter.filter("")   // filter object for performing search operation

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE

                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE

                    tvNodata.text="No 4 Wheeler Added"

                    recyclerViewVehicles.visibility=View.GONE
                }
            }

            "2wheeler"->
            {
                tvScooter.setImageResource(R.drawable.scooter_red)

                view_scooter.visibility = View.VISIBLE

                tvCar.setImageResource(R.drawable.car_gray)

                view_car.visibility = View.GONE

                dataListMain.clear()

                dataListMain.addAll(dataListTwo)

                filter.filter("")    // filter object for performing search operation

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE

                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE

                    tvNodata.text="No 2 Wheeler Added"

                    recyclerViewVehicles.visibility=View.GONE
                }
            }
        }
    }

    override fun onViewClick(vehicleID: String)
    {
        deleteVehiclePopup(vehicleID)       ///DELETE VEHICLE  POPUP
    }

    //////////////////////////////// Vehicle List API  (API - users/vehicles(GET)) //////////////////////////////////////////////////

    /// Vehicle List API  (API - users/vehicles(GET))
    private fun vehicleListApi()
    {
        if (CommonClass(this, this).checkInternetConn(this))
        {
            val tokenHeader = CommonClass(this, this).getString("x_access_token")

            val userId = CommonClass(this, this).getString("_id")

            vehicleHandler.sendVehicleData(tokenHeader,userId)
        }
        else
            CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable))
    }

    /// Vehicle List Response (API -users/vehicles(GET))
    @SuppressLint("SetTextI18n")
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
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
                tvScooter.setImageResource(R.drawable.scooter_gray)

                view_scooter.visibility = View.GONE

                tvCar.setImageResource(R.drawable.car_red)

                view_car.visibility = View.VISIBLE

                dataListMain.addAll(dataListFour)

                filter.filter("")   // filter object for performing search operation

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE

                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE

                    tvNodata.text="No 2 Wheeler Added"

                    recyclerViewVehicles.visibility=View.GONE
                }
            }
            else
            {
                tvScooter.setImageResource(R.drawable.scooter_red)

                view_scooter.visibility = View.VISIBLE

                tvCar.setImageResource(R.drawable.car_gray)

                view_car.visibility = View.GONE

                dataListMain.addAll(dataListTwo)

                filter.filter("")       // filter object for performing search operation

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE

                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE

                    tvNodata.text="No 2 Wheeler Added"

                    recyclerViewVehicles.visibility=View.GONE
                }
            }

            // setting up recycler
            filter.filter("")           // filter object for performing search operation

            if (intent.hasExtra("vehicle_type"))
            {
                vehicleType=intent.getStringExtra("vehicle_type")

                manageLayout(intent.getStringExtra("vehicle_type"))           // function for managing layout
            }
        }
    }

    override fun showLoader()
    {
        rlLoaderVehicleList.visibility=View.VISIBLE
    }

    override fun hideLoader()
    {
        rlLoaderVehicleList.visibility=View.GONE
    }

    private fun getEnv(): MyApplication
    {
        return application as MyApplication
    }

    ///DELETE VEHICLE  POPUP
    private fun deleteVehiclePopup(vehicleID: String)
    {
        this.vehicleID =vehicleID

        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle(resources.getString(R.string.Delete))

        alertDialog.setMessage(resources.getString(R.string.are_you_sure_you_want_to_delete))

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()

            deleteVehicle()
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
    //************************ users/vehicles (DELETE) API  ****************************//

    // implementing users/vehicles (DELETE) API
    private fun deleteVehicle()
    {
        if (CommonClass(this,this).checkInternetConn(this))
        {
            val tokenHeader = CommonClass(this, this).getString("x_access_token")

            val userId = CommonClass(this, this).getString("_id")

            addEditHandler.deleteVehicle(tokenHeader,vehicleID,userId)
        }
        else
            CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable))

    }

    // function for handling the response of the users/vehicles (DELETE) API
    override fun onDeleteVehicleSuccessUpdateVehicle(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            for (i in dataListMain.size-1 downTo 0)
            {
                if (vehicleID==dataListMain[i]["_id"])
                {
                    dataListMain.removeAt(i)

                    break
                }
            }

            for (i in dataListTwo.size-1 downTo 0)
            {
                if (vehicleID==dataListTwo[i]["_id"])
                {
                    dataListTwo.removeAt(i)

                    break
                }
            }

            for (i in dataListFour.size-1 downTo 0)
            {
                if (vehicleID==dataListFour[i]["_id"])
                {
                    dataListFour.removeAt(i)

                    break
                }
            }
            vehicleAdapter.notifyDataSetChanged()
        }
        else
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestSuccessReportEdit(mainPojo: MainPojo) {}

    override fun onRequestSuccessUpdateVehicle(mainPojo: MainPojo) {}

    // filter object for performing search operation
    override fun getFilter(): Filter
    {
        return object : Filter()
        {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults
            {
                val charString = charSequence.toString()

                if (charString.isEmpty())
                    filteredList=dataListMain

                else
                {
                    val filteredCustomList = ArrayList<HashMap<String,Any>>()

                    for (row in dataListMain)
                    {
                        // name match condition. this might differ depending on your requirement

                        // here we are looking for name or phone number match
                        if (row["vehicle_brand"].toString().toLowerCase().contains(charString.toLowerCase()) )
                            filteredCustomList.add(row)
                    }
                    filteredList = filteredCustomList
                }
                val filterResults = Filter.FilterResults()

                filterResults.values = filteredList

                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults)
            {
                filteredList = filterResults.values as ArrayList<HashMap<String,Any>>

                setupRecycler()          // function for setting up recycler
            }
        }
    }

    override fun onResume()
    {
        super.onResume()

        vehicleListApi()              /// Vehicle List API  (API - users/vehicles(GET))
    }
}