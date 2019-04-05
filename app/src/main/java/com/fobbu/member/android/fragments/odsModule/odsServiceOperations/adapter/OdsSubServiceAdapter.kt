package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.view.SelectedSubServiceView
import com.fobbu.member.android.utils.CommonClass
import java.util.logging.Handler

class OdsSubServiceAdapter (var activity: Activity, var dataList:ArrayList<HashMap<String,Any>>):
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
        p0.tvSubService.text = """${p1 + 1}: ${dataList[p1]["name"]}"""

        when (dataList[p1]["selected"])
        {
            "0" ->
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_uncheck)

            else ->
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_checked)
        }
    }

    // inner view holder class
    class OdsSubServiceViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val tvSubService:TextView=view.findViewById(R.id.tvSubServiceOds)

        val ivSubServiceChecked:ImageView=view.findViewById(R.id.ivSubServiceChecked)
    }

}