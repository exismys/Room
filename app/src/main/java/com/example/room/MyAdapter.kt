package com.example.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyAdapter(var mCtx: Context, var resources: Int, var list: List<Contact>): ArrayAdapter<Contact>(mCtx, resources, list)  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mCtx)
        val view = layoutInflater.inflate(resources, parent, false)

        val id = view.findViewById<TextView>(R.id.tv_id)
        val name = view.findViewById<TextView>(R.id.tv_name)
        val phone = view.findViewById<TextView>(R.id.tv_phone)

        val contact = list[position]

        id.text = contact.id.toString()
        name.text = contact.name
        phone.text = contact.phone.toString()

        return view
    }
}