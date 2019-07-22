package com.softtech360.totalservey.adapter.universal


import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup




/**
 *
 */
class UniversalAdapter<T, VM : ViewDataBinding>(private val context: Context, private var items: List<T>?, private val layoutId: Int, private val bindingInterface: RecyclerCallback<VM, T>)
    : RecyclerView.Adapter<UniversalAdapter<T,VM>.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var binding: VM? = null

        init {
            binding = DataBindingUtil.bind(view)
        }

        fun bindData(model: T) {
            bindingInterface.bindData(binding!!, model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: UniversalAdapter<T,VM>.RecyclerViewHolder, position: Int) {
        val item = items!![position]
        holder.bindData(item)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0  // value or if you got null then 0
    }

    fun updatedata(items: ArrayList<T>){
        this.items =items

    }


}