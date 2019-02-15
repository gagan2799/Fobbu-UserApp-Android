package com.fobbu.member.android.activities.paymentModule

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.adapter.OdsSlideAdapter
import com.fobbu.member.android.utils.CardsPagerTransformerBasic
import com.fobbu.member.android.utils.CommonClass
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_ods_get_set_go.*

class OdsGetSetGoActivity : AppCompatActivity()
{
    lateinit var commonClass:CommonClass

    lateinit var adapter: OdsSlideAdapter

    var list=ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ods_get_set_go)

        initView()

        clicks()
    }

    //function for initialising all the views of the class
    private fun initView()
    {
       commonClass= CommonClass(this,this)

        setUpSlider()
    }


    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            list.add(R.drawable.hill)

            setUpSlider()
        },5000)

    }

    //function for handling clicks of the class
    private fun clicks()
    {
        ivShareOdsGet.setOnClickListener {
            commonClass.shareIt(this)
        }
    }


    // functiom for setting up slider
    private fun  setUpSlider()
    {
        adapter=OdsSlideAdapter(this,list)

        picker.adapter=adapter

        val density = resources.displayMetrics.density
        val partialWidth = (16 * density).toInt() // 16dp
        val pageMargin = (8 * density).toInt() // 8dp

        val viewPagerPadding = partialWidth + pageMargin

        picker.pageMargin = pageMargin

        picker.setPadding(viewPagerPadding, 0, viewPagerPadding, 0)

        picker.setPageTransformer(true,CardsPagerTransformerBasic(2,5, 0.7F))

    }




}
