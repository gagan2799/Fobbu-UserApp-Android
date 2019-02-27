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

class OdsSubServiceAdapter (var activity: Activity, var dataList:ArrayList<HashMap<String,Any>>,var selectedSubServiceView: SelectedSubServiceView):
    RecyclerView.Adapter<OdsSubServiceAdapter.OdsSubServiceViewHolder>()
{
    var selectedSubServiceList= ArrayList<HashMap<String,Any>>()

    var newSelectedList=ArrayList<HashMap<String,Any>>()

    var selectedPos=0

    init {

        if (CommonClass(activity,activity).getStringList("subServiceList").isNotEmpty())
        {
            selectedSubServiceList=CommonClass(activity,activity).getStringList("subServiceList")
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdsSubServiceViewHolder
    {
        return OdsSubServiceViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_ods_sub_service,p0,false))
    }

    override fun getItemCount(): Int
    {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: OdsSubServiceViewHolder, p1: Int) {
        p0.tvSubService.text = """${p1 + 1}: ${dataList[p1]["name"]}"""


        when (dataList[p1]["selected"]) {
            "0" -> {
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_uncheck)
               /* val map =dataList[p1]
                saveToList(map)*/
            }

            else ->
            {
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_checked)
               /* val map =dataList[p1]
                saveToList(map)*/
            }
        }
    }

    class OdsSubServiceViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val tvSubService:TextView=view.findViewById(R.id.tvSubServiceOds)
        val ivSubServiceChecked:ImageView=view.findViewById(R.id.ivSubServiceChecked)
    }
/*
    private fun saveToList(map:HashMap<String,Any>)
    {

        if (map["selected"]!="0")
        {
                selectedSubServiceList.add(map)

        }
        else{
            if (selectedSubServiceList.isNotEmpty())

                outerloop@
            for ( i in selectedSubServiceList.size-1 downTo   0)
            {
                if (selectedSubServiceList[i]["inner_service_name"]==map["inner_service_name"])
                {
                    if (selectedSubServiceList[i]["name"]==map["name"])
                    {
                        selectedSubServiceList.removeAt(i)
                    }
                }
            }
        }


        println("subServiceList::::$selectedSubServiceList")

        CommonClass(activity,activity).putStringList("subServiceList",selectedSubServiceList)


    }*/
}