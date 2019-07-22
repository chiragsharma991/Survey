package com.softtech360.totalservey.fragment

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.databinding.QuestionanswerBinding
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.utils.OnBackPressedListener
import com.softtech360.totalservey.utils.PreferenceUtil
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.thread


class QuestionAnswer : RessumeQuestion(),OnBackPressedListener {




    companion object {

        val TAG = "QuestionAnswer"
        fun newInstance(): QuestionAnswer {
            return QuestionAnswer()
        }

    }


    override fun onBackPressed() {
        loge(TAG, "onBackPressed--QuestionAnswer-")

    /*    val navHostFragment = activity!!.supportFragmentManager.findFragmentById(R.id.navhost_fragment)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount
        loge(TAG,"nav host backStackEntryCount"+backStackEntryCount+" child backstack--"+childFragmentManager.backStackEntryCount)
        val navController = Navigation.findNavController(activity!!, R.id.navhost_fragment)*/

        if(childFragmentManager.backStackEntryCount> 0){
            loge(TAG,"onBackPressed--QuestionAnswer-Sectionwise"+childFragmentManager.backStackEntryCount)
            val currentFragment = childFragmentManager.getFragments().get(0)
            if (currentFragment is OnBackPressedListener){
                (currentFragment as OnBackPressedListener).onBackPressed() //pass into current fragment.
            }
        }else{
            loge(TAG,"finishing Host Activity--")
            finishThisActivity()
        }


    }



    fun finishThisActivity(){
        activity!!.finish()
    }


    override fun getLayout(): Int {
        return R.layout.questionanswer

    }


    private lateinit var binding: QuestionanswerBinding
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

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loge(TAG, "saveInstance NULL")
            binding.onclick=myclickhandler
            databaseclient = DatabaseClient.getInstance(activity!!.applicationContext).getappDatabase()
            // here is condion for resume or make question

            if(HostActivity.questionIsResume){
                loge(TAG,"questionIsResume--")
                makeEditQuestion(databaseclient!!)
            }else{
                addQuestion(0)
            }


        }else{
            loge(TAG, "saveInstance Not NULL")

        }

    }

/*
    private fun test(sectionId : Int){
        thread {


            val list = databaseclient!!.answerdao().getAnswerbyFormId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0))
            if(list.size > 0){
                // you can resume.

                if(databaseclient!!.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id =sectionId + 1).size > 0){
                    // data is present
                    val sectionList = databaseclient!!.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id =sectionId + 1)

                    context!!.runOnUiThread {
                        moveOnEditQuestion(sectionList,sectionId)
                    }

                }else{
                    // data is no longer present
                }



            }else{
                //no entry found fro resume so just open first section.
            }



        }
    }

    private fun moveOnEditQuestion(sectionlist : List<Answer>,sectionId : Int) {



                // you can resume.

                    Log.e(TAG,"moveOnEditQuestion - - "+sectionId + 1 )

                    val parsinglist = ArrayList<SectionWise.SectionWiseModel>()
                    val arraylist : ArrayList<Answer> = ArrayList(sectionlist.size) // change list to array list
                    arraylist.addAll(sectionlist)

                  // convert row data from database to actual array list.
                    for (i in 0.until(arraylist.size)){
                        Log.e(TAG,"convert row data  - - " )

                        val data = arraylist[i]

                                val type = object : TypeToken<ArrayList<Answers>>() {}.type
                                val answer : ArrayList<Answers> = Gson().fromJson(data.answer, type)
                                val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                                for (j in 0.until(answer.size)){
                                    answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                                }
                                parsinglist.add(SectionWise.SectionWiseModel(section_id =data.section_id , section_name = data.section_name , question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist))

                    }


                        Log.e(TAG,"runOnUiThread  - - " )

                        val fragment = SectionWise.newInstance(parsinglist,sectionId + 1,parsinglist[0].section_name)
                        addFragment(R.id.qacontainer, fragment, SectionWise.TAG , false)


                    // we are not inserted pwd question in this answer list so implement like:
                    Log.e(TAG," sectionId + 1  - - " )

                    var sectionId_ = sectionId + 1
                    if(sectionId_ == 1){
                        sectionId_ = 2
                    }

                    test(sectionId_)








    }
*/



    var parsinglist : ArrayList<SectionWise.SectionWiseModel>? = null

    fun addQuestion(setctionId : Int){



        thread {

            val listFromAnswered = databaseclient!!.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id =setctionId + 1 )

            if(listFromAnswered.size > 0){
                // result is already present so pass this value in next question
                parsinglist = ArrayList()
                val arraylist : ArrayList<Answer> = ArrayList(listFromAnswered.size) // change list to array list
                arraylist.addAll(listFromAnswered)
                convertlisttoArray(arraylist) // convert row data from database to actual array list.
                context!!.runOnUiThread{
                    val fragment = SectionWise.newInstance(parsinglist!!,setctionId + 1,parsinglist!![0].section_name)
                    addFragment(R.id.qacontainer, fragment, SectionWise.TAG , false)
                }

            }else{
                // no result has been found for next question.

                val listFromQuestion = databaseclient!!.questiondao().getQuestionFromSection(section_id = setctionId + 1)

                if(listFromQuestion.size > 0 ){
                    // you can next..
                    parsinglist = ArrayList()
                    val arraylist : ArrayList<Question> = ArrayList(listFromQuestion.size) // change list to array list
                    arraylist.addAll(listFromQuestion)
                    convertlisttoArray(arraylist) // convert row data from database to actual array list.
                    context!!.runOnUiThread{
                        val fragment = SectionWise.newInstance(parsinglist!!,setctionId + 1,parsinglist!![0].section_name)
                        addFragment(R.id.qacontainer, fragment, SectionWise.TAG , false)
                    }

                }
                else{
                    // section has been end..
                    context!!.runOnUiThread{
                        //Toast.makeText(context,"Question has been End.. Thank you!!",Toast.LENGTH_SHORT).show()
                        val fragment = Finisher.newInstance()
                        addFragment(R.id.qacontainer, fragment, Finisher.TAG , false)
                    }
                }

            }


        }

    }



    private fun <T> convertlisttoArray(question : ArrayList<T>){

                for (i in 0.until(question.size)){
                    val data = question[i]
                    when (data) {

                        is Question -> {

                            val type = object : TypeToken<ArrayList<Answers>>() {}.type
                            val answer : ArrayList<Answers> = Gson().fromJson(data.answer, type)
                            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                            for (j in 0.until(answer.size)){
                                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                            }
                            parsinglist!!.add(SectionWise.SectionWiseModel(section_id =data.section_id , section_name = data.section_name , question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist,is_pwd =data.is_pwd ))


                        }

                        is Answer ->{

                            val type = object : TypeToken<ArrayList<Answers>>() {}.type
                            val answer : ArrayList<Answers> = Gson().fromJson(data.answer, type)
                            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                            for (j in 0.until(answer.size)){
                                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                            }
                            parsinglist!!.add(SectionWise.SectionWiseModel(section_id =data.section_id , section_name = data.section_name , question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist,is_pwd =data.is_pwd ))


                        }

                    }

                }
            }






    class MyClickHandler(val questionanswer: QuestionAnswer) {


        fun onNext(view: View) {



        }

        fun onCancel(view: View) {

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