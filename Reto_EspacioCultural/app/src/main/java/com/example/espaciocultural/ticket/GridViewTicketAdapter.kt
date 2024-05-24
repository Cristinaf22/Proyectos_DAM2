package com.example.espaciocultural.ticket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.espaciocultural.R

class GridViewTicketAdapter(context: Context, val layout: Int, private val numberedTickets: MutableList<GridViewTicket>) :
    ArrayAdapter<GridViewTicket>(context, layout, numberedTickets)
{
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
        {
            val view: View

            if (convertView != null)
            {
                view = convertView
            }
            else
            {
                view=LayoutInflater.from(context).inflate(layout,parent,false)
            }

            bindNumberedTicket(view, numberedTickets[position])

            return view
        }

    private fun bindNumberedTicket(view: View, gridViewTicket: GridViewTicket)
    {
        val numberedTickets = view.findViewById<TextView>(R.id.NumberedSeats)
        numberedTickets.text = gridViewTicket.startNumber.toString()

        val imgNumberedTicket = view.findViewById<ImageView>(R.id.ImageSeatEvent)
        imgNumberedTicket.setImageResource(gridViewTicket.image)
    }
}