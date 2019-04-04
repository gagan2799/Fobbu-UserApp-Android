package com.fobbu.member.android.activities.profileModule.langauage.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class LangaugeAdapter(var activity:Activity,var languageDataList:ArrayList<HashMap<String,Any>>):
    RecyclerView.Adapter<LangaugeAdapter.LanguageViewHolder>()
{
    
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LanguageViewHolder
    {
    return LanguageViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_language_layout, p0, false))
    }

    override fun getItemCount(): Int
    {
     return languageDataList.size
    }

    override fun onBindViewHolder(p0: LanguageViewHolder, p1: Int)
    {
        p0.tvLanguage.text=languageDataList[p1]["language"].toString()

        when
        {
            languageDataList[p1]["selected"]=="1"->
                p0.cbLanguage.setImageResource(R.drawable.checkbox_checked)

            else->
                p0.cbLanguage.setImageResource(R.drawable.checkbox_uncheck)
        }
    }

    // inner view holder class
    class LanguageViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val tvLanguage: TextView = view.findViewById(R.id.tvLanguageRecycler)

        val cbLanguage: ImageView = view.findViewById(R.id.cblanguageRecycler)
    }
}