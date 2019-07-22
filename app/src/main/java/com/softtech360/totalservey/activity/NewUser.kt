package com.softtech360.totalservey.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.NewUserBinding
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Status
import com.softtech360.totalservey.utils.PreferenceUtil
import org.jetbrains.anko.*
import kotlin.concurrent.thread
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import android.widget.AdapterView.OnItemSelectedListener
import com.softtech360.totalservey.adapter.universal.SpinAdapter
import com.softtech360.totalservey.utils.DialogUtils
import kotlinx.android.synthetic.main.new_user.*
import com.softtech360.totalservey.adapter.universal.SearchableAdapter
import com.softtech360.totalservey.utils.CustomListViewDialog
import android.widget.SpinnerAdapter


class NewUser : BaseActivity(), AnkoLogger {


    companion object {
        val TAG = "NewUser"
        fun newInstance(): NewUser {
            return NewUser()
        }
    }


    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private lateinit var binding: NewUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.new_user)
        initView(savedInstanceState)

    }


    val myclickhandler: MyClickHandler = MyClickHandler(this)
    private var databaseclient: AppDatabase? = null
    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {

            loge(MainActivity.TAG, "saveInstance Not NULL")
            databaseclient = DatabaseClient.getInstance(applicationContext).getappDatabase()
            binding.onclick = myclickhandler
            setSpinnervalues()


/*
            thread {

               // val statuslist = databaseclient!!.statusdao().getStatusListByformId(PreferenceUtil.getInt(PreferenceUtil.FORM_ID,1))
                val statuslist = databaseclient!!.statusdao().getStatusListByformId(0) // no need to get all values from table

                runOnUiThread(){

                    if(statuslist.size > 0){

                        setSpinnervalues(statuslist)

                    }
                }

            }
*/


        } else {

            logd(MainActivity.TAG, "saveInstance Not NULL")

        }

    }

    private var state_value: String = "-1"
    private var district_value: String = "-1"
    private var section_value: String = "-1"
    private var village_council_value: String = "-1"

    /*
        private fun setSpinnervalues( status_list  : List<Status> ){

            district_error.visibility= View.GONE
            section_error.visibility= View.GONE
            village_council_error.visibility= View.GONE
            colony_error.visibility= View.GONE
            village_error.visibility= View.GONE
            state_error.visibility= View.GONE

            dob.text=if(status_list[0].date_time.trim().length > 0) status_list[0].date_time.trim() else getCurrentDate()
            organization.text=if(status_list[0].organization.trim().length > 0) status_list[0].organization.trim() else PreferenceUtil.getString(PreferenceUtil.ORGANIZATION_NAME,"")
            village.setText(if(status_list[0].village.trim().length > 0) status_list[0].village.trim() else "")
            colony.setText(if(status_list[0].colony.trim().length > 0) status_list[0].colony.trim() else "" )

            val user_states= PreferenceUtil.getString(PreferenceUtil.USER_STATES,"")
            val user_districts= PreferenceUtil.getString(PreferenceUtil.USER_DISTRICTS,"")
            val user_sections= PreferenceUtil.getString(PreferenceUtil.USER_SECTIONS,"")
            val user_village_council= PreferenceUtil.getString(PreferenceUtil.USER_VILLAGE_COUNCIL,"")


            val type = object : TypeToken<ArrayList<Login.UserStates>>() {}.type

            //--------State---------

            val gson_state : ArrayList<Login.UserStates> = Gson().fromJson(user_states,type)
            val list_state = ArrayList<Login.UserStates>()
            list_state.add(Login.UserStates(is_deleted = 0,name = "Select state",id = 0,user_id = 0))
            list_state.addAll(gson_state)
            var adapter = SpinAdapter(this@NewUser,android.R.layout.simple_spinner_dropdown_item,list_state)

            state.setAdapter(adapter)
            state.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                    state_value=list_state[position].id.toString()
                }
                override fun onNothingSelected(arg0: AdapterView<*>) {
                }
            })

            if(status_list[0].state.trim().length > 0){
                for(value in list_state){
                    if(value.id ==status_list[0].state.trim().toInt()){
                        val selection_position = adapter.getPosition(value)
                        state.setSelection(selection_position)
                        break
                    }
                }
            }



            //--------dist---------


            val gson_dist : ArrayList<Login.UserStates> = Gson().fromJson(user_districts,type)
            val list_dist = ArrayList<Login.UserStates>()
            list_dist.add(Login.UserStates(is_deleted = 0,name = "Select district",id = 0,user_id = 0))
            list_dist.addAll(gson_dist)
            adapter = SpinAdapter(this@NewUser,android.R.layout.simple_spinner_dropdown_item,list_dist)
            district.setAdapter(adapter)
            district.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                    district_value=list_dist[position].id.toString()
                }
                override fun onNothingSelected(arg0: AdapterView<*>) {
                }
            })

            if(status_list[0].district.trim().length > 0){
                for(value in list_dist){
                    if(value.id ==status_list[0].district.trim().toInt()){
                        val selection_position = adapter.getPosition(value)
                        district.setSelection(selection_position)
                        break
                    }
                }
            }


            //-- section---

            val gson_sections : ArrayList<Login.UserStates>  = Gson().fromJson(user_sections,type)
            val list_sections = ArrayList<Login.UserStates>()
            list_sections.add(Login.UserStates(is_deleted = 0,name = "Select section",id = 0,user_id = 0))
            list_sections.addAll(gson_sections)
            adapter = SpinAdapter(this@NewUser,android.R.layout.simple_spinner_dropdown_item,list_sections)
            section.setAdapter(adapter)
            section.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                    section_value=list_sections[position].id.toString()

                }
                override fun onNothingSelected(arg0: AdapterView<*>) {
                }
            })

            if(status_list[0].section.trim().length > 0){
                for(value in list_sections){
                    if(value.id ==status_list[0].section.trim().toInt()){
                        val selection_position = adapter.getPosition(value)
                        section.setSelection(selection_position)
                        break
                    }
                }
            }


            //-----village council---

            val gson_village_council : ArrayList<Login.UserStates> = Gson().fromJson(user_village_council,type)
            val list_village_council = ArrayList<Login.UserStates>()
            list_village_council.add(Login.UserStates(is_deleted = 0,name = "Select village council",id = 0,user_id = 0))
            list_village_council.addAll(gson_village_council)
            adapter = SpinAdapter(this@NewUser,android.R.layout.simple_spinner_dropdown_item,list_village_council)
            village_council.setAdapter(adapter)
            village_council.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                    village_council_value=list_village_council[position].id.toString()
                }
                override fun onNothingSelected(arg0: AdapterView<*>) {
                }
            })
            if(status_list[0].village_council.trim().length > 0){
                for(value in list_village_council){
                    if(value.id ==status_list[0].village_council.trim().toInt()){
                        val selection_position = adapter.getPosition(value)
                        village_council.setSelection(selection_position)
                        break
                    }
                }
            }



        }
    */
    private fun setSpinnervalues() {

        district_error.visibility = View.GONE
        section_error.visibility = View.GONE
        village_council_error.visibility = View.GONE
        colony_error.visibility = View.GONE
        village_error.visibility = View.GONE
        state_error.visibility = View.GONE

        // array list for all
        val list_state = ArrayList<Login.UserStates>()
        val list_dist = ArrayList<Login.UserStates>()
        val list_sections = ArrayList<Login.UserStates>()
        var village_council_list = ArrayList<Login.UserStates>()


        // adapter for all
        var state_adapter: SpinAdapter? = null
        var dist_adapter: SpinAdapter? = null
        var section_adapter: SpinAdapter? = null
        var villagecouncil_adapter: SearchableAdapter? = null


        dob.text = getCurrentDate()
        organization.text = PreferenceUtil.getString(PreferenceUtil.ORGANIZATION_NAME, "")

        // get list from sharedprefrence
        val user_states = PreferenceUtil.getString(PreferenceUtil.USER_STATES, "")
        val user_districts = PreferenceUtil.getString(PreferenceUtil.USER_DISTRICTS, "")
        val user_sections = PreferenceUtil.getString(PreferenceUtil.USER_SECTIONS, "")
        val user_village_council = PreferenceUtil.getString(PreferenceUtil.USER_VILLAGE_COUNCIL, "")


        val type = object : TypeToken<ArrayList<Login.UserStates>>() {}.type

        //--------State---------

        val gson_state: ArrayList<Login.UserStates> = Gson().fromJson(user_states, type)
        list_state.add(Login.UserStates(is_deleted = 0, name = "Select state", id = -1, user_id = 0))
        list_state.addAll(gson_state)
        state_adapter = SpinAdapter(this@NewUser, android.R.layout.simple_spinner_dropdown_item, list_state)

        var check_state = 0
        state.setAdapter(state_adapter)

        state.setOnItemSelectedListener(object : OnItemSelectedListener {


            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

                loge(TAG, "onItemSelected-----")

                if (++check_state > 1) {


                    state_value = list_state[position].id.toString()

                    // change dist values:
                    var temp_list: ArrayList<Login.UserStates> = ArrayList()
                    temp_list.add(Login.UserStates(is_deleted = 0, name = "Select district", id = -1, user_id = 0))
                    val gson_dist: ArrayList<Login.UserStates> = Gson().fromJson(user_districts, type)

                    for (value in gson_dist) {

                        //  loge(TAG,"-temp------"+value.state_id+" - "+list_state[position].id)
                        if (value.state_id == list_state[position].id) {
                            temp_list.add(value)
                        }
                    }
                    list_dist.clear()
                    list_dist.addAll(temp_list)
                    district_value="-1"
                    dist_adapter!!.notifyDataSetChanged()
                    district.setSelection(0)


                    // change section values:

                    temp_list = ArrayList()
                    temp_list.add(Login.UserStates(is_deleted = 0, name = "Select section", id = -1, user_id = 0))

                    list_sections.clear()
                    section_value="-1"
                    list_sections.addAll(temp_list)
                    section_adapter!!.notifyDataSetChanged()
                    section.setSelection(0)

                    // change village council values:

                    village_council.text = "Select village council"
                    village_council.setTextColor(ContextCompat.getColor(this@NewUser,R.color.black_transparent))
                    village_council_list.clear()
                    village_council_value="-1"
                    villagecouncil_adapter?.let { it.notifyDataSetChanged() }


                }


            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
            }
        })


        //--------dist---------


        val gson_dist: ArrayList<Login.UserStates> = Gson().fromJson(user_districts, type)
        list_dist.add(Login.UserStates(is_deleted = 0, name = "Select district", id = -1, user_id = 0))
        list_dist.addAll(gson_dist)
        dist_adapter = SpinAdapter(this@NewUser, android.R.layout.simple_spinner_dropdown_item, list_dist)
        district.setAdapter(dist_adapter)
        var check_district = 0
        district.setOnItemSelectedListener(object : OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

                if (++check_district > 1) {

                    district_value = list_dist[position].id.toString()

                    // change section values:
                    val temp_list: ArrayList<Login.UserStates> = ArrayList()
                    temp_list.add(Login.UserStates(is_deleted = 0, name = "Select section", id = -1, user_id = 0))
                    val gson_sections: ArrayList<Login.UserStates> = Gson().fromJson(user_sections, type)


                    for (value in gson_sections) {

                        //  loge(TAG,"-temp------"+value.state_id+" - "+list_state[position].id)
                        if (value.district_id == list_dist[position].id) {
                            temp_list.add(value)
                        }
                    }
                    list_sections.clear()
                    list_sections.addAll(temp_list)
                    section_value="-1"
                    section_adapter!!.notifyDataSetChanged()
                    section.setSelection(0)


                    // change village council values:

                    village_council.text = "Select village council"
                    village_council.setTextColor(ContextCompat.getColor(this@NewUser,R.color.black_transparent))
                    village_council_value="-1"
                    village_council_list.clear()
                    villagecouncil_adapter?.let { it.notifyDataSetChanged() }


                }

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
            }
        })


        //-- section---

        val gson_sections: ArrayList<Login.UserStates> = Gson().fromJson(user_sections, type)
        list_sections.add(Login.UserStates(is_deleted = 0, name = "Select section", id = -1, user_id = 0))
        list_sections.addAll(gson_sections)
        section_adapter = SpinAdapter(this@NewUser, android.R.layout.simple_spinner_dropdown_item, list_sections)
        section.setAdapter(section_adapter)
        var check_section = 0
        section.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

                if (++check_section > 1) {

                    section_value = list_sections[position].id.toString()

                    // change village council values:

                    val temp_list: ArrayList<Login.UserStates> = ArrayList()
                    val gson_council_list : ArrayList<Login.UserStates> = Gson().fromJson(PreferenceUtil.getString(PreferenceUtil.USER_VILLAGE_COUNCIL, ""), type)


                    for (value in gson_council_list) {

                        //  loge(TAG,"-temp------"+value.state_id+" - "+list_state[position].id)
                        if (value.section_id == list_sections[position].id) {
                            temp_list.add(value)
                        }
                    }



                    village_council.text = "Select village council"
                    village_council.setTextColor(ContextCompat.getColor(this@NewUser,R.color.black_transparent))
                    village_council_value="-1"
                    village_council_list.clear()
                    village_council_list = temp_list
                    villagecouncil_adapter?.let { it.notifyDataSetChanged() }


                }

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
            }
        })


        //-----village council---

        var dialog :CustomListViewDialog?=null
        village_council_list = Gson().fromJson(PreferenceUtil.getString(PreferenceUtil.USER_VILLAGE_COUNCIL, ""), type)
        village_council.text = "Select village council"
        village_council.setTextColor(ContextCompat.getColor(this,R.color.black_transparent))
        village_council.setOnClickListener {

            villagecouncil_adapter = SearchableAdapter(village_council_list, object : SearchableAdapter.RecyclerViewItemClickListener {

                override fun <T> clickOnItem(model: T) {
                    //hide keyboard :
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    village_council_value = (model as Login.UserStates).id.toString()
                    village_council.text = (model as Login.UserStates).name
                    village_council.setTextColor(ContextCompat.getColor(this@NewUser,R.color.black_light))
                    dialog!!.dismiss()

                }
            })

            dialog = CustomListViewDialog(this@NewUser, villagecouncil_adapter!!, village_council_list)

            loge(TAG,"village_council----"+village_council_list.size+" "+village_council_list.toString())
            dialog!!.show()
            dialog!!.setCanceledOnTouchOutside(false)

        }

    }

    private fun checkValidation(): Boolean {

        var result = true



        if (village.text!!.trim().length <= 0) {
            village_error.visibility = View.VISIBLE
            result = false
        } else {
            village_error.visibility = View.GONE
        }


        if (colony.text!!.trim().length <= 0) {
            colony_error.visibility = View.VISIBLE
            result = false
        } else {
            colony_error.visibility = View.GONE
        }

        loge(TAG, "state_value-" + state_value)
        if (state_value == "-1") {
            state_error.visibility = View.VISIBLE
            result = false
        } else {
            state_error.visibility = View.GONE
        }



        if (district_value == "-1") {
            district_error.visibility = View.VISIBLE
            result = false
        } else {
            district_error.visibility = View.GONE
        }


        if (section_value == "-1") {
            section_error.visibility = View.VISIBLE
            result = false
        } else {
            section_error.visibility = View.GONE
        }



        if (village_council_value == "-1") {
            village_council_error.visibility = View.VISIBLE
            result = false

        } else {
            village_council_error.visibility = View.GONE
        }



        return result

    }


    private fun onContinue() {

        if (checkValidation()) {

            thread {
                // pass the values in status table:
                //     loge(TAG,"updateEntries -- "+dob.text!!.trim().toString()+" "+state_value+" "+district_value+" "+section_value+" "+village_council_value+" "+village.text!!.trim().toString()+" "+colony.text!!.trim().toString())

                /*     databaseclient!!.statusdao().updateEntries(
                             form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),
                             date_time = dob.text!!.trim().toString(),
                             latitude = "Unknown",
                             longitude = "Unknown",
                             state = state_value,
                             district = district_value,
                             section = section_value,
                             village_council = village_council_value,
                             village = village.text!!.trim().toString(),
                             colony = colony.text!!.trim().toString()
                             )*/

                var form_id: Int = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0)
                form_id = form_id + 1
                loge(TAG, "form id - " + form_id)
                PreferenceUtil.putValue(PreferenceUtil.FORM_ID, form_id)
                PreferenceUtil.save()

                databaseclient!!.statusdao().insert(
                        Status(
                                form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                                date_time = dob.text!!.trim().toString(),
                                latitude = "Unknown",
                                longitude = "Unknown",
                                state = state_value,
                                district = district_value,
                                section = section_value,
                                village_council = village_council_value,
                                village = village.text!!.trim().toString(),
                                colony = colony.text!!.trim().toString(),
                                serial_number = null,
                                user_id = PreferenceUtil.getInt(PreferenceUtil.USER_ID, 0),
                                organization = PreferenceUtil.getString(PreferenceUtil.ORGANIZATION_NAME, "")!!,
                                email = PreferenceUtil.getString(PreferenceUtil.EMAIL, "")!!,
                                submission_status = false
                        )
                )





                runOnUiThread {

                    val intent = Intent(this, HostActivity::class.java)
                    startActivityForResult(intent, 1)

                }

            }

        } else {
            DialogUtils.openDialog(context = this, btnNegative = "", btnPositive = getString(R.string.ok), color = ContextCompat.getColor(this, R.color.theme_color), msg = "Please fill the all details first", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {
                }

                override fun onNegativeButtonClick() {
                }
            })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        loge(TAG, "onActivityResult -- " + requestCode + " - " + resultCode)

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                loge(TAG, "on activity result---")
                onBackPressed()

            } else if (resultCode == 3) {
                // start new survey
                val intent = Intent()
                setResult(3, intent)
                finish()
            }
        }
    }


    class MyClickHandler(val newuser: NewUser) {


        fun onContinue(view: View) {

            newuser.onContinue()


        }


    }


    private var selectedtimeslot_position: Int = 0
    private fun selectdeliverytime(list: ArrayList<String>, title: String, appcompattextview: AppCompatTextView) {

        val item = arrayOfNulls<String>(list.size)
        list.toArray(item)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setSingleChoiceItems(item, selectedtimeslot_position, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item_position: Int) {
                //   selectedtimeslot_position=item_position
                loge(TAG, "onclick--")
            }
        })
        /*  .setPositiveButton("Done!", object : DialogInterface.OnClickListener {
              override fun onClick(dialog: DialogInterface, id: Int) {
                  loge(TAG,"onclick-Done-")
                  appcompattextview.text=item[selectedtimeslot_position]


              }
          })
          .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
              override fun onClick(dialog: DialogInterface, id: Int) {
                  loge(TAG,"onclick-Cancel-")

              }
          })*/
        val dialog = builder.create()
        dialog.show()
    }


    private fun getstringArray(list: ArrayList<Login.UserStates>): ArrayList<String> {
        val value = ArrayList<String>()
        for (i in 0.until(list.size)) {
            value.add(list[i].name)
        }
        return value
    }


}


