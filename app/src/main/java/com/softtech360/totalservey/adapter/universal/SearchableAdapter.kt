package com.softtech360.totalservey.adapter.universal

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.Login
import com.softtech360.totalservey.databinding.RowOptionalBinding
import com.softtech360.totalservey.databinding.RowSearchitemBinding
import com.softtech360.totalservey.fragment.SectionWise
import kotlinx.android.synthetic.main.row_searchitem.view.*
import java.util.ArrayList


class SearchableAdapter(

        private var village_council_list : ArrayList<Login.UserStates>,
        internal var recyclerViewItemClickListener: RecyclerViewItemClickListener

) : RecyclerView.Adapter<SearchableAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_searchitem, parent, false)

        return MyViewHolder(v)

    }

    override fun onBindViewHolder(holder : MyViewHolder, i: Int) {

     //   if(i < 75 ){

            holder.mTextView.text = village_council_list[i].name
     //   }else{
          //  holder.mTextView.text = "test"
     //   }


    }

    override fun getItemCount(): Int {
        Log.e("getItemCount",""+village_council_list.size)
        return village_council_list.size
    }


    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var mTextView: AppCompatTextView

        init {
            mTextView = v.title
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            recyclerViewItemClickListener.clickOnItem(village_council_list[this.adapterPosition])

        }
    }

    interface RecyclerViewItemClickListener {
        fun <T> clickOnItem(userstates: T)
    }


    fun refreshlist(list_filtered: ArrayList<Login.UserStates>) {
        village_council_list=list_filtered
        notifyDataSetChanged()
    }
}




