package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.fragments.odsModule.odsServiceOperations.view.SelectedSubServiceView
import com.fobbu.member.android.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.inflate_ods_operation.view.*
import java.lang.Exception

class OdsOperationAdapter(var context:Activity, var dataList: ArrayList<HashMap<String, Any>>):RecyclerView.Adapter<OdsOperationAdapter.OperationViewHolder>(),SelectedSubServiceView
{
    var optionList=ArrayList<HashMap<String,Any>>()

    var mainList=ArrayList<HashMap<String,Any>>()

    var list=ArrayList<String>()

    val tempList=ArrayList<HashMap<String,Any>>()

    var newSelectedList=ArrayList<HashMap<String,Any>>()

    private lateinit var odsSubServiceAdapter:OdsSubServiceAdapter

    private var viewpool:RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OperationViewHolder
    {
        val view=LayoutInflater.from(context).inflate(R.layout.inflate_ods_operation,p0,false)

        viewpool=   view.rvOdsSubService.recycledViewPool

        return OperationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: OperationViewHolder, p1: Int)
    {
        p0.tvTopUp.text=dataList[p1]["service_name"].toString()

        p0.itemView.setOnClickListener {
            if (p0.rlOdsSubService.visibility==View.GONE)
            {
                optionList= dataList[p1]["option"] as ArrayList<HashMap<String, Any>>

                setUpInnerRecycler(p0)
            }

            else{
                p0.rlOdsSubService.visibility=View.GONE
            }
        }

        p0.rvOdsSubSrevice.addOnItemTouchListener(RecyclerItemClickListener(context,object :RecyclerItemClickListener.OnItemClickListener
        {
            override fun onItemClick(view: View, position: Int)
            {
              /*  if (optionList[position]["selected"]=="0")
                {
                    optionList[position]["selected"]="1"
                }

                else
                {
                    optionList[position]["selected"]="0"
                }*/

                for (i in dataList.indices)
                {
                    if (dataList[i]["service_name"]==optionList[position]["inner_service_name"])
                    {
                        val list=dataList[i]["option"] as ArrayList<HashMap<String,Any>>

                        for ( k in list.indices)
                        {
                            if (list[k]["name"]==optionList[position]["name"])
                            {
                                if (list[k]["selected"]=="0")
                                list[k]["selected"]="1"

                                else
                                    list[k]["selected"]="0"
                            }

                        }
                    }
                }

                println("new main list:::: $dataList")

                setUpInnerRecycler(p0)
            }

        }))

        /*p0.ivPlusListing.setOnClickListener {
            if (p0.rlOdsSubService.visibility==View.GONE)
            {
                var list= ArrayList<Any>()

                try
                {
                    list.clear()

                    optionList.clear()



                    list= dataList[p1]["option"] as ArrayList<Any>

                    loop@
                    for (i in list.indices)
                    {
                        val subServicemap = HashMap<String, Any>()

                        subServicemap["option"] = list[i].toString()

                        if (newSelectedList.isNotEmpty())
                            {
                                innerloop@
                                for (j in newSelectedList.indices)
                                {
                                    if (newSelectedList[j]["service_name"]==dataList[p1]["service_name"])
                                    {
                                        if (newSelectedList[j]["option"]==list[i])
                                        {
                                            if (subServicemap["selected"]!="1")
                                            subServicemap["selected"]=newSelectedList[j]["selected"].toString()
                                        }
                                        else
                                        {
                                            if (subServicemap["selected"]!="1")
                                            subServicemap["selected"]="0"
                                        }

                                    }
                                    else
                                    {
                                        if (subServicemap["selected"]!="1")
                                        subServicemap["selected"]="0"
                                    }

                                }
                            }
                        else{
                            subServicemap["selected"] = "0"
                        }


                            subServicemap["service_name"] = dataList[p1]["service_name"].toString()


                            optionList.add(subServicemap)
                        }



                }
                catch (e:Exception)
                {
                    print("error:::${e.message}")
                }


                odsSubServiceAdapter=OdsSubServiceAdapter(context,optionList,this)

                p0.rvOdsSubSrevice.layoutManager=LinearLayoutManager(context)

                p0.rvOdsSubSrevice.adapter=odsSubServiceAdapter

                p0.rvOdsSubSrevice.setRecycledViewPool(viewpool)

                p0.rvOdsSubSrevice.setHasFixedSize(true)

                p0.rvOdsSubSrevice.isNestedScrollingEnabled=false

                p0.rlOdsSubService.visibility=View.VISIBLE
            }

            else
                p0.rlOdsSubService.visibility=View.GONE
        }*/
    }


    class OperationViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        var tvTopUp:TextView=view.findViewById(R.id.tvTopUpOperations)

        var ivPlusListing:ImageView=view.findViewById(R.id.ivOperationAdd)

        var rlOdsSubService:RelativeLayout=view.findViewById(R.id.rlOdsSubService)

        var rvOdsSubSrevice:RecyclerView=view.findViewById(R.id.rvOdsSubService)
    }

    override fun onSuccessReport(selectedSubSercice: ArrayList<HashMap<String,Any>>)
    {
        newSelectedList=selectedSubSercice
        println("success list::::: ${newSelectedList}")
    }

    private fun setUpInnerRecycler(p0: OperationViewHolder)
    {
        p0.rlOdsSubService.visibility=View.VISIBLE

        odsSubServiceAdapter=OdsSubServiceAdapter(context,optionList,this)

        p0.rvOdsSubSrevice.layoutManager=LinearLayoutManager(context)

        p0.rvOdsSubSrevice.adapter=odsSubServiceAdapter

        p0.rvOdsSubSrevice.setRecycledViewPool(viewpool)

        p0.rvOdsSubSrevice.setHasFixedSize(true)

        p0.rvOdsSubSrevice.isNestedScrollingEnabled=false

        p0.rlOdsSubService.visibility=View.VISIBLE
    }

}