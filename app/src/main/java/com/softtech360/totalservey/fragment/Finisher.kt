package com.softtech360.totalservey.fragment


import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.adapter.universal.statuswise.StatusParentAdapter
import com.softtech360.totalservey.databinding.FinisherBinding
import com.softtech360.totalservey.databinding.QuestionanswerBinding
import com.softtech360.totalservey.databinding.SectionwiseBinding
import com.softtech360.totalservey.databinding.SubsectionwiseBinding
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.OnBackPressedListener
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.questionanswer.*
import kotlinx.android.synthetic.main.subsectionwise.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread

class Finisher : BaseFragment(), OnBackPressedListener {




    companion object {

        val TAG = "Finisher"
        fun newInstance(): Finisher {

            val fragment = Finisher()

            return fragment
        }

    }


    override fun onBackPressed() {
        loge(TAG, "onBackPressed--Finisher-")
       // (parentFragment as QuestionAnswer).pop()

    }





    override fun getLayout(): Int {
        return R.layout.finisher

    }


    private lateinit var binding: FinisherBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loge(TAG, "onCreateView---")
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private var databaseclient: AppDatabase? = null
    private var myclickhandler: MyClickHandler? = MyClickHandler(this)
    lateinit var mAdapter: StatusParentAdapter<SubSectionWise>

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loge(TAG, "saveInstance NULL")
            databaseclient = DatabaseClient.getInstance(activity!!.applicationContext).getappDatabase()
            binding.onclick=myclickhandler

            onlytxt_toolbar.text=""
            savedataFormWise()


        }else{
            loge(TAG, "saveInstance Not NULL")



        }

    }

    private fun savedataFormWise(){


        thread {

            databaseclient!!.statusdao().updateCompleteDate(
                    form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),
                    completed_at = getCurrentDate()
            )

            val jsonArray = JsonArray()
            val list = databaseclient!!.answerdao().getAnswerbyFormId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0))

            for (i in 0.until(list.size)){
                val jsonObject = JsonObject()

                val type = object : TypeToken<ArrayList<Answers>>() {}.type
                val answer : ArrayList<Answers> = Gson().fromJson(list[i].answer, type)

                if(list[i].question_type == 1 || list[i].question_type == 2){
                    //TYPE_EDTTEXT || TYPE_EDITNUM
                    jsonObject.addProperty("section_id",list[i].section_id)
                    jsonObject.addProperty("question_id",list[i].question_id)
                    jsonObject.addProperty("answer_id","0")
                    jsonObject.addProperty("answer",answer[0].is_values)

                }else if(list[i].question_type == 5){
                    // TYPE_OPTIONAL
                    jsonObject.addProperty("section_id",list[i].section_id)
                    jsonObject.addProperty("question_id",list[i].question_id)
                    var is_any_check : Boolean = false
                    for(value in answer){

                        if(value.is_selected){
                            // yes/no  some one is selected
                            if(value.answer == "हाँ"){
                                // yes
                              //  jsonObject.addProperty("answer","1")
                                is_any_check = true

                            }else{
                                //no
                               // jsonObject.addProperty("answer","0")
                            }

                              //  break
                        }

                        //

                    }
                    jsonObject.addProperty("answer",if(is_any_check) "1" else "0")
                    jsonObject.addProperty("answer_id","")

                }
                else  {
                    //TYPE_RADIO || TYPE_CHECK
                    jsonObject.addProperty("section_id",list[i].section_id)
                    jsonObject.addProperty("question_id",list[i].question_id)
                    val answer_id_list = ArrayList<Int>()
                    for(value in answer){
                        if(value.is_selected)
                        answer_id_list.add(value.answer_id)
                    }
                    jsonObject.addProperty("answer_id",TextUtils.join(",", answer_id_list))
                    jsonObject.addProperty("answer", if(list[i].question_type == 3)"" else "0")

                }

                jsonArray.add(jsonObject)

            }


            // add pwd survey.

            val pwdList = databaseclient!!.pwddao().getQuestionUsingFormId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0))

            for (j in 0.until(pwdList.size)){

                val jsonObject = JsonObject()



                val type = object : TypeToken<ArrayList<Answers>>() {}.type
                val answer : ArrayList<Answers>  = Gson().fromJson(pwdList[j].answer, type)
                if(pwdList[j].question_type == 1 || pwdList[j].question_type == 2){
                    //TYPE_EDTTEXT || TYPE_EDITNUM
                    jsonObject.addProperty("section_id",pwdList[j].section_id)
                    jsonObject.addProperty("question_id",pwdList[j].question_id)
                    jsonObject.addProperty("answer_id","0")
                    jsonObject.addProperty("answer",answer[0].is_values)

                }
                else if(pwdList[j].question_type == 5){
                    // TYPE_OPTIONAL
                    jsonObject.addProperty("section_id",pwdList[j].section_id)
                    jsonObject.addProperty("question_id",pwdList[j].question_id)
                    var is_any_check : Boolean = false

                    for(value in answer){

                        if(value.is_selected){
                            // yes/no  some one is selected
                            if(value.answer == "हाँ"){
                                // yes
                                is_any_check = true
                                //jsonObject.addProperty("answer","1")

                            }else{
                                //no
                                //jsonObject.addProperty("answer","0")
                            }

                            break
                        }
                    }

                    jsonObject.addProperty("answer",if(is_any_check) "1" else "0")
                    jsonObject.addProperty("answer_id","")

                }

                else {
                    //TYPE_RADIO || TYPE_CHECK
                    jsonObject.addProperty("section_id",pwdList[j].section_id)
                    jsonObject.addProperty("question_id",pwdList[j].question_id)
                    val answer_id_list = ArrayList<Int>()
                    for(value in answer){
                        if(value.is_selected)
                        answer_id_list.add(value.answer_id)
                    }


                    jsonObject.addProperty("answer_id",TextUtils.join(",", answer_id_list))
                    jsonObject.addProperty("answer",if(pwdList[j].question_type == 3)"" else "0")

                }

                jsonArray.add(jsonObject)

            }

            loge(TAG,"list are -- "+jsonArray.toString())



            // pass the all data into status table:

            databaseclient!!.statusdao().updateStatus(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),question_answer = Gson().toJson(jsonArray),is_form_submited = true)


            // delete entry form wise if data is saved in status table.
            databaseclient!!.pwddao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))
            // delete Answer form
            databaseclient!!.answerdao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))

            context!!.runOnUiThread {

             //   val servey_count = PreferenceUtil.getString(PreferenceUtil.SURVEY_COUNT,"0")
             //   PreferenceUtil.putValue(PreferenceUtil.SURVEY_COUNT,(servey_count!!.toInt() + 1).toString())
               // PreferenceUtil.save()

         /*       Toast.makeText(context,"Thank you! data has been successfully submited",Toast.LENGTH_SHORT).show()
                val intent = Intent()
                activity!!.setResult(Activity.RESULT_OK, intent)
                activity!!.finish()*/

            }

        }


    }










    class MyClickHandler(val finisher : Finisher) {


        fun onNewsurvey(view: View) {

            val intent = Intent()
            finisher.activity!!.setResult(3, intent)
            finisher.activity!!.finish()

        }
        fun onHome(view: View) {

            val intent = Intent()
            finisher.activity!!.setResult(Activity.RESULT_OK, intent)
            finisher.activity!!.finish()
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