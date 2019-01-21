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


class OrderAdapter(var activity: Activity):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>()
{
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderViewHolder
        {
            return OrderViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_order_all,parent,false))
        }

        override fun getItemCount(): Int
        {
            return  10
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: OrderViewHolder, pos: Int)
        {
          /*  holder.tvOrderID.text= activity.resources.getString(R.string.order_id)+((dataList[pos][FobbuConstants.Constant.orderId] as Double).toLong())

            holder.tvOrderTime.text=BaseActivity().getDesireFormat("yyyy-MM-dd'T'HH:mm:ss.SSS","HH:mm aa dd MMM yyyy",dataList[pos][FobbuConstants.Constant.created].toString())

            holder.tvOrderType.text=dataList[pos][FobbuConstants.Constant.serviceName].toString()

            holder.tvOrderTotal.text=activity.resources.getString(R.string.rs)+((dataList[pos][FobbuConstants.Constant.totalPrice] as Double).toLong())
  */      }


        class OrderViewHolder(view : View): RecyclerView.ViewHolder(view)
        {
            val tvOrderID:TextView=view.findViewById(R.id.tvOrderIdAll)

            val tvOrderTime:TextView=view.findViewById(R.id.tvOrderTimeAll)

            val tvOrderType:TextView=view.findViewById(R.id.tvOrderTypeAll)

            val tvOrderTotal:TextView=view.findViewById(R.id.tvTotalAll)

            val ivOrderChecked:ImageView=view.findViewById(R.id.ivCheckAll)
        }
    }
