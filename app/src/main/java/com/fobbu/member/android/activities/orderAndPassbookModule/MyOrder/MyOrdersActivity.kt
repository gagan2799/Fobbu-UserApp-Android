package com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
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
import com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.adapter.OrderAdapter
import com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.presenter.OrderHandler
import com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.presenter.OrderPresenter
import com.fobbu.member.android.activities.orderAndPassbookModule.OrderDetail.OrderDetailActivity
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.toolbar.*

class MyOrdersActivity : AppCompatActivity(),ActivityView
{

    private lateinit var sharedPref: CommonClass

    private lateinit var orderAdapter:OrderAdapter

    lateinit var searchtoolbar: Toolbar

    private lateinit var search_menu: Menu

    private lateinit var item_search: MenuItem

    private lateinit var orderHandler:OrderHandler

    val sortedMap=HashMap<String,Any>()

    var sortedList= ArrayList<HashMap<String,Any>>()

    var dataList= ArrayList<HashMap<String,Any>>()


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_orders)

        initView()

        clicks()
    }
    

    // function for handling all the views of the class
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun initView()
    {
        setSupportActionBar(toolbar)

        setsearchtoolbar()

        sharedPref= CommonClass(this,this)

        orderHandler=OrderPresenter(this,this)

        tabAllSelected()
    }

    
    // function for setting up the recycler
    private fun setUpRecycler() 
    {
        orderAdapter= OrderAdapter(this,dataList)

        rvOrder.layoutManager=LinearLayoutManager(this)

        rvOrder.adapter=orderAdapter
    }

    // function for handling all the clicks of the class
    private fun clicks()
    {
        // ALL tab
        llAllOrder.setOnClickListener{
            tabAllSelected()
        }


        // finish
        ivBackButton.setOnClickListener {
            finish()
        }

        //recycler view clicks
        rvOrder.addOnItemTouchListener(RecyclerItemClickListener(this,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
             val map=dataList[position]
                for (i in dataList.indices)
                {
                     val hashMap=dataList[i]

                    if (map["order_id"]==hashMap["order_id"])
                    {
                        sortedMap.putAll(hashMap)

                        sortedList.add(sortedMap)
                    }
                }
                startActivity(
                    Intent(this@MyOrdersActivity, OrderDetailActivity::class.java)
                        .putExtra("service_list",sortedList))
            }

        }))

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


        // RSA tab
        llRsaOrder.setOnClickListener{
            tvAllOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewAll.visibility=View.GONE

            tvRsaOrder.setTextColor(resources.getColor(R.color.red))

            viewRSA.visibility=View.VISIBLE

            tvOdsOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewOds.visibility=View.GONE

            tvLsdOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewLSD.visibility=View.GONE

            tvFreeOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewFree.visibility=View.GONE

           getOrderApi("RSA")
        }


        // ODS tab
        llOdsOrder.setOnClickListener{
            tvAllOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewAll.visibility=View.GONE

            tvRsaOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewRSA.visibility=View.GONE

            tvOdsOrders.setTextColor(resources.getColor(R.color.red))

            viewOds.visibility=View.VISIBLE

            tvLsdOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewLSD.visibility=View.GONE

            tvFreeOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewFree.visibility=View.GONE

              getOrderApi("ODS")
        }

        // LSD Tab
        llLsdOrder.setOnClickListener{
            tvAllOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewAll.visibility=View.GONE

            tvRsaOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewRSA.visibility=View.GONE

            tvOdsOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewOds.visibility=View.GONE

            tvLsdOrders.setTextColor(resources.getColor(R.color.red))

            viewLSD.visibility=View.VISIBLE

            tvFreeOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewFree.visibility=View.GONE

              getOrderApi("LSD")
        }

        // Free Tab
        llFreeOrder.setOnClickListener{
            tvAllOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewAll.visibility=View.GONE

            tvRsaOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewRSA.visibility=View.GONE

            tvOdsOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewOds.visibility=View.GONE

            tvLsdOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            viewLSD.visibility=View.GONE

            tvFreeOrders.setTextColor(resources.getColor(R.color.red))

            viewFree.visibility=View.VISIBLE

              getOrderApi("FREE")
        }

    }

    // change in layout when tab all is  selected
    private fun tabAllSelected()
    {
        tvAllOrder.setTextColor(resources.getColor(R.color.red))

        viewAll.visibility= View.VISIBLE

        tvRsaOrder.setTextColor(resources.getColor(R.color.colorPrimaryDark))

        viewRSA.visibility=View.GONE

        tvOdsOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

        viewOds.visibility=View.GONE

        tvLsdOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

        viewLSD.visibility=View.GONE

        tvFreeOrders.setTextColor(resources.getColor(R.color.colorPrimaryDark))

        viewFree.visibility=View.GONE

        getOrderApi("ALL")
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
                searchtoolbar!!.visibility = View.GONE
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

        // set the cursor

        val searchTextView = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try
        {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")

            mCursorDrawableRes.isAccessible = true

            // mCursorDrawableRes.set(searchTextView, R.drawable.search) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

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
                Log.i("query", "" + query)

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


    //******************get order API*******************//

    //function for hitting get order API
    private fun getOrderApi(type:String)
    {
        if (sharedPref.checkInternetConn(this))
        {
            orderHandler.getOrder(type,"1",CommonClass(this, this).getString("x_access_token"))
        }
        else
        {
        Toast.makeText(this,resources.getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
        }
    }

    // handling response of the get order API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
    if (mainPojo.success=="true")
    {
        dataList=mainPojo.list
        if (dataList.size>0)
        {
            rvOrder.visibility=View.VISIBLE

            tvNOdataOrder.visibility=View.GONE

            setUpRecycler()
        }
        else
        {
            rvOrder.visibility=View.GONE

            tvNOdataOrder.visibility=View.VISIBLE
        }

    }
        else{
        println("message:: ${mainPojo.message}")
    }
    }

    override fun showLoader()
    {

    }

    override fun hideLoader()
    {

    }


}
