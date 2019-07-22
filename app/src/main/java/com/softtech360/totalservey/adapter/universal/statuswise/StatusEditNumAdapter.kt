package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.databinding.RowEditnumBinding
import com.softtech360.totalservey.fragment.SectionWise
import java.util.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.softtech360.totalservey.fragment.SubSectionWise
import kotlinx.android.synthetic.main.sectionwise.*
import kotlinx.android.synthetic.main.subsectionwise.*


class StatusEditNumAdapter <T> (val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val sectionwise: T, val parentPosition: Int,val user_type : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>? = null

    init {
        answerlist = list[parentPosition].answer
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        val binding: RowEditnumBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_editnum, parent, false)
        // binding.util= BindDataUtils
        vh = MyViewHolder(binding,MyCustomEditTextListener(answerlist,sectionwise),MyCustomFocuslistner(list[parentPosition].question_id,sectionwise))


        return vh
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            holder.binding.edttxt.setTag(position)


            holder.myCustomEditTextListener.updatePosition(position,list[parentPosition].question_id,user_type)
         //   holder.binding.edttxt.isFocusableInTouchMode = true
            //holder.binding.edttxt.isFocusable = false
         //   holder.binding.edttxt.clearFocus()
            holder.binding.edttxt.setText(answerlist!![position].is_values)


            holder.binding.edttxt.setOnClickListener(View.OnClickListener {

                var layoutManager : RecyclerView.LayoutManager? = null
                var currentActivity : FragmentActivity? = null

                when (sectionwise) {
                    is SectionWise -> {
                       layoutManager = sectionwise.recyclerview_section.getLayoutManager() as LinearLayoutManager
                        currentActivity = (sectionwise as SectionWise).activity!!
                    }
                    is SubSectionWise -> {
                        layoutManager = sectionwise.recyclerview_subsection.getLayoutManager() as LinearLayoutManager
                        currentActivity = (sectionwise as SubSectionWise).activity!!

                    }
                }

                // you may want to play with the offset parameter
              //  layoutManager!!.scrollToPosition(position)
                holder.binding.edttxt.setFocusableInTouchMode(true)
                holder.binding.edttxt.post({
                    holder.binding.edttxt.requestFocus()
                    //Show soft-keyboard:
                     currentActivity!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                    //hide keyboard :
                    //currentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                })
            })


        }
    }


    private class MyViewHolder(val binding: RowEditnumBinding ,val myCustomEditTextListener: MyCustomEditTextListener<*>,val mycustomfocuslistner: MyCustomFocuslistner<*>) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.edttxt.addTextChangedListener(myCustomEditTextListener)
            binding.edttxt.setOnFocusChangeListener(mycustomfocuslistner)


        }
    }


    override fun getItemCount(): Int {

        return answerlist!!.size
    }


    private  class MyCustomEditTextListener <T> (val answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>?, val sectionwise: T) : TextWatcher {

        private var position: Int = 0
        private var question_id: Int = 0
        private var user_type: Int = 0

        fun updatePosition(position: Int, question_id: Int,user_type : Int) {
            this.position = position
            this.question_id = question_id
            this.user_type = user_type
        }


        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

            answerlist!![position].is_values=charSequence.toString()


            if(question_id == 16){
                HostActivity.statusofAge.put(user_type,if(answerlist[position].is_values.trim().length > 0)answerlist[position].is_values.trim().toInt() else 0)
            }

        }

        override fun afterTextChanged(editable: Editable) {
            // no op

            }
        }


    private class MyCustomFocuslistner <T> (val question_id : Int,val sectionwise: T) : View.OnFocusChangeListener{

        override fun onFocusChange(v: View?, hasFocus: Boolean) {


            var currentActivity : FragmentActivity? = null


            when (sectionwise) {
                is SectionWise -> {
                    currentActivity = (sectionwise as SectionWise).activity!!
                }
                is SubSectionWise -> {

                    currentActivity = (sectionwise as SubSectionWise).activity!!

                }
            }



            try {

                if (!hasFocus) {
                    val position = v!!.getTag() as Int
                    val edit = v as AppCompatEditText
                    edit.isFocusableInTouchMode =false
                    edit.post {
                        //hide keyboard :
                        currentActivity!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }

                } else {

                    // val edit = v as AppCompatEditText
                    // edit.isCursorVisible = true
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }






        }



    }



    }

