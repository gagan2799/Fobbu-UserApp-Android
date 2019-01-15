package com.fobbu.member.android.activities.paymentModule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class WorkSummaryAdapter(private var serviceMap:ArrayList<HashMap<String,Any>>, var context: Context):
    RecyclerView.Adapter<WorkSummaryAdapter.MyWorkViewHolder>() {

    lateinit var amount: String
    var totalAmount: String=""

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyWorkViewHolder {
        return MyWorkViewHolder(LayoutInflater.from(context).inflate(R.layout.inflate_work_summary,p0,false))
    }


    override fun getItemCount(): Int {
        return serviceMap.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyWorkViewHolder, position: Int) {
        val no_of_service: Long = (serviceMap[position]["number_of_services"] as Double).toLong()
        val service_price: Long = (serviceMap[position]["service_price"] as Double).toLong()
        holder.textViewSubServiceWork.text= serviceMap[position]["service_name"].toString()
        holder.textViewNumberSubServiceWork.text= no_of_service.toString()
        amount= ((no_of_service*service_price).toString())
        holder.textViewNumericAmountWork.text=  amount
    }


    inner class MyWorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSubServiceWork: TextView = itemView.findViewById(R.id.textViewSubServiceWork)
        val textViewNumberSubServiceWork: TextView = itemView.findViewById(R.id.textViewNumberSubServiceWork)
        val textViewAmountWork: TextView = itemView.findViewById(R.id.textViewAmountWork)
        val textViewNumericAmountWork: TextView = itemView.findViewById(R.id.textViewNumericAmountWork)
    }
}