package com.softtech360.totalservey.activity

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.HelpBinding
import com.softtech360.totalservey.databinding.LoginBinding
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

class Help : BaseActivity(){

    companion object {
        val TAG = "Help"
        fun newInstance(): Help {
            return Help()
        }
    }

    private lateinit var binding: HelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this@Help,R.layout.help)
        initView(savedInstanceState)


    }


    val myclickhandler: MyClickHandler = MyClickHandler(this)
    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            loge(MainActivity.TAG, "saveInstance Not NULL")
            txt_toolbar.visibility = View.VISIBLE
            txt_toolbar.text="Help"
            img_toolbar_back.visibility= View.VISIBLE
            img_toolbar_back.setOnClickListener{ finish() }

        } else {

            logd(MainActivity.TAG, "saveInstance Not NULL")

        }

    }


    class MyClickHandler(val help: Help) {






    }
}