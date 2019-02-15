package com.fobbu.member.android.activities.paymentModule.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.sliding_cards.view.*

class OdsSlideAdapter(var context: Context, var list:ArrayList<Int>): PagerAdapter(){
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return if (list.size==0)
            1
        else
            6
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val view=LayoutInflater.from(context).inflate(R.layout.sliding_cards,container,false)

        if (list.size==0)
        {
        view.loaderOds.visibility=View.VISIBLE

            view.ivSlidingImage.visibility=View.GONE
        }else
        {
            view.loaderOds.visibility=View.GONE

            view.ivSlidingImage.visibility=View.VISIBLE

            view.ivSlidingImage.setImageResource(R.drawable.hill)
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}