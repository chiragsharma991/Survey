package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.RowEdittextBinding
import com.softtech360.totalservey.fragment.SectionWise
import com.softtech360.totalservey.fragment.SubSectionWise
import java.util.ArrayList
import kotlinx.android.synthetic.main.subsectionwise.*
import android.widget.EditText
import kotlinx.android.synthetic.main.sectionwise.*


class StatusEditTextAdapter <T> (val c: Context, val list: ArrayList<SectionWise.SectionWiseModel>, val sectionwise: T?, val parentPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>? = null

    init {
        answerlist = list[parentPosition].answer
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        val binding: RowEdittextBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_edittext, parent, false)
        // binding.util= BindDataUtils
        vh = MyViewHolder(binding,MyCustomEditTextListener(answerlist,sectionwise),MyCustomFocuslistner(list[parentPosition].question_id,sectionwise))

        return vh
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            holder.binding.edttxt.setTag(position)

            holder.myCustomEditTextListener.updatePosition(position)
            holder.binding.edttxt.setText(answerlist!![position].is_values)
           // holder.binding.edttxt.isFocusableInTouchMode = true
            //holder.binding.edttxt.isFocusable = false
           // holder.binding.edttxt.clearFocus()
            holder.binding.edttxt.isEnabled = if(list[parentPosition].question_id == 6) false else true


            holder.binding.edttxt.setFocusableInTouchMode(false)



            holder.binding.edttxt.setOnClickListener(View.OnClickListener {

                /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       holder.binding.edttxt.showSoftInputOnFocus = true
                   }*/

                holder.binding.edttxt.setFocusableInTouchMode(true)

                holder.binding.edttxt.requestFocus()


                val inputMethodManager = c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(holder.binding.edttxt, InputMethodManager.SHOW_IMPLICIT)

            })


            Handler().postDelayed({

                val inputMethodManager_ = c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager_.hideSoftInputFromWindow(holder.binding.edttxt.windowToken,0)

            },500)



        }
    }


    private class MyViewHolder  (val binding: RowEdittextBinding,val myCustomEditTextListener: MyCustomEditTextListener<*> , val mycustomfocuslistner: MyCustomFocuslistner<*>) : RecyclerView.ViewHolder(binding.root) {
        init {

            binding.edttxt.addTextChangedListener(myCustomEditTextListener)
            binding.edttxt.setOnFocusChangeListener(mycustomfocuslistner)

        }
    }


    override fun getItemCount(): Int {

        return answerlist!!.size
    }


    private  class  MyCustomEditTextListener <T> (val answerlist: ArrayList<SectionWise.SectionWiseAnswerModel>?, val sectionwise: T) : TextWatcher {

        private var position: Int = 0

        fun updatePosition(position: Int) {
            this.position = position
        }


        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            // no op
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

            answerlist!![position].is_values=charSequence.toString()


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
                       // currentActivity!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }


                } else {

                    // val edit = v as AppCompatEditText
                    // edit.isCursorVisible = true
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }



            if(sectionwise is SubSectionWise && question_id == 6 && !hasFocus ){
                Log.e("TAG","onFocusChange"+question_id+" "+hasFocus)
                sectionwise.mAdapter.notifyDataSetChanged()
            }



            }


    }


}
