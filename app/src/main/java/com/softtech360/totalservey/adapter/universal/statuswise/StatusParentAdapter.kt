package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.databinding.RowStatuswisePBinding
import com.softtech360.totalservey.fragment.SectionWise
import com.softtech360.totalservey.fragment.SubSectionWise
import kotlinx.android.synthetic.main.subsectionwise.*
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread


class StatusParentAdapter<T>(val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val sectionwise: T?,val user_type : Int,val connectedHeader : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_EDTTEXT = 1
    private val TYPE_EDITNUM = 2
    private val TYPE_RADIO = 3
    private val TYPE_CHECK = 4
    private val TYPE_OPTIONAL = 5


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        if (viewType == TYPE_EDTTEXT) {
            val binding: RowStatuswisePBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_statuswise_p, parent, false)
            vh = EditTextViewHolder(binding)
            return vh
        } else if (viewType == TYPE_EDITNUM) {
            val binding: RowStatuswisePBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_statuswise_p, parent, false)
            vh = EditNumViewHolder(binding)
            return vh
        } else if (viewType == TYPE_RADIO) {
            val binding: RowStatuswisePBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_statuswise_p, parent, false)
            vh = RadioViewHolder(binding)
            return vh
        } else if (viewType == TYPE_CHECK) {
            val binding: RowStatuswisePBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_statuswise_p, parent, false)
            vh = CheckBoxViewHolder(binding)
            return vh
        } else if (viewType == TYPE_OPTIONAL) {
            val binding: RowStatuswisePBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_statuswise_p, parent, false)
            vh = OptionalViewHolder(binding)
            return vh
        }

        return vh!!
    }

    override fun getItemViewType(position: Int): Int {

        if (list[position].question_type == 1)
            return TYPE_EDTTEXT
        else if (list[position].question_type == 2)
            return TYPE_EDITNUM
        else if (list[position].question_type == 3)
            return TYPE_RADIO
        else if (list[position].question_type == 4)
            return TYPE_CHECK
        else (list[position].question_type == 5)
        return TYPE_OPTIONAL
    }





    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //ArrayList<Answers>





        if (holder is EditTextViewHolder) {

           // Log.e("section id--"," "+list[position].section_id )
            val builder = SpannableStringBuilder()
            var span1 = ""
            when(list[position].section_id){
                2,3,4,5,6 ->{

                    if(list[position].is_pwd == 1)
                    span1 = SpannableString(if(list[position].question_id != 6) connectedHeader+" " else "").toString()
                }
            }

            val span2 = SpannableString(list[position].question)
            builder.append(span1).append(span2)

            holder.binding.questionContainer.visibility = if(list[position].view_hide_is || list[position].question_id == 6) View.GONE else View.VISIBLE


            holder.binding.questionName.text = builder
            holder.binding.errorImg.visibility = if(list[position].is_any_error) View.VISIBLE else View.GONE

            val mAdapter = StatusEditTextAdapter(c, list, sectionwise, position)
            holder.binding.recyclerViewParent.layoutManager = LinearLayoutManager(c)
            (holder.binding.recyclerViewParent.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)

            holder.binding.recyclerViewParent.adapter = mAdapter


            ConditionalQuestion.conditionalIs(list[position],holder.binding.questionContainer,list,c)




        } else if (holder is EditNumViewHolder) {

            val builder = SpannableStringBuilder()
            var span1 = ""
            when(list[position].section_id){
                2,3,4,5,6 ->{

                    if(list[position].is_pwd == 1)
                    span1 = SpannableString(if(list[position].question_id != 6) connectedHeader+" " else "").toString()
                }
            }
            val span2 = SpannableString(list[position].question)
            builder.append(span1).append(span2)

            holder.binding.questionContainer.visibility = if(list[position].view_hide_is) View.GONE else View.VISIBLE


            holder.binding.questionName.text = builder
            holder.binding.errorImg.visibility = if(list[position].is_any_error) View.VISIBLE else View.GONE

            val mAdapter = StatusEditNumAdapter(c, list, sectionwise, position,user_type)
            holder.binding.recyclerViewParent.layoutManager = LinearLayoutManager(c)
            (holder.binding.recyclerViewParent.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)

            holder.binding.recyclerViewParent.adapter = mAdapter

            ConditionalQuestion.conditionalIs(list[position],holder.binding.questionContainer,list,c)


        } else if (holder is RadioViewHolder) {

            val builder = SpannableStringBuilder()
            var span1 = ""
            when(list[position].section_id){
                2,3,4,5,6 ->{

                    if(list[position].is_pwd == 1)
                    span1 = SpannableString(if(list[position].question_id != 6) connectedHeader+" " else "").toString()
                }
            }
            val span2 = SpannableString(list[position].question)
            builder.append(span1).append(span2)

            holder.binding.questionContainer.visibility = if(list[position].view_hide_is) View.GONE else View.VISIBLE

            holder.binding.questionName.text = builder
            holder.binding.errorImg.visibility = if(list[position].is_any_error) View.VISIBLE else View.GONE

            val mAdapter = StatusRadioAdapter(c, list, sectionwise, position)
            holder.binding.recyclerViewParent.layoutManager = LinearLayoutManager(c)
            (holder.binding.recyclerViewParent.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)

            holder.binding.recyclerViewParent.adapter = mAdapter

            ConditionalQuestion.conditionalIs(list[position],holder.binding.questionContainer,list,c)




        } else if (holder is CheckBoxViewHolder) {

            val builder = SpannableStringBuilder()
            var span1 = ""
            when(list[position].section_id){
                2,3,4,5,6 ->{

                    if(list[position].is_pwd == 1)
                    span1 = SpannableString(if(list[position].question_id != 6) connectedHeader+" " else "").toString()
                }
            }
            val span2 = SpannableString(list[position].question)
            builder.append(span1).append(span2)

            holder.binding.questionContainer.visibility = if(list[position].view_hide_is) View.GONE else View.VISIBLE


            holder.binding.questionName.text = builder
            holder.binding.errorImg.visibility = if(list[position].is_any_error) View.VISIBLE else View.GONE

            val mAdapter = StatusCheckBoxAdapter(c, list, sectionwise, position)
            holder.binding.recyclerViewParent.layoutManager = LinearLayoutManager(c)
            (holder.binding.recyclerViewParent.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)

            holder.binding.recyclerViewParent.adapter = mAdapter

            ConditionalQuestion.conditionalIs(list[position],holder.binding.questionContainer,list,c)


        } else if (holder is OptionalViewHolder) {

            val builder = SpannableStringBuilder()
            var span1 = ""
            when(list[position].section_id){
                2,3,4,5,6 ->{

                    if(list[position].is_pwd == 1)
                    span1 = SpannableString(if(list[position].question_id != 6) connectedHeader+" " else "").toString()
                }
            }
            val span2 = SpannableString(list[position].question)
            builder.append(span1).append(span2)

            holder.binding.questionContainer.visibility = if(list[position].view_hide_is) View.GONE else View.VISIBLE


            holder.binding.questionName.text = builder
            holder.binding.errorImg.visibility = if(list[position].is_any_error) View.VISIBLE else View.GONE

            val mAdapter = StatusOptionalAdapter(c, list, sectionwise, position)
            holder.binding.recyclerViewParent.layoutManager = LinearLayoutManager(c)
            (holder.binding.recyclerViewParent.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)

            holder.binding.recyclerViewParent.adapter = mAdapter

            ConditionalQuestion.conditionalIs(list[position],holder.binding.questionContainer,list,c)


        }

    }


    private class EditTextViewHolder(val binding: RowStatuswisePBinding) : RecyclerView.ViewHolder(binding.root)
    private class EditNumViewHolder(val binding: RowStatuswisePBinding) : RecyclerView.ViewHolder(binding.root)
    private class RadioViewHolder(val binding: RowStatuswisePBinding) : RecyclerView.ViewHolder(binding.root)
    private class CheckBoxViewHolder(val binding: RowStatuswisePBinding) : RecyclerView.ViewHolder(binding.root)
    private class OptionalViewHolder(val binding: RowStatuswisePBinding) : RecyclerView.ViewHolder(binding.root)


    override fun getItemCount(): Int {
        return list.size
    }



}