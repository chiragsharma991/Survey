package com.softtech360.totalservey.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.Login
import com.softtech360.totalservey.adapter.universal.SearchableAdapter
import kotlinx.android.synthetic.main.fragment_dialog.*
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread



class CustomListViewDialog(var activity: Activity, internal var adapter: RecyclerView.Adapter<*>, val village_council_list: ArrayList<Login.UserStates>) : Dialog(activity),
        View.OnClickListener {

    var dialog: Dialog? = null

    internal var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_dialog)

        recyclerView = recyclerview_search
        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter

        search_edt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                filterlist(search_edt.text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


        })

      //  yes.setOnClickListener(this)
       // no.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
        /*    R.id.yes -> {
            }
            R.id.no -> dismiss()
            else -> {
            }*/
        }//Do Something
       // dismiss()
    }


    private fun filterlist(value : String) {

        val filteredList = ArrayList<Login.UserStates>()
        Log.e("tag","filterlist----")
        thread {
            val search_text = value.trim().toLowerCase()

            if(search_text.length <= 0 ){
                // data is empty
                filteredList.addAll(village_council_list)
                Log.e("No filtered value","- "+filteredList.size+" "+filteredList.toString())
            }else{



               loop@ for (i  in 0.until(village_council_list.size)){

                    if(search_text.get(0).equals(village_council_list[i].name.trim().toLowerCase().get(0))){
                        // match is success

                        for (j in 0.until(search_text.length)){

                            if(!search_text.get(j).equals(village_council_list[i].name.trim().toLowerCase().get(j))){
                                // if not equal any case then search in another
                                continue@loop
                            }


                        }

                        filteredList.add(village_council_list[i])


                    }


                }

                Log.e("filtered value","- "+filteredList.size+" "+filteredList.toString())


            }

            context.runOnUiThread {

                (adapter as SearchableAdapter).refreshlist(filteredList)
            }


        }

    }


}