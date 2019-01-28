package com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass


class OrderAdapter(var activity: Activity,var dataList:ArrayList<HashMap<String,Any>>):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>()
{
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderViewHolder
        {
            return OrderViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_order_all,parent,false))
        }

        override fun getItemCount(): Int
        {
            return  dataList.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: OrderViewHolder, pos: Int)
        {
            val hashMap= dataList[pos]

            holder.tvOrderID.text= activity.resources.getString(R.string.order_id)+((hashMap["order_id"] as Double).toLong())

            holder.tvOrderTime.text= CommonClass(activity,activity).getDesireFormat("yyyy-MM-dd'T'HH:mm:ss.SSS","HH:mm aa dd MMM yyyy",hashMap["created"].toString())

            holder.tvOrderType.text=hashMap["service_type"].toString()

            holder.tvOrderTotal.text=activity.resources.getString(R.string.rs)+((hashMap["total_price"] as Double).toLong())
        }


        class OrderViewHolder(view : View): RecyclerView.ViewHolder(view)
        {
            val tvOrderID:TextView=view.findViewById(R.id.tvOrderIdAll)

            val tvOrderTime:TextView=view.findViewById(R.id.tvOrderTimeAll)

            val tvOrderType:TextView=view.findViewById(R.id.tvOrderTypeAll)

            val tvOrderTotal:TextView=view.findViewById(R.id.tvTotalAll)

            val ivOrderChecked:ImageView=view.findViewById(R.id.ivCheckAll)
        }
    }
