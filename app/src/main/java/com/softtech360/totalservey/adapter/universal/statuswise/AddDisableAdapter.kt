package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.RowAddDisableuserBinding
import com.softtech360.totalservey.databinding.RowEdittextBinding
import com.softtech360.totalservey.fragment.AddDisableUser
import com.softtech360.totalservey.fragment.SectionWise
import com.softtech360.totalservey.fragment.SubSectionWise
import java.util.ArrayList
import kotlinx.android.synthetic.main.subsectionwise.*


class AddDisableAdapter (val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val adddisableuser : AddDisableUser) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        val binding: RowAddDisableuserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_add_disableuser, parent, false)
        // binding.util= BindDataUtils
        vh = MyViewHolder(binding,MyCustomEditTextListener(list))


        return vh
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            holder.myCustomEditTextListener.updatePosition(position)
            holder.binding.edttxt.setText(list[position].answer[0].is_values)
            holder.binding.title.setText("${(position+1)} ${list[position].question}")
            holder.binding.edttxt.isFocusableInTouchMode = true
            //holder.binding.edttxt.isFocusable = false
            holder.binding.edttxt.clearFocus()

        }
    }


    private class MyViewHolder  (val binding: RowAddDisableuserBinding,val myCustomEditTextListener: MyCustomEditTextListener ) : RecyclerView.ViewHolder(binding.root) {
        init {

            binding.edttxt.addTextChangedListener(myCustomEditTextListener)



        }
    }


    override fun getItemCount(): Int {

        return list.size
    }


    private  class  MyCustomEditTextListener  (val list: ArrayList<SectionWise.SectionWiseModel>) : TextWatcher {

        private var position: Int = 0

        fun updatePosition(position: Int) {
            this.position = position
        }


        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            // no op
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

            list[position].answer[0].is_values=charSequence.toString()


        }

        override fun afterTextChanged(editable: Editable) {
            // no op

        }
    }




}
