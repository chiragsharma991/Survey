package com.softtech360.totalservey.fragment

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.adapter.universal.statuswise.AddDisableAdapter
import com.softtech360.totalservey.adapter.universal.statuswise.StatusParentAdapter
import com.softtech360.totalservey.databinding.*
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.OnBackPressedListener
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.adddisableuser.*
import kotlinx.android.synthetic.main.questionanswer.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread

class AddDisableUser : BaseFragment(), OnBackPressedListener {


    var user_type: Int? = null
    var list: ArrayList<SectionWise.SectionWiseModel>? = null
    var addDisableuserList: ArrayList<SectionWise.SectionWiseModel>? = null
    var section_name: String = ""
    var sectionId : Int? = null


    companion object {

        val TAG = "AddDisableUser"
        fun newInstance(user_type: Int , sectionId : Int ): AddDisableUser {

            val fragment = AddDisableUser()
            val bundle = Bundle()
            bundle.putInt("USER_TYPE", user_type)
            bundle.putInt("SECTIONID",sectionId)
            fragment.arguments = bundle

            return fragment
        }

    }

    override fun onBackPressed() {
        loge(TAG, "onBackPressed--AddDisableUser-")
        (parentFragment as SectionWise).pop()
    }


    override fun getLayout(): Int {
        return R.layout.adddisableuser

    }


    private lateinit var binding: AdddisableuserBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loge(TAG, "onCreateView---")
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return binding.root

    }


    private var databaseclient: AppDatabase? = null
    private var myclickhandler: MyClickHandler? = MyClickHandler(this)
    lateinit var mAdapter: AddDisableAdapter

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loge(TAG, "saveInstance NULL")
            databaseclient = DatabaseClient.getInstance(activity!!.applicationContext).getappDatabase()
            binding.onclick = myclickhandler

            user_type = arguments!!.getInt("USER_TYPE")
            sectionId=arguments!!.getInt("SECTIONID")


            onlytxt_toolbar.text = ""

            setPwdList()


        } else {
            loge(TAG, "saveInstance Not NULL")


        }

    }

    private fun setPwdList() {

        thread {

            var result = 0
            var user_type: Int = 1
            list = ArrayList()
            // user type : user type is per question entry in perticulaer one section.
            while (databaseclient!!.pwddao().getQuestionbyFromSecId(user_type = user_type, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),section_id =sectionId!! + 1 ).size > 0) {

                val data = databaseclient!!.pwddao().getQuestionbyFromSecId(user_type = user_type, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),section_id = sectionId!! + 1)

                val question: ArrayList<Pwd> = ArrayList(data.size) // change list to array list
                question.addAll(data)

                for (i in 0.until(question.size)) {

                    val type = object : TypeToken<ArrayList<Answers>>() {}.type
                    val answer: ArrayList<Answers> = Gson().fromJson(question[i].answer, type)
                    val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                    for (j in 0.until(answer.size)) {
                        answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                    }
                    list!!.add(SectionWise.SectionWiseModel(section_id = question[i].section_id, section_name = question[i].section_name, question = question[i].question, question_id = question[i].question_id, question_type = question[i].question_type, answer = answerlist,user_type = user_type,is_pwd =question[i].is_pwd ))

                    // answerlist.clear()

                }


                result = result + 1
                user_type = user_type + 1
            }

            // make list acording to question id=6

            addDisableuserList = ArrayList()

            for (value in list!!){
                if(value.question_id == 6){
                    addDisableuserList!!.add(SectionWise.SectionWiseModel(section_name = value.section_name,question_id = value.question_id,section_id = value.section_id,question =value.question,question_type = value.question_type,answer = value.answer,user_type = value.user_type,is_pwd = value.is_pwd))
                }

            }


            context!!.runOnUiThread { refresh(addDisableuserList!!) }

        }


    }


    private fun refresh(list : ArrayList<SectionWise.SectionWiseModel>){

        loge(TAG,"recyclerview_disableuser-- "+recyclerview_disableuser)
        recyclerview_disableuser.layoutManager = LinearLayoutManager(getActivityBase())
        (recyclerview_disableuser.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)
        mAdapter = AddDisableAdapter(context!!,list,this@AddDisableUser)
        recyclerview_disableuser.adapter = mAdapter

    }

    fun checkValidation() : Boolean{

        if(addDisableuserList == null ) return false

        for (i in 0.until(addDisableuserList!!.size)){
            // if someone get false , result has been false
            val key = i + 1
            HostActivity.pwdName.put(key,addDisableuserList!![i].answer[0].is_values.trim())

            if(addDisableuserList!![i].answer[0].is_values.trim().length <= 0){
                return false
            }
        }

        return true

    }

    private fun submitUser(){

        if(checkValidation()){

            thread {

                for (value in addDisableuserList!!){

                    databaseclient!!.pwddao().update(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),
                            user_type = value.user_type , answer = Gson().toJson(value.answer) ,question_id = value.question_id,is_saved = false)
                }

                context!!.runOnUiThread {

                    (parentFragment as SectionWise).addSubQuestion(user_type!!,sectionId!!)

                }


            }


        }else{
            DialogUtils.openDialog(context = context!!, btnNegative = "", btnPositive = getString(R.string.ok), color = ContextCompat.getColor(context!!, R.color.theme_color), msg = "Please fill the all details first", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {
                }
                override fun onNegativeButtonClick() {
                }
            })
        }
    }




    class MyClickHandler(val adddisableuser: AddDisableUser) {


        fun onNext(view: View) {
            adddisableuser.submitUser()
        }
        fun onCancel(view: View) {

            DialogUtils.openDialog(context = adddisableuser.context!!, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(adddisableuser.context!!, R.color.theme_color), msg = "Are you sure to go home?", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {
                    val intent = Intent()
                    adddisableuser.activity!!.setResult(Activity.RESULT_OK, intent)
                    adddisableuser.activity!!.finish()
                }
                override fun onNegativeButtonClick() {
                }
            })

        }

        fun onBack(view: View) {
            adddisableuser.onBackPressed()
        }


        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logd(TAG, "onCreate---")

    }


    override fun onStart() {
        super.onStart()
        logd(TAG, "onStart---")

    }

    override fun onPause() {
        super.onPause()
        logd(TAG, "onPause---")

    }

    override fun onStop() {
        super.onStop()
        logd(TAG, "onStop---")

    }

    override fun onDestroy() {
        super.onDestroy()
        logd(TAG, "onDestroy---")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        logd(TAG, "onDestroyView---")

    }

    override fun onDetach() {
        super.onDetach()
        logd(TAG, "onDetach---")

    }


}