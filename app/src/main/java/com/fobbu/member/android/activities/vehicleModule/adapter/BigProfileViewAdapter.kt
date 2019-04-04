package com.fobbu.member.android.activities.vehicleModule.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.inflate_big_profile.view.*
import kotlinx.android.synthetic.main.sliding_cards.view.*
import java.io.File

class BigProfileViewAdapter(var context: Context, var list:ArrayList<Any>): PagerAdapter()
{
    override fun isViewFromObject(p0: View, position: Any): Boolean
    {
        return p0 == position
    }

    override fun getCount(): Int
    {
        return  list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val view= LayoutInflater.from(context).inflate(R.layout.inflate_big_profile,container,false)

        if (list[position].toString().startsWith("https"))
            Glide.with(context)
                .load(list[position])
                .into(view.imgBig)

        else
        {
            val file= File(list[position].toString())

            Glide.with(context)
                .load(file)
                .into(view.imgBig)
        }
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
    {
        container.removeView(`object` as View?)
    }

}