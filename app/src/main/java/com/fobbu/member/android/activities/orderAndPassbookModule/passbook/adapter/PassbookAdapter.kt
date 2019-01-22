package com.fobbu.member.android.activities.orderAndPassbookModule.passbook.adapter


import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R


class PassbookAdapter(var activity: Activity): RecyclerView.Adapter<PassbookAdapter.PassbookViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PassbookViewHolder {
        return PassbookViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_passbook_data,p0,false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(p0: PassbookViewHolder, p1: Int) {
    }


    class PassbookViewHolder(view :View) :RecyclerView.ViewHolder(view)
    {
        var tvPayment:TextView= view.findViewById(R.id.tvPaymentReceived)

        var tvTimePassbook:TextView= view.findViewById(R.id.tvPassbookTime)

        var tvPassbookOrderId:TextView= view.findViewById(R.id.tvPassbookOrderId)

        var ivPassbookCheck:ImageView= view.findViewById(R.id.ivPassbookCheck)
    }

}