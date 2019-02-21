package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class OdsSubServiceAdapter (var activity: Context, var dataList:ArrayList<String>):
RecyclerView.Adapter<OdsSubServiceAdapter.OdsSubServiceViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdsSubServiceViewHolder
    {
        return OdsSubServiceViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_ods_sub_service,p0,false))
    }

    override fun getItemCount(): Int
    {
    return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: OdsSubServiceViewHolder, p1: Int)
    {
     p0.tvSubService.text= """${p1 + 1}: ${dataList[p1]}"""
    }

    class OdsSubServiceViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val tvSubService:TextView=view.findViewById(R.id.tvSubServiceOds)
    }
}