package com.softtech360.totalservey.adapter.universal

import androidx.databinding.ViewDataBinding

interface RecyclerCallback<VM : ViewDataBinding, T> {
    fun bindData(binder: VM, model: T)
}
