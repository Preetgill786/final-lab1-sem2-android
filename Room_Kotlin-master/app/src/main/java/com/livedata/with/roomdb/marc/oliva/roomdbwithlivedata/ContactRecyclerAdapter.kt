package com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata

import android.arch.persistence.room.TypeConverter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.livedata.with.roomdb.marc.oliva.roomdbwithlivedata.data.Contact
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by ThinkSoft on 20/12/2017.
 */
class ContactRecyclerAdapter(contacts: ArrayList<Contact>, listener: OnItemClickListener) : RecyclerView.Adapter<ContactRecyclerAdapter.RecyclerViewHolder>() {

    private var listContacts: List<Contact> = contacts

    private var listenerContact: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
        var currentContact: Contact = listContacts[position]

        var nameContact = currentContact.name
        var age = currentContact.age
        var tution = currentContact.tution
        var date = toDate(currentContact.date)

        holder!!.mName.text = nameContact
        holder!!.mNumber.text = age.toString()
        holder!!.mTution.text = tution.toString()

        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        holder!!.mDate.text = sdf.format(date).toString()

        holder.bind(currentContact, listenerContact)

    }

    fun addContacts(listContacts: List<Contact>) {
        this.listContacts = listContacts
        notifyDataSetChanged()
    }


    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return  Date(dateLong)
    }
    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mName = itemView.findViewById<TextView>(R.id.Sname)!!
        var mNumber = itemView.findViewById<TextView>(R.id.Sage)!!
        var mTution = itemView.findViewById<TextView>(R.id.Sfees)!!
        var mDate = itemView.findViewById<TextView>(R.id.Sdate)!!

        fun bind(contact: Contact, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(contact)
            }
        }

    }
}