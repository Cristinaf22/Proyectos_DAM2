package com.example.espaciocultural.events

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.espaciocultural.R
import java.time.format.DateTimeFormatter

class GridViewEventAdapter(private val context: Context,
                           private val gridEvents: MutableList<GridViewEvent>) :
    RecyclerView.Adapter<GridViewEventAdapter.GridEventsViewHolder>(),
    View.OnClickListener, View.OnLongClickListener
{
    private val layout = R.layout.activity_gridview_event
    private var clickListener: View.OnClickListener? = null
    private var onLongClickListener: View.OnLongClickListener? = null
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    class GridEventsViewHolder(val view: View):
        RecyclerView.ViewHolder(view)
    {
        var imageEvent: ImageView
        var lblEventName: TextView
        var lblEventDate: TextView
        var lblEventPrice: TextView

        init
        {
            imageEvent = view.findViewById(R.id.ImageListEvent)
            lblEventName = view.findViewById(R.id.LblListEventName)
            lblEventDate = view.findViewById(R.id.LblListEventDate)
            lblEventPrice = view.findViewById(R.id.LblListEventPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridEventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return GridEventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridEventsViewHolder, position: Int)
    {
        val event = gridEvents[position]
        bindEvent(holder, event)
    }
    override fun getItemCount() = gridEvents.size

    private fun bindEvent(holder: GridEventsViewHolder, event: GridViewEvent)
    {
        val eventPath = context.filesDir.toString() + "/img/" + event.image
        val bitmap = BitmapFactory.decodeFile(eventPath)
        holder.imageEvent?.setImageBitmap(bitmap)
        holder.lblEventName?.text = event.name
        holder.lblEventDate?.text = event.date.format(formatter)
        holder.lblEventPrice?.text = event.price.toString()
    }

    override fun onClick(view: View?)
    {
        clickListener?.onClick(view)
    }

    fun setOnClickListener(listener: View.OnClickListener) {

        clickListener = listener

    }
    override fun onLongClick(v: View?): Boolean {

        onLongClickListener?.onLongClick(v)

        return true
    }

    fun setOnLongClickListener(v: View.OnLongClickListener): Boolean{

        onLongClickListener = v

        return true
    }

}
