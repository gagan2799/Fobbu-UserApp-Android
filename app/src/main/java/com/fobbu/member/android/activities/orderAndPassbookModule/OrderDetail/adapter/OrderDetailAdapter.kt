package com.fobbu.member.android.activities.orderAndPassbookModule.OrderDetail.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class OrderDetailAdapter(var activity: Activity, var dataList:ArrayList<HashMap<String,Any>>): RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderDetailViewHolder {
        return OrderDetailViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.inflate_work_summary,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, pos: Int)
    {
        val amount= ((dataList[pos]["number_of_services"] as Double).toLong()*(dataList[pos
        ]["service_price"] as Double).toLong()).toString()

        holder.textViewNumericAmountWork.text= amount

        holder.textViewSubServiceWork.text= dataList[pos]["service_name"].toString()

        holder.textViewNumberSubServiceWork.text="""${(dataList[pos]["number_of_services"] as Double).toLong()}"""
    }


    class OrderDetailViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val textViewSubServiceWork: TextView = itemView.findViewById(R.id.textViewSubServiceWork)

        val textViewNumberSubServiceWork: TextView = itemView.findViewById(R.id.textViewNumberSubServiceWork)

        val textViewAmountWork: TextView = itemView.findViewById(R.id.textViewAmountWork)

        val textViewNumericAmountWork: TextView = itemView.findViewById(R.id.textViewNumericAmountWork)

    }
}