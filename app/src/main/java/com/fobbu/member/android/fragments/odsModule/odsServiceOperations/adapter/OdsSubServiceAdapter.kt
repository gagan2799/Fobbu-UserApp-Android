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
        p0.tvSubService.text = """${p1 + 1}: ${dataList[p1]["option"]}"""

        p0.itemView.setOnClickListener {
            if (dataList[p1]["selected"] == "0")
            {
                dataList[p1]["selected"] = "1"


                        val map =dataList[p1]
                        saveToList(map)



            }
            else
            {
                dataList[p1]["selected"] = "0"


                        val map =dataList[p1]
                        saveToList(map)


            }

            notifyDataSetChanged()


        }
        when (dataList[p1]["selected"]) {
            "0" -> {
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_uncheck)

            }

            else ->
            {
                p0.ivSubServiceChecked.setImageResource(R.drawable.checkbox_checked)
            }
        }
    }

    class OdsSubServiceViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val tvSubService:TextView=view.findViewById(R.id.tvSubServiceOds)
        val ivSubServiceChecked:ImageView=view.findViewById(R.id.ivSubServiceChecked)
    }

    private fun saveToList(map:HashMap<String,Any>)
    {

        if (map["selected"]!="0")
        {
                selectedSubServiceList.add(map)

          /*  else
            {
                outerloop@
                for (i in selectedSubServiceList.indices)
                {
                    if (selectedSubServiceList[i]["parent_pos"]==map["parent_pos"])
                    {
                        if (selectedSubServiceList[i]["option"]!=map["option"])
                        {
                            selectedSubServiceList.add(map)
                            break@outerloop
                        }
                    }
                    else{
                        selectedSubServiceList.add(map)
                        break@outerloop
                    }

                }
            }*/
        }
        else{
            if (selectedSubServiceList.isNotEmpty())

                outerloop@
            for ( i in selectedSubServiceList.size-1 downTo   0)
            {
                if (selectedSubServiceList[i]["service_name"]==map["service_name"])
                {
                    if (selectedSubServiceList[i]["option"]==map["option"])
                    {
                        selectedSubServiceList.removeAt(i)
                    }
                }
            }
        }

/*

       if (CommonClass(activity,activity).getStringList("subServiceList").isEmpty())
        {
            newSelectedList=selectedSubServiceList
        }
        else{
            newSelectedList=CommonClass(activity,activity).getStringList("subServiceList")

            outerLoop@
            for (i in newSelectedList.indices)
            {
                innerLoop@
                for (j in selectedSubServiceList.indices)
                {
                    if (selectedSubServiceList[j]["service_name"]==newSelectedList[i]["service_name"])
                    {
                        if (selectedSubServiceList[j]["option"]==newSelectedList[i]["option"])
                        {
                            newSelectedList.removeAt(i)

                            val selectedMap=selectedSubServiceList[j]

                            newSelectedList.add(selectedMap)

                            break@outerLoop
                        }else{
                            val selectedMap=selectedSubServiceList[j]

                            newSelectedList.add(selectedMap)

                            break@outerLoop
                        }
                    }
                    else
                    {
                        val selectedMap=selectedSubServiceList[j]

                        newSelectedList.add(selectedMap)

                        break@outerLoop
                    }
                }
            }
        }
*/


        println("subServiceList::::$selectedSubServiceList")

        CommonClass(activity,activity).putStringList("subServiceList",selectedSubServiceList)

        selectedSubServiceView.onSuccessReport(selectedSubServiceList)
    }
}