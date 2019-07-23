package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.databinding.RowCheckBinding
import com.softtech360.totalservey.databinding.RowEditnumBinding
import com.softtech360.totalservey.databinding.RowOptionalBinding
import com.softtech360.totalservey.databinding.RowRadioBinding
import com.softtech360.totalservey.fragment.SectionWise
import com.softtech360.totalservey.fragment.SubSectionWise
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.entity.Question
import java.util.ArrayList


class StatusOptionalAdapter <T> (val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val sectionwise: T, val parentPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>? = null

    init {
        answerlist = list[parentPosition].answer
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        val binding: RowOptionalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_optional, parent, false)
        // binding.util= BindDataUtils
        vh = MyViewHolder(binding)


        return vh
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            if(list[parentPosition].question_id == 31){
                //की शिक्षा की स्
                if(answerlist!![0].is_selected){
                    HostActivity.any_toilet = true
                }else{
                    HostActivity.any_toilet = false
                }
            }

            holder.binding.radiobutton.isChecked = if(answerlist!![position].is_selected) true else false

            holder.binding.titlename.text=answerlist!![position].answer

            holder.binding.rowItem.setOnClickListener{

                for (i in 0.until(answerlist!!.size)){
                    answerlist!![i].is_selected=false
                }

                answerlist!![position].is_selected=true


                when (sectionwise) {
                    is SectionWise -> {
                        sectionwise.mAdapter.notifyDataSetChanged()

                    }
                    is SubSectionWise -> {

                        sectionwise.mAdapter.notifyDataSetChanged()

                    }
                }
            }


        }
    }


    private class MyViewHolder(val binding: RowOptionalBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun getItemCount(): Int {

        return answerlist!!.size
    }


}