package com.fobbu.member.android.activities.rsaModule.adapter

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.HashMap

class RsaRecyclerAdapter(private var activtiy:Activity, private var textList: ArrayList<HashMap<String, Any>>): RecyclerView.Adapter<RsaRecyclerAdapter.MyRsaViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyRsaViewHolder
    {
        val view:View=LayoutInflater.from(activtiy).inflate(R.layout.inflate_rsa_recycler_layout,p0,false)

        return MyRsaViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return textList.size
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: MyRsaViewHolder, position: Int)
    {
        if (textList[position]["image"].toString() != "")
            Picasso.get().load(textList[position]["image"].toString())
                .error(R.drawable.dummy_services)
                .into(holder.imageViewRsaRecycler)

        else
            holder.imageViewRsaRecycler.setImageResource(R.drawable.dummy_services)


        holder.textViewRSaRecycler.text = textList[position]["reason"].toString()

        if(textList[position]["select"]=="1")
        {
            holder.textViewRSaRecycler.setTextColor(activtiy.resources.getColor(R.color.red))

            holder.relativeLayoutCancelBorder.background= activtiy.getDrawable(R.drawable.red_border)
        }
        else
        {
            holder.relativeLayoutCancelBorder.background= activtiy.getDrawable(R.drawable.border_line_grey)

            holder.linearLayoutRsaCancel.setBackgroundColor(activtiy.resources.getColor(R.color.white))

            holder.textViewRSaRecycler.setTextColor(activtiy.resources.getColor(R.color.drawer_text_color))
        }
    }

    // inner view holder class
    class  MyRsaViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var imageViewRsaRecycler:ImageView= view.findViewById(R.id.imageViewRecycleRsaCancel)

        var linearLayoutRsaCancel:LinearLayout= view.findViewById(R.id.linearLayoutRsaCancel)

        var textViewRSaRecycler: TextView = view.findViewById(R.id.textViewRsaCancelRecycle)

        var relativeLayoutCancelBorder: RelativeLayout = view.findViewById(R.id.relativeLayoutCancelBorder)
    }


}