package com.fobbu.member.android.activities.rsaModule.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.HashMap

class RsaRecyclerAdapter(private var activtiy:Activity, private var textList: ArrayList<HashMap<String, Any>>): RecyclerView.Adapter<RsaRecyclerAdapter.MyRsaViewHolder>() {

    internal lateinit var reason:String
    var row_index:Int=0
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyRsaViewHolder {

       val view:View=LayoutInflater.from(activtiy).inflate(R.layout.inflate_rsa_recycler_layout,p0,false)
        return MyRsaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return textList.size
    }

    override fun onBindViewHolder(holder: MyRsaViewHolder, position: Int) {

        if (textList[position]["image"].toString() != "")
            Picasso.get().load(textList[position]["image"].toString())
                .error(R.drawable.dummy_services)
                .into(holder.imageViewRsaRecycler)
        else
            holder.imageViewRsaRecycler.setImageResource(R.drawable.dummy_services)


        holder.textViewRSaRecycler.text = textList[position]["reason"].toString()

        if(textList[position]["select"]=="1")
        {
            holder.linearLayoutRsaCancel.setBackgroundColor(activtiy.resources.getColor(R.color.color_grey))
            holder.textViewRSaRecycler.setTextColor(activtiy.resources.getColor(R.color.white))
        }
        else
        {
            holder.linearLayoutRsaCancel.setBackgroundColor(activtiy.resources.getColor(R.color.white))
            holder.textViewRSaRecycler.setTextColor(activtiy.resources.getColor(R.color.drawer_text_color))
        }

    }


    class  MyRsaViewHolder(view: View): RecyclerView.ViewHolder(view)
     {
         var imageViewRsaRecycler:ImageView= view.findViewById(R.id.imageViewRecycleRsaCancel)
         var linearLayoutRsaCancel:LinearLayout= view.findViewById(R.id.linearLayoutRsaCancel)
         var textViewRSaRecycler: TextView = view.findViewById(R.id.textViewRsaCancelRecycle)
     }


}