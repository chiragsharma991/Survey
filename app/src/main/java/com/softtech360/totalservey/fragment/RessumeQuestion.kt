package com.softtech360.totalservey.fragment

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.PreferenceUtil
import org.jetbrains.anko.runOnUiThread
import java.util.logging.Handler
import kotlin.concurrent.thread

abstract class RessumeQuestion : BaseFragment() {


    // sectionId : default is 0

    protected fun makeEditQuestion(databaseclient: AppDatabase) {

        thread {


            for(i in 0.until(PreferenceUtil.getInt(PreferenceUtil.TOTAL_SECTION,0))){

                val section_id = i + 1

                if (databaseclient.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = section_id).size > 0) {
                    // data is present


                    val sectionList = databaseclient.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = section_id)

                    context!!.runOnUiThread {
                        moveOnEditQuestion(sectionList, section_id, databaseclient)
                }

                }else{

                    if(section_id == 1){
                        // only to resume of section 1

                        val listFromQuestion = databaseclient.questiondao().getQuestionFromSection(section_id =1)

                        if(listFromQuestion.size > 0 ){
                            // you can next.
                            val parsinglist : ArrayList<SectionWise.SectionWiseModel> = ArrayList()
                            val arraylist : ArrayList<Question> = ArrayList(listFromQuestion.size) // change list to array list
                            arraylist.addAll(listFromQuestion)

                            for (j in 0.until(arraylist.size)){
                                val data = arraylist[j]


                                        val type = object : TypeToken<ArrayList<Answers>>() {}.type
                                        val answer : ArrayList<Answers> = Gson().fromJson(data.answer, type)
                                        val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                                        for (k in 0.until(answer.size)){
                                            answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[k].answer_id, answer = answer[k].answer, question_id = answer[k].question_id, is_selected = answer[k].is_selected, is_values = answer[k].is_values))
                                        }
                                        parsinglist.add(SectionWise.SectionWiseModel(section_id =data.section_id , section_name = data.section_name , question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist,is_pwd =data.is_pwd ))

                            }

                            context!!.runOnUiThread{
                                val fragment = SectionWise.newInstance(parsinglist,1,parsinglist[0].section_name)
                                addFragment(R.id.qacontainer, fragment, SectionWise.TAG , false)
                            }


                        }

                    }

                }



            /*    else{
                    // form start with +1

                    val sectionList = databaseclient.questiondao().getQuestionFromSection(section_id = section_id)

                    context!!.runOnUiThread {

                        // you can resume.

                        val parsinglist = ArrayList<SectionWise.SectionWiseModel>()
                        val arraylist: ArrayList<Question> = ArrayList(sectionList.size) // change list to array list
                        arraylist.addAll(sectionList)

                        // convert row data from database to actual array list.
                        for (k in 0.until(arraylist.size)) {

                            val data = arraylist[k]

                            val type = object : TypeToken<ArrayList<Answers>>() {}.type
                            val answer: ArrayList<Answers> = Gson().fromJson(data.answer, type)
                            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                            for (j in 0.until(answer.size)) {
                                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                            }
                            parsinglist.add(SectionWise.SectionWiseModel(section_id = data.section_id, section_name = data.section_name, question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist,is_pwd = data.is_pwd))

                        }

                        val fragment = SectionWise.newInstance(parsinglist, section_id , parsinglist[0].section_name)
                        addFragment(R.id.qacontainer, fragment, SectionWise.TAG, false)


                    }

                   break

                }*/

            }


        }
    }

    protected fun makeSubEditQuestion(user_type: Int, databaseclient: AppDatabase, sectionId: Int, p_section_id: Int) {

        thread {
            loge("tag","makeSubEditQuestion--"+user_type)
            // default user type = 0 , p section = 1, section =1  And we start from user type = 1 , p section = 1, section =2
               val totalsection = databaseclient.pwddao().getlastSectionid(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),p_section_id =p_section_id)

               val pwdList = databaseclient.pwddao().getQuestionFromPsection(user_type = user_type + 1, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),p_section_id =p_section_id,section_id =sectionId + 1 )
                // you will get all user type 1 screen question.


                    if (pwdList.size > 0 && pwdList[0].is_saved!!) {
                        // you can next.. (change list to Array list for pass one to another)
                        val parsinglist = ArrayList<SectionWise.SectionWiseModel>()
                        val arraylist: ArrayList<Pwd> = ArrayList(pwdList.size) // change list to array list
                        arraylist.addAll(pwdList)

                        // convert row data from database to actual array list.

                        for (i in 0.until(arraylist.size)) {

                            val type = object : TypeToken<ArrayList<Answers>>() {}.type
                            val answer: ArrayList<Answers> = Gson().fromJson(arraylist[i].answer, type)
                            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                            for (j in 0.until(answer.size)) {
                                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                            }

                            parsinglist.add(SectionWise.SectionWiseModel(section_id = arraylist[i].section_id, section_name = arraylist[i].section_name, question = arraylist[i].question, question_id = arraylist[i].question_id, question_type = arraylist[i].question_type, answer = answerlist,is_pwd = arraylist[i].is_pwd))


                        }


                        val pwd_size = getPwdSize(sectionId + 1,databaseclient)

                        var connectedHeader=""
                        val pwd_list = databaseclient.pwddao().getQuestionbyFromSecId(user_type = user_type + 1, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),section_id = 2 )


                        if(pwd_list.size > 0){
                            val type = object : TypeToken<ArrayList<Answers>>() {}.type
                            val answer : ArrayList<Answers>  = Gson().fromJson(pwd_list[0].answer, type)
                            connectedHeader=answer[0].is_values.trim()
                        }



                        context!!.runOnUiThread {


                            if(connectedHeader.length > 0){
                                // if you did not enter pwd name then not go next

                                if(sectionId == 1 && (user_type + 1) == 1){
                                    loge("tag","if sectionId == 1 "+user_type)

                                    // add disable screen if you resume
                                    val adddisableuser= AddDisableUser.newInstance(0, 1)
                                    addFragment(R.id.seccontainer, adddisableuser, AddDisableUser.TAG, false)

                                    android.os.Handler().postDelayed({

                                        val fragment = SubSectionWise.newInstance(parsinglist, user_type + 1, parsinglist[0].section_name,sectionId + 1,p_section_id,if(connectedHeader.trim().length > 0) connectedHeader.substring(0, 1).toUpperCase() + connectedHeader.substring(1) else "")
                                        addFragment(R.id.seccontainer, fragment, SubSectionWise.TAG, false)

                                        nextSubQuestion (pwd_size,user_type + 1,databaseclient,sectionId + 1,p_section_id,totalsection)

                                    },500)


                                }else{
                                    loge("tag","else sectionId == 1 "+user_type)

                                    val fragment = SubSectionWise.newInstance(parsinglist, user_type + 1, parsinglist[0].section_name,sectionId + 1,p_section_id,if(connectedHeader.trim().length > 0) connectedHeader.substring(0, 1).toUpperCase() + connectedHeader.substring(1) else "")
                                    addFragment(R.id.seccontainer, fragment, SubSectionWise.TAG, false)

                                    nextSubQuestion (pwd_size,user_type + 1,databaseclient,sectionId + 1,p_section_id,totalsection)

                                }




                            }else{

                                if(sectionId == 1 && (user_type + 1) == 1){
                                    loge("tag","if sectionId == 1 with connectedHeader = 0 "+user_type)

                                    // add disable screen if you resume
                                    val adddisableuser= AddDisableUser.newInstance(0, 1)
                                    addFragment(R.id.seccontainer, adddisableuser, AddDisableUser.TAG, false)

                                }else{
                                    // if any field is empty on disable screen: pop other fragment
                                    // close progress dialog
                                    // show alert


                                    for (i in 0 until childFragmentManager.backStackEntryCount - 1) {
                                        childFragmentManager.popBackStack()
                                    }

                                    DialogUtils.openDialog(context = context!!, btnNegative = "", btnPositive = getString(R.string.ok), color = ContextCompat.getColor(context!!, R.color.theme_color), msg = "Please fill the all details first", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                                        override fun onPositiveButtonClick(position: Int) {
                                        }

                                        override fun onNegativeButtonClick() {
                                        }
                                    })


                                }

                                showProgressDialog()
                            }


                        }

                    }else{
                        // Add +1 question more

                     /*   context!!.runOnUiThread {
                            showProgressDialog()

                        }*/
                        Log.e("tag"," Add +1 question more"+sectionId+" "+totalsection)


                        if(pwdList.size > 0 ){
                            // add ++1 sub section

                            val parsinglist = ArrayList<SectionWise.SectionWiseModel>()
                            val arraylist: ArrayList<Pwd> = ArrayList(pwdList.size) // change list to array list
                            arraylist.addAll(pwdList)

                            Log.e("Next sub section - ",""+pwdList.size)

                            // convert row data from database to actual array list.

                            for (i in 0.until(arraylist.size)) {

                                val type = object : TypeToken<ArrayList<Answers>>() {}.type
                                val answer: ArrayList<Answers> = Gson().fromJson(arraylist[i].answer, type)
                                val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

                                for (j in 0.until(answer.size)) {
                                    answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
                                }

                                parsinglist.add(SectionWise.SectionWiseModel(section_id = arraylist[i].section_id, section_name = arraylist[i].section_name, question = arraylist[i].question, question_id = arraylist[i].question_id, question_type = arraylist[i].question_type, answer = answerlist,is_pwd = arraylist[i].is_pwd))


                            }


                            val pwd_size = getPwdSize(sectionId + 1,databaseclient)

                            var connectedHeader=""
                            val pwd_list = databaseclient.pwddao().getQuestionbyFromSecId(user_type = user_type + 1, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),section_id = 2 )
                            if(pwd_list.size > 0){
                                val type = object : TypeToken<ArrayList<Answers>>() {}.type
                                val answer : ArrayList<Answers>  = Gson().fromJson(pwd_list[0].answer, type)
                                connectedHeader=answer[0].is_values.trim()
                            }

                            context!!.runOnUiThread {

                                if(connectedHeader.length > 0){
                                    // if you did not enter pwd name then not go next
                                    if(sectionId == 1 && (user_type + 1) == 1 ){
                                        loge("tag","IF Add +1  sectionId == 1 "+user_type)


                                        // add disable screen if you resume
                                        val adddisableuser= AddDisableUser.newInstance(0, 1)
                                        addFragment(R.id.seccontainer, adddisableuser, AddDisableUser.TAG, false)

                                        android.os.Handler().postDelayed({

                                            val fragment = SubSectionWise.newInstance(parsinglist, user_type + 1, parsinglist[0].section_name,sectionId + 1,p_section_id,if(connectedHeader.trim().length > 0) connectedHeader.substring(0, 1).toUpperCase() + connectedHeader.substring(1) else "")
                                            addFragment(R.id.seccontainer, fragment, SubSectionWise.TAG, false)
                                            //  nextSubQuestion (pwd_size,user_type + 1,databaseclient,sectionId + 1,p_section_id,totalsection)

                                        },500)

                                    }else{

                                        val fragment = SubSectionWise.newInstance(parsinglist, user_type + 1, parsinglist[0].section_name,sectionId + 1,p_section_id,if(connectedHeader.trim().length > 0) connectedHeader.substring(0, 1).toUpperCase() + connectedHeader.substring(1) else "")
                                        addFragment(R.id.seccontainer, fragment, SubSectionWise.TAG, false)
                                        //  nextSubQuestion (pwd_size,user_type + 1,databaseclient,sectionId + 1,p_section_id,totalsection)
                                    }



                                }
                                else{

                                    if(sectionId == 1 && (user_type + 1) == 1 ){
                                        loge("tag","else Add +1  connectedHeader = 0 "+user_type)

                                        // add disable screen if you resume
                                        val adddisableuser= AddDisableUser.newInstance(0, 1)
                                        addFragment(R.id.seccontainer, adddisableuser, AddDisableUser.TAG, false)
                                    }
                                    else{
                                        // if any field is empty on disable screen: pop other fragment
                                        // close progress dialog
                                        // show alert

                                        for (i in 0 until childFragmentManager.backStackEntryCount - 1) {
                                            childFragmentManager.popBackStack()
                                        }
                                        DialogUtils.openDialog(context = context!!, btnNegative = "", btnPositive = getString(R.string.ok), color = ContextCompat.getColor(context!!, R.color.theme_color), msg = "Please fill the all details first", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                                            override fun onPositiveButtonClick(position: Int) {
                                            }

                                            override fun onNegativeButtonClick() {
                                            }
                                        })

                                    }
                                }

                                showProgressDialog()

                            }


                        }else{
                            // add ++1  only section
                            // This is function for add section : condition if section is not present in answer table then i will add for resume
                            var loop_totalsection = totalsection + 1  // start with 7

                            while ( 8 >= loop_totalsection){
                                val listFromAnswered = databaseclient.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id =loop_totalsection)
                                Log.e("Next Only section - ",""+listFromAnswered.size)
                                if(listFromAnswered.size <= 0){
                                    context!!.runOnUiThread {
                                        (parentFragment as QuestionAnswer).addQuestion(loop_totalsection - 1)

                                    }
                                    break
                                }

                                loop_totalsection = loop_totalsection + 1
                            }


                            context!!.runOnUiThread {
                                showProgressDialog()
                            }





                        }






                    }

        }
    }

    private fun nextSubQuestion (pwd_size : Int , user_type: Int, databaseclient: AppDatabase, sectionId: Int,p_section_id: Int,totalsection : Int){
        // default section id =2  p_section =1 , usertype =1


        if(sectionId < totalsection ){

            if(sectionId == 3 && HostActivity.statusofAge!![user_type]!! <= 14){

                makeSubEditQuestion(user_type - 1 ,databaseclient,sectionId + 1 , p_section_id)

            }else{

                makeSubEditQuestion(user_type - 1 ,databaseclient,sectionId , p_section_id)
            }

        }else{
            makeSubEditQuestion(user_type , databaseclient,p_section_id ,p_section_id)

        }


    }


    private fun getPwdSize(sectionId : Int , databaseclient: AppDatabase) : Int {

        var result = 0
        var user_type : Int = 1

        while (databaseclient.pwddao().getQuestionbyFormId(user_type =user_type , form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id = sectionId).size > 0){
            result = result + 1
            user_type = user_type + 1
        }

        return result

    }



    protected fun moveOnEditQuestion(sectionlist: List<Answer>, sectionId: Int, databaseclient: AppDatabase) {

        // you can resume.


        val parsinglist = ArrayList<SectionWise.SectionWiseModel>()
        val arraylist: ArrayList<Answer> = ArrayList(sectionlist.size) // change list to array list
        arraylist.addAll(sectionlist)

        // convert row data from database to actual array list.
        for (i in 0.until(arraylist.size)) {

            val data = arraylist[i]

            val type = object : TypeToken<ArrayList<Answers>>() {}.type
            val answer: ArrayList<Answers> = Gson().fromJson(data.answer, type)
            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

            for (j in 0.until(answer.size)) {
                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
            }
            parsinglist.add(SectionWise.SectionWiseModel(section_id = data.section_id, section_name = data.section_name, question = data.question, question_id = data.question_id, question_type = data.question_type, answer = answerlist,is_pwd = data.is_pwd))

        }



        val fragment = SectionWise.newInstance(parsinglist, sectionId , parsinglist[0].section_name)
        addFragment(R.id.qacontainer, fragment, SectionWise.TAG, false)




    }


}