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
import com.softtech360.totalservey.databinding.RowCheckBinding
import com.softtech360.totalservey.databinding.RowRadioBinding
import com.softtech360.totalservey.fragment.SectionWise
import com.softtech360.totalservey.fragment.SubSectionWise
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.entity.Question
import java.util.ArrayList


class StatusCheckBoxAdapter<T>(val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val sectionwise: T, val parentPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>? = null

    init {
        answerlist = list[parentPosition].answer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        val binding: RowCheckBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_check, parent, false)
        // binding.util= BindDataUtils
        vh = MyViewHolder(binding)


        return vh
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            holder.binding.chkbutton.isChecked = if (answerlist!![position].is_selected) true else false
            holder.binding.titlename.text = answerlist!![position].answer

            holder.binding.rowItem.setOnClickListener {

                when (list[parentPosition].question_id) {

                    62, 63, 64, 65 -> {

                        if (answerlist!![position].answer == "नहीं" && !answerlist!![position].is_selected) {
                            // you are going to check

                            for (list in answerlist!!) {

                                if (list.answer == "नहीं") {
                                    list.is_selected = true
                                } else {
                                    list.is_selected = false

                                }
                            }

                        } else {

                            for (list in answerlist!!) {

                                if (list.answer == "नहीं") {
                                    list.is_selected = false
                                }
                            }

                            if(answerlist!![position].is_selected){
                                // check box already checked.
                                answerlist!![position].is_selected = false
                            }else{
                                answerlist!![position].is_selected = true
                            }


                        }


                    }
                    else -> {

                        if (holder.binding.chkbutton.isChecked) {
                            // please uncheck this
                            answerlist!![position].is_selected = false

                        } else {
                            // please check this
                            answerlist!![position].is_selected = true

                        }

                    }
                }


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


    private class MyViewHolder(val binding: RowCheckBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun getItemCount(): Int {

        return answerlist!!.size
    }


}
