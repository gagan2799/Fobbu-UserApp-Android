package com.fobbu.member.android.activities.paymentModule.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class OdsWorkAdapter(var activity: Activity,var dataList:ArrayList<HashMap<String,Any>>):
    RecyclerView.Adapter<OdsWorkAdapter.MyWorkViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyWorkViewHolder {
        return MyWorkViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_work_summary,p0,false))
    }

    override fun getItemCount(): Int
    {
    return dataList.size
    }

    override fun onBindViewHolder(p0: MyWorkViewHolder, p1: Int)
    {
        p0.textViewAmountWork.visibility=View.INVISIBLE

        p0.textViewNumericAmountWork.visibility=View.INVISIBLE

        p0.textViewSubServiceWork.text=dataList[p1]["inner_service_name"].toString()

        p0.textViewNumberSubServiceWork.text=dataList[p1]["name"].toString()
    }

    inner class MyWorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewSubServiceWork: TextView = itemView.findViewById(R.id.textViewSubServiceWork)
        val textViewNumberSubServiceWork: TextView = itemView.findViewById(R.id.textViewNumberSubServiceWork)
        val textViewAmountWork: TextView = itemView.findViewById(R.id.textViewAmountWork)
        val textViewNumericAmountWork: TextView = itemView.findViewById(R.id.textViewNumericAmountWork)
    }

}