/*
class MyDialogFragment() : DialogFragment() {


    private var mRecyclerView: RecyclerView? = null
    private val adapter: SearchableAdapter? = null
    // this method create view for your Dialog


    @Override
    internal fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        //inflate layout with recycler view
        val type = object : TypeToken<ArrayList<Login.UserStates>>() {}.type
        val user_village_council= PreferenceUtil.getString(PreferenceUtil.USER_VILLAGE_COUNCIL,"")
        val village_council_list : ArrayList<Login.UserStates> = Gson().fromJson(user_village_council,type)
        val v = inflater.inflate(R.layout.fragment_dialog, container, false)
        mRecyclerView = v.findViewById(R.id.recyclerview_search)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        //setadapter
        val adapter = SearchableAdapter(context!!, village_council_list)
        mRecyclerView!!.adapter = adapter
        //get your recycler view and populate it.
        return v
    }
}
*/


/*
class yDialogFragment:DialogFragment() {

    private var mRecyclerView: RecyclerView? = null

    fun onCreateDialog(savedInstanceState:Bundle):Dialog {
        mRecyclerView = RecyclerView(getContext())
        // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
        mRecyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        mRecyclerView.setAdapter(*/
/* your adapter *//*
)
        return AlertDialog.Builder(getActivity())
                .setTitle(*/
/* your title *//*
)
                .setView(mRecyclerView)
                .setPositiveButton(android.R.string.ok,
                        object:DialogInterface.OnClickListener() {
                            fun onClick(dialog:DialogInterface, whichButton:Int) {
                                // do something
                            }
                        }
                ).create()
    }
}
*/




