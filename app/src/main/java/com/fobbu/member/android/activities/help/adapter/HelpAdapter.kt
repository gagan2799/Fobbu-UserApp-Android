package com.fobbu.member.android.activities.help.adapter

import android.app.Activity
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.inflate_expandable_layout.view.*
import java.lang.Exception

class HelpAdapter(var activity:Activity, private var dataList:ArrayList<HashMap<String,Any>>): RecyclerView.Adapter<HelpAdapter.HelpViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HelpViewHolder
    {
    return HelpViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_expandable_layout,p0,false) )
    }

    override fun getItemCount(): Int
    {
   return dataList.size
    }

    override fun onBindViewHolder(p0: HelpViewHolder, p1: Int)
    {

        p0.tvQuestion.text=dataList[p1]["questions"].toString()

        p0.tvAnswer.text=dataList[p1]["answer"].toString()

        if (dataList[p1]["selected"]== "1")
               p0.llAnswer.visibility=View.VISIBLE
           else
               p0.llAnswer.visibility=View.GONE


    }


    class HelpViewHolder(view :View) :RecyclerView.ViewHolder(view)
    {
        val tvAnswer: TextView =view.findViewById(R.id.tvHelpAnswer)

        val llAnswer: LinearLayout =view.findViewById(R.id.llAnswer)

        val tvQuestion: TextView =view.findViewById(R.id.tvHelpQuestion)
    }
}