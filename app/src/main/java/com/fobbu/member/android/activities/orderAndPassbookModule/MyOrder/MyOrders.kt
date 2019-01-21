package com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.adapter.OrderAdapter
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.toolbar.*

class MyOrders : AppCompatActivity()
{
    lateinit var sharedPref:CommonClass

    lateinit var orderAdapter:OrderAdapter

    lateinit var searchtoolbar: Toolbar

    private lateinit var search_menu: Menu

    internal lateinit var item_search: MenuItem


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_orders)

        initView()

        clicks()
    }
    

    // funcion for handling all the views of the class
    private fun initView()
    {
        sharedPref= CommonClass(this,this)

        setUpRecycler()
    }

    
    // function for setting up the recycler
    private fun setUpRecycler() 
    {
        orderAdapter= OrderAdapter(this)

        rvOrder.layoutManager=LinearLayoutManager(this)

        rvOrder.adapter=orderAdapter
    }

    // function for handling all the clicks of the class
    private fun clicks()
    {
        llAllOrder.setOnClickListener{
            tabAllSelected()
        }


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

       //     getOrderApi()
        }


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

            //  getOrderApi()
        }

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

            //  getOrderApi()
        }

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

            //  getOrderApi()
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

       // getOrderApi()
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

        closeButton.setImageResource(R.drawable.cross_drawer)

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




}
