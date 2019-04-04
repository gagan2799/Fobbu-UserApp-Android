package com.fobbu.member.android.activities.profileModule.profile.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class ProfileLangaugeAdapter(var activity: Activity,var selectedLanguageList:ArrayList<HashMap<String,Any>>) :RecyclerView.Adapter<ProfileLangaugeAdapter.ProfileViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProfileViewHolder
    {
        return ProfileViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_selected_language,p0,false))
    }

    override fun getItemCount(): Int
    {
        return  selectedLanguageList.size
    }

    override fun onBindViewHolder(p0: ProfileViewHolder, p1: Int)
    {
        p0.tvSelectedLanguage.text=selectedLanguageList[p1]["language"].toString()
    }

    // inner view holder class
    class ProfileViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        var tvSelectedLanguage:TextView=view.findViewById(R.id.tvSelectedLanguageProfile)
    }
}