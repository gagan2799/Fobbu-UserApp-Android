package com.fobbu.member.android.activities.orderAndPassbookModule.passbookDetail.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fobbu.member.android.R

class PaymentDetailAdapter(var activity:Activity):
    RecyclerView.Adapter<PaymentDetailAdapter.PaymentDetailViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PaymentDetailViewHolder
    {
        return PaymentDetailViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_work_summary,p0,false))
    }

    override fun getItemCount(): Int
    {
        return 10
    }

    override fun onBindViewHolder(p0: PaymentDetailViewHolder, p1: Int) {}

    // inner view holder class
    class PaymentDetailViewHolder(view:View) :RecyclerView.ViewHolder(view)
    {
        val textViewSubServiceWork: TextView = itemView.findViewById(R.id.textViewSubServiceWork)

        val textViewNumberSubServiceWork: TextView = itemView.findViewById(R.id.textViewNumberSubServiceWork)

        val textViewAmountWork: TextView = itemView.findViewById(R.id.textViewAmountWork)

        val textViewNumericAmountWork: TextView = itemView.findViewById(R.id.textViewNumericAmountWork)
    }
}