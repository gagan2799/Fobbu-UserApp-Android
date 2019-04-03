package com.fobbu.member.android.activities.emergencyContactsModule.contactList.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.EmergencyContactsActivity
import com.fobbu.member.android.interfaces.DeleteVehicleClickListener

class ContactsAdapter(var activity: Activity,var dataList:ArrayList<HashMap<String,Any>>):
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>()
{
    var sortedList=ArrayList<HashMap<String,Any>>()

    var clickListener: DeleteVehicleClickListener =activity!! as DeleteVehicleClickListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ContactsViewHolder
    {
        return  ContactsViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_contactlist,p0,false))
    }

    override fun getItemCount(): Int
    {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int)
    {
        holder.tvName!!.text=dataList[position]["name"].toString()

        holder.tvRelationShip!!.text=dataList[position]["mobile_number"].toString()

        holder.tvNumber!!.text=dataList[position]["relationship"].toString()

        holder.llcontainer!!.setOnClickListener {
            for (i in dataList.indices)
            {
                if (dataList[position]["mobile_number"]== dataList[i]["mobile_number"])
                {
                    val map=dataList[i]

                    sortedList.add(map)
                }
            }

            activity.startActivity(Intent(activity, EmergencyContactsActivity::class.java).putExtra("sorted_list",sortedList).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

            activity.finish()
        }

        holder.ivDELETE!!.setOnClickListener{
               clickListener.onViewClick(dataList[position]["_id"].toString())
        }
    }

    // inner view holder class
    class ContactsViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var tvName = view.findViewById(R.id.tvVehicleName) as TextView?

        var tvRelationShip = view.findViewById(R.id.tvYear) as TextView?

        var llcontainer = view.findViewById(R.id.llContainer) as LinearLayout?

        var tvNumber = view.findViewById(R.id.tvVehicleNumber) as TextView?

        var ivDELETE = view.findViewById(R.id.ivDelete) as ImageView?

        var ivEdit = view.findViewById(R.id.ivEdit) as ImageView?

    }

}