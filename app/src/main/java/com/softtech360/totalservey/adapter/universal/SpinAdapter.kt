package com.softtech360.totalservey.adapter.universal

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.core.content.ContextCompat
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.Login


class SpinAdapter(
         context: Context, textViewResourceId: Int, val values: ArrayList<Login.UserStates>) : ArrayAdapter<Login.UserStates>(context, textViewResourceId, values) {


    override fun isEnabled(position: Int): Boolean {
        if(position == 0)
            return false
        else
            return true
    }

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): Login.UserStates? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val label = super.getView(position, convertView, parent) as TextView

        label.setTextColor(if(position == 0) ContextCompat.getColor(context,R.color.black_transparent) else ContextCompat.getColor(context,R.color.black_light))
        label.setText(values[position].name)

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View? {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(if(position == 0) ContextCompat.getColor(context,R.color.black_transparent) else ContextCompat.getColor(context,R.color.black_light))
        label.setText(values[position].name)

        return label
    }

}