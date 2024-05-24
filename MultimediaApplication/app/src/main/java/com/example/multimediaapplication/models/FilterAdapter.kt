package com.example.multimediaapplication.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.multimediaapplication.R

class FilterAdapter(context: Context, val layout: Int, val filters: MutableList<Filter>) :
    ArrayAdapter<Filter>(context, layout, filters)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var view: View

        if (convertView != null)
        {
            view = convertView
        }
        else
        {
            view=
                LayoutInflater.from(context).inflate(layout,parent,false)
        }

        bindFilter(view, filters[position])


        return view
    }

    private fun bindFilter(view: View, filter: Filter) {

        val imgFilter = view.findViewById<ImageView>(R.id.ImgListFilter)
        imgFilter.setImageResource(filter.image)

        val lblFilterName = view.findViewById<TextView>(R.id.LblListFilterName)
        lblFilterName.text = filter.name

    }
}