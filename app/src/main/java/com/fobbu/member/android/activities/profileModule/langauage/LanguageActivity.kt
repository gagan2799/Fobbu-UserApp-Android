package com.fobbu.member.android.activities.profileModule.langauage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.*
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.profileModule.langauage.adapter.LangaugeAdapter
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_language.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class LanguageActivity : AppCompatActivity(),Filterable
{
    var filteredLanguageList=ArrayList<HashMap<String,Any>>()

    lateinit var searchtoolbar: Toolbar

    private lateinit var search_menu: Menu

    private lateinit var item_search: MenuItem

    private lateinit var languageData:Array<String>

    private lateinit var languageAdapter: LangaugeAdapter

    private var selectedLanguageList=ArrayList<HashMap<String,Any>>()

    private var languageList=ArrayList<HashMap<String,Any>>()

    lateinit var commonClass:CommonClass

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_language)

        initView()           //function for initialising all the views of the class

        clicks()                     //function for handling the clicks of the class
    }

    //function for initialising all the views of the class
    private fun initView()
    {
        setSupportActionBar(toolbar)

        setsearchtoolbar()

        commonClass= CommonClass(this,this)

        languageData=resources.getStringArray(R.array.fobbu_langauage_array)

        Arrays.sort(languageData)

        for (i in languageData.indices)
        {
            val languageMap=HashMap<String,Any>()

            languageMap["language"]=languageData[i]

            languageMap["selected"]="0"

            languageList.add(languageMap)

            if (commonClass.getStringList(RsaConstants.Constants.selectedLanguageList).isNotEmpty())
            {
                selectedLanguageList=commonClass.getStringList(RsaConstants.Constants.selectedLanguageList)

                for (i in languageList.indices)
                {
                    for (j in selectedLanguageList.indices)
                    {
                        if (languageList[i]["language"]==selectedLanguageList[j]["language"])
                            if (languageList[i]["selected"]!=selectedLanguageList[j]["selected"])
                                languageList[i]["selected"]=selectedLanguageList[j]["selected"].toString()

                    }
                }
            }
        }
        filter.filter("")        // filter object for performing search operation
    }

    //function for handling the clicks of the class
    private fun clicks()
    {
        // search
        ivSearchToolbar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.searchtoolbar, 1, true, true)      // function for adding circle reveal animation to the search tool bar

            else
                searchtoolbar.visibility = View.VISIBLE

            item_search.expandActionView()
        }

        ivBackButton.setOnClickListener {
            finish()
        }

        rvLanguageCheck.addOnItemTouchListener(RecyclerItemClickListener(this,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
                if (filteredLanguageList[position]["selected"]=="0")
                    filteredLanguageList[position]["selected"]="1"

                else
                    filteredLanguageList[position]["selected"]="0"

                when
                {
                    filteredLanguageList[position]["selected"]=="1"->
                    {
                        if (selectedLanguageList.size<3)
                        {
                            val map = filteredLanguageList[position]

                            selectedLanguageList.add(map)
                        }
                        else
                        {
                            filteredLanguageList[position]["selected"]="0"

                            commonClass.showToast(resources.getString(R.string.limit_of_selected_languages_msg),rlLanguage)
                        }
                    }

                    else->
                    {
                        if (selectedLanguageList.isNotEmpty())
                        {
                            for (i in selectedLanguageList.size-1 downTo 0)
                            {
                                if (selectedLanguageList[i]["language"]==filteredLanguageList[position]["language"])
                                {
                                    selectedLanguageList.removeAt(i)

                                    break
                                }
                            }
                        }
                    }
                }


                commonClass.putStringList(RsaConstants.Constants.selectedLanguageList,selectedLanguageList)

                languageAdapter.notifyDataSetChanged()
            }
        }))
    }

    //function for setting up recycler
    private fun setRecycler()
    {
        languageAdapter=LangaugeAdapter(this,filteredLanguageList)

        rvLanguageCheck.layoutManager=LinearLayoutManager(this)

        rvLanguageCheck.adapter=languageAdapter
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
                circleReveal(R.id.searchtoolbar, 1, true, false)  // function for adding circle reveal animation to the search tool bar

            else
                searchtoolbar.visibility = View.GONE
        }

        item_search = search_menu.findItem(R.id.action_filter_search)

        MenuItemCompat.setOnActionExpandListener(item_search, object : MenuItemCompat.OnActionExpandListener
        {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean
            {
                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchtoolbar, 1, true, false)     // function for adding circle reveal animation to the search tool bar

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
        initSearchView()          // function for initialising the variables and views of the search tool bar
    }

    // function for initialising the variables and views of the search tool bar
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean
            {
                callSearch(query)      // function for performing search operation on the list

                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean
            {
                callSearch(newText)     // function for performing search operation on the list

                return true
            }

            // function for performing search operation on the list
            fun callSearch(query: String)
            {
                filter.filter(query)        // filter object for performing search operation
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

    // filter object for performing search operation
    override fun getFilter(): Filter {

        return object : Filter()
        {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults
            {
                val charString = charSequence.toString()

                if (charString.isEmpty())
                {
                    filteredLanguageList=languageList
                }
                else
                {
                    val filteredList = ArrayList<HashMap<String,Any>>()
                    for (row in languageList)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row["language"].toString().toLowerCase().contains(charString.toLowerCase()) )
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

                setRecycler()       //function for setting up recycler
            }
        }
    }

}
