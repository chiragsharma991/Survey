package com.softtech360.totalservey.fragment


import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.adapter.universal.statuswise.StatusParentAdapter
import com.softtech360.totalservey.databinding.SectionwiseBinding
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.OnBackPressedListener
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.sectionwise.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.runOnUiThread
import java.io.Serializable
import kotlin.concurrent.thread


class SectionWise : RessumeQuestion(), OnBackPressedListener {


    var sectionId: Int? = null
    var section_name: String = ""
    var list: ArrayList<SectionWiseModel>? = null
    var error_contmsg = ""
    var total_member = "0"
    var total_farmland: Long = 0


    //  val span1 = SpannableString(if (sectionwise is SubSectionWise && list[position].question_id != 6) list[0].answer[0].is_values.trim() + " " else "")
    //   val span2 = SpannableString(list[position].question)
    //  builder.append(span1).append(span2)


    companion object {

        val TAG = "SectionWise"
        fun newInstance(list: ArrayList<SectionWiseModel>, sectionId: Int, sectionName: String): SectionWise {
            val fragment = SectionWise()
            val bundle = Bundle()
            bundle.putSerializable("QUESTION", list)
            bundle.putInt("SECTIONID", sectionId)
            bundle.putString("SECTIONNAME", sectionName)
            fragment.arguments = bundle
            return fragment
        }

    }


    override fun onBackPressed() {
        loge(TAG, "onBackPressed---SectionWise")

        if (this.childFragmentManager.backStackEntryCount > 0) {
            loge(TAG, "onBackPressed---SectionWise-subsectionwise-" + childFragmentManager.backStackEntryCount)
            val currentFragment = this.childFragmentManager.getFragments().get(0)
            if (currentFragment is OnBackPressedListener) {
                (currentFragment as OnBackPressedListener).onBackPressed() //pass into current fragment.
            }
        } else {
            if ((parentFragment as QuestionAnswer).childFragmentManager.backStackEntryCount == 1) // if only present one then finish activity also
                (parentFragment as QuestionAnswer).finishThisActivity()
            else
                (parentFragment as QuestionAnswer).pop()
        }
    }


    override fun getLayout(): Int {
        return R.layout.sectionwise

    }


    private lateinit var binding: SectionwiseBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loge(TAG, "onCreateView---")
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


    private var databaseclient: AppDatabase? = null
    lateinit var mAdapter: StatusParentAdapter<SectionWise>
    private var isAnyPwdQuestion: Boolean = false
    private var myclickhandler: MyClickHandler? = MyClickHandler(this)

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loge(TAG, "saveInstance NULL")
            databaseclient = DatabaseClient.getInstance(activity!!.applicationContext).getappDatabase()
            binding.onclick = myclickhandler

            list = arguments!!.getSerializable("QUESTION") as ArrayList<SectionWiseModel>
            sectionId = arguments!!.getInt("SECTIONID")
            section_name = arguments!!.getString("SECTIONNAME")!!

            onlytxt_toolbar.text = section_name
            nxt_btn.text= if(sectionId == 8) "Save" else "Next"
            Log.e(TAG, "oncreate -section id - " + sectionId!!)
            refresh()

            //check if question type is PWD
            checkPwdQuestion()

/*            for (value in list!!){
                if(value.is_question_pwd){
                    isAnyPwdQuestion= true
                    break
                }
            }*/


            // Make resume function.

            //  if(HostActivity.questionIsResume && isAnyPwdQuestion){
            // you are coming from resume function so add nested fragment.


            /*      var personCount = 0
                  for (value in list!!){
                      if(value.question_id == 2){
                          personCount =value.answer[0].is_values.trim().toInt() // check if 0 then not go for it.
                          break
                      }
                  }
                  // do not make sub question if user entered 0 already
                  loge(TAG,"Person count "+personCount)
                  if(personCount > 0){
                      val fragment = AddDisableUser.newInstance(0)
                      addFragment(R.id.seccontainer, fragment, AddDisableUser.TAG, false)
                      makeSubEditQuestion(0,databaseclient)
                  }

                  HostActivity.questionIsResume= false  // this is function that you have to off in PWD function only because this is replace fragment and call again and again if you not do false.


                  */


            //   }


        } else {
            loge(TAG, "saveInstance Not NULL")


        }

    }


    private fun getPwdSize(sectionId: Int): Int {

        var result = 0
        var user_type: Int = 1

        while (databaseclient!!.pwddao().getQuestionbyFormId(user_type = user_type, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = sectionId).size > 0) {
            result = result + 1
            user_type = user_type + 1
        }

        return result

    }

    private fun checkPwdQuestion() {

        thread {

            loge(TAG, "section id : " + sectionId)
            val listFromQuestion = databaseclient!!.questiondao().getQuestionFromSection(section_id = sectionId!! + 1)

            if (listFromQuestion.size > 0 && listFromQuestion[0].is_pwd == 1) {
                isAnyPwdQuestion = true
            } else {
                isAnyPwdQuestion = false
            }


            personCount = getPwdSize(2) // get pwd value


            context!!.runOnUiThread {

                loge(TAG, "resume function " + HostActivity.questionIsResume + " - " + isAnyPwdQuestion)

                if (HostActivity.questionIsResume && isAnyPwdQuestion) {
                    // you are coming from resume function so add nested fragment.
                    HostActivity.questionIsResume = false  // this is function that you have to off in PWD function only because this is replace fragment and call again and again if you not do false.

                    if (personCount > 0) {
                        /*   if(sectionId!! == 1){
                               val fragment = AddDisableUser.newInstance(0,sectionId!!)
                               addFragment(R.id.seccontainer, fragment, AddDisableUser.TAG, false)
                           }*/
                        showProgressDialog()

                        makeSubEditQuestion(0, databaseclient!!, sectionId!!, sectionId!!)
                    }


                }

            }


        }

    }


    private var personCount = 0

    private fun createPwdQuestion() {

        thread {


            if (sectionId == 1) {
                // start from user disable.
                personCount = list!![1].answer[0].is_values.trim().toInt()
                startFromUserDisableScreen()


            } else {
                // start from subsection screen.
                personCount = getPwdSize(2) // in this case both wil be same, user cant edit after section 2
                startFromUserDisableScreen()
            }


        }

    }

    private fun startFromUserDisableScreen() {

        var sectionId_ = sectionId!! + 1

        // section should start from 2
        while (databaseclient!!.questiondao().getQuestionFromSection(section_id = sectionId_)[0].is_pwd == 1) {

            // loop acording section id:

            //val pwd_size = databaseclient!!.pwddao().getQuestionusingFormNsec(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id = sectionId_).size
            val pwd_size = getPwdSize(sectionId_)


            if (personCount == 0) {
                loge(TAG, "delete all entries --- ")
                databaseclient!!.pwddao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))

            } else {

                if (personCount == pwd_size) {
                    // No need to create entries
                    loge(TAG, "No need to create entries---")

                    /*   context!!.runOnUiThread {

                           addDisableUser(0)
                       }*/

                } else if (personCount > pwd_size) {

                    loge(TAG, "Add data from last row---")

                    // add data from last row

                    // get the all question for next section.
                    val list = databaseclient!!.questiondao().getQuestionFromSection(section_id = sectionId_)

                    // pass the loop data in another pwd table.
                    val loopvalue = personCount - pwd_size

                    for (j in 0.until(loopvalue)) {
                        // add i > for one screen

                        for (k in 0.until(list.size)) {

                            databaseclient!!.pwddao().insert(
                                    Pwd(serial_number = null,
                                            form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                                            user_type = pwd_size + 1 + j,
                                            section_id = sectionId_,
                                            p_section_id = sectionId!!,
                                            section_name = list[k].section_name,
                                            question_id = list[k].question_id,
                                            question_type = list[k].question_type,
                                            question = list[k].question,
                                            is_saved = false,
                                            answer = list[k].answer,
                                            is_pwd = list[k].is_pwd
                                    )
                            )

                        }
                    }

                    databaseclient!!.answerdao().deleteEntry(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id = 7)
                    databaseclient!!.answerdao().deleteEntry(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),section_id = 8)

                    /*   context!!.runOnUiThread {

                           addDisableUser(0)
                       }*/





                } else if (personCount < pwd_size) {
                    loge(TAG, "delete data from last row---")

                    // delete data from last row.


                    // pass the loop data in another pwd table.
                    var j: Int = pwd_size
                    while (personCount != j) {
                        // delete last entry according to person count
                        databaseclient!!.pwddao().deleteFormbyUsertype(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), user_type = j, section_id = sectionId_)
                        j = j - 1
                    }


                    /*  context!!.runOnUiThread {

                          addDisableUser(0)
                      }*/


                }


            }

            sectionId_ += 1
        }




        loge(TAG, "final-----" + sectionId_)
        if (personCount != 0) // if loop is break then not goes in this condition.
            addDisableUser(0)
        else
            (parentFragment as QuestionAnswer).addQuestion(sectionId_ - 1) // -1 according to while loop


    }


    fun addDisableUser(user_type: Int) {

        thread {

            if (personCount == user_type) {
                // sub question has been end..
                (parentFragment as QuestionAnswer).addQuestion(sectionId!! + 1) // 1+ loop section 1 =2 then  send

            } else {
                context!!.runOnUiThread {

                    if (sectionId == 1) {
                        val fragment = AddDisableUser.newInstance(user_type, sectionId!!)
                        addFragment(R.id.seccontainer, fragment, AddDisableUser.TAG, false)
                    } else {

                        addSubQuestion(0, sectionId!!)
                    }

                }

            }

        }

    }

    var parsinglist: ArrayList<SectionWiseModel>? = null

    fun addSubQuestion(user_type: Int, sectionId_: Int) {

        thread {
            loge(TAG, "addSubQuestion-" + user_type + " - " + sectionId_ + " person- " + personCount)

            if (personCount == user_type) {
                loge(TAG, "addMainsection--")

                // sub question has been end..
                val totalsection = databaseclient!!.pwddao().getlastSectionid(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), p_section_id = 1)
                // get total last added sub section and pass into section. 6 goes to 7.
                (parentFragment as QuestionAnswer).addQuestion(totalsection) // 1+ loop section 1 =2 then  send

            } else {

                // sub question is running.
                val list = databaseclient!!.pwddao().getQuestionbyFormId(user_type = user_type + 1, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = sectionId_ + 1)

                if (list.size > 0) {
                    // you can next.. (change list to Array list for pass one to another)
                    parsinglist = ArrayList()
                    val arraylist: ArrayList<Pwd> = ArrayList(list.size) // change list to array list
                    arraylist.addAll(list)
                    convertlisttoArray(arraylist) // convert row data from database to actual array list.

                    var connectedHeader = ""
                    val pwd_list = databaseclient!!.pwddao().getQuestionbyFromSecId(user_type = user_type + 1, form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = 2)
                    if (pwd_list.size > 0) {
                        val type = object : TypeToken<ArrayList<Answers>>() {}.type
                        val answer: ArrayList<Answers> = Gson().fromJson(pwd_list[0].answer, type)
                        // connectedHeader="(${user_type + 1}/${answer[0].is_values})"
                        connectedHeader = answer[0].is_values.trim()
                    }

                    context!!.runOnUiThread {

                        if(connectedHeader.length > 0){
                            val fragment = SubSectionWise.newInstance(parsinglist!!, user_type + 1, parsinglist!![0].section_name, sectionId_ + 1, sectionId!!,if(connectedHeader.trim().length > 0)  connectedHeader.substring(0, 1).toUpperCase() + connectedHeader.substring(1) else "")
                            addFragment(R.id.seccontainer, fragment, SubSectionWise.TAG, false)
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

                } else {
                    // section has been end..

                    /*      context!!.runOnUiThread{
                              Toast.makeText(context,"No question is match..", Toast.LENGTH_SHORT).show()
                          }*/

                    (parentFragment as QuestionAnswer).addQuestion(sectionId_)
                }


            }


        }


    }




    private fun convertlisttoArray(question: ArrayList<Pwd>) {

        for (i in 0.until(question.size)) {

            val type = object : TypeToken<ArrayList<Answers>>() {}.type
            val answer: ArrayList<Answers> = Gson().fromJson(question[i].answer, type)
            val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

            for (j in 0.until(answer.size)) {
                answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
            }

            parsinglist!!.add(SectionWise.SectionWiseModel(section_id = question[i].section_id, section_name = question[i].section_name, question = question[i].question, question_id = question[i].question_id, question_type = question[i].question_type, answer = answerlist, is_pwd = question[i].is_pwd))

            // answerlist.clear()

        }
    }

    private fun refresh() {

        mAdapter = StatusParentAdapter(context!!, list!!, this@SectionWise, 0, "")
        recyclerview_section.layoutManager = LinearLayoutManager(getActivityBase())
        (recyclerview_section.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)
        recyclerview_section.adapter = mAdapter

        //  questiontitle.text= list!![0].question

    }


    data class SectionWiseModel(

            val section_id: Int,
            val section_name: String,
            val question_id: Int,
            val is_pwd: Int,
            val question_type: Int,
            val is_question_pwd: Boolean = false,
            val question: String,
            val user_type: Int = 0,
            var view_hide_is: Boolean = false,
            var is_any_error: Boolean = false,
            var is_saved: Boolean = false,
            val answer: ArrayList<SectionWiseAnswerModel>

    ) : Serializable

    data class SectionWiseAnswerModel(

            val answer_id: Int,
            val question_id: Int,
            val answer: String,
            var is_selected: Boolean = false,
            var is_values: String = ""

    ) : Serializable


    fun checkValidation(): Boolean {

//         TYPE_EDTTEXT = 1
//         TYPE_EDITNUM = 2
//         TYPE_RADIO = 3
//         TYPE_CHECK = 4
//         TYPE_OPTIONAL = 5
       // return true
        error_contmsg = ""
        if (list == null) return false

        for (i in 0.until(list!!.size)) {

            if (list!![i].view_hide_is) {
                // bypass acording to conditional question.
                list!![i].is_any_error = false
                continue
            }


            // if someone get false , result has been false
            if (!AnswerCheck(list!![i].answer, list!![i].question_type, list!![i].question_id)) {
                return domark_error()
            } else {
                list!![i].is_any_error = false
            }


        }

        mAdapter.notifyDataSetChanged()

        return true

    }


    private fun domark_error(): Boolean {

        for (i in 0.until(list!!.size)) {

            if (!domark_check(list!![i].answer, list!![i].question_type, list!![i].question_id)) {
                list!![i].is_any_error = true

            } else {
                list!![i].is_any_error = false

            }
        }

        mAdapter.notifyDataSetChanged()

        return false

    }


    private fun domark_check(list: ArrayList<SectionWise.SectionWiseAnswerModel>, question_type: Int, question_id: Int): Boolean {

        when (question_type) {
            1, 2 -> {
                // true if all are edit text have atleast one values
                for (value in list) {

                    if (question_id == 1) {
                        total_member = value.is_values.trim()
                        if (value.is_values.trim().length <= 0 || value.is_values.trim().toInt() == 0 || value.is_values.trim().toInt() > 99) {
                            error_contmsg =if(value.is_values.trim().length <= 0 ) "" else  error_contmsg + "\n\n- कुल परिवार के सदस्य should be greaterthen 0 or lessthen 2 digit"
                            return false
                        }
                    } else if (question_id == 2) {
                        if (value.is_values.trim().length <= 0 || value.is_values.trim().toInt() > total_member.toInt()) {
                            error_contmsg =if(value.is_values.trim().length <= 0 ) "" else error_contmsg + "\n\n- निःशक्तता वाले कुल सदस्य should be 0 or less then to कुल परिवार के सदस्य"
                            return false
                        }

                    }

                    else if (question_id == 61) {
                        if (value.is_values.trim().length <= 0 || value.is_values.trim().toLong() > total_farmland) {
                            error_contmsg = if(value.is_values.trim().length > 0) error_contmsg+"\n\n- सिंचित क्षेत्र should not be greater to कुल कृषि क्षेत्र" else ""
                            return false
                        }
                    }

                    else {
                        if (value.is_values.trim().length <= 0) {
                            return false
                        }
                    }
                }
                return true

            }
            3, 4, 5 -> {
                // true if all check/radio have atleast one select
                for (value in list) {

                    if (value.is_selected) {
                        return true
                    }
                }
                return false
            }
            else -> {
                return false
            }
        }
    }


    fun AnswerCheck(list: ArrayList<SectionWise.SectionWiseAnswerModel>, question_type: Int, question_id: Int): Boolean {

        when (question_type) {
            1, 2 -> {
                // true if all are edit text have atleast one values
                for (value in list) {

                    if (question_id == 1) {
                        total_member = value.is_values.trim()
                        if (value.is_values.trim().length <= 0 || value.is_values.trim().toInt() == 0 || value.is_values.trim().toInt() > 99) {
                            //  error_contmsg = error_contmsg+"\n\n- कुल परिवार के सदस्य should be greaterthen 0 or lessthen 2 digit"
                            return false
                        }
                    } else if (question_id == 2) {
                        if (value.is_values.trim().length <= 0 || value.is_values.trim().toInt() > total_member.toInt()) {
                            // error_contmsg = error_contmsg+"\n\n- निःशक्तता वाले कुल सदस्य should be 0 or less then to कुल परिवार के सदस्य"
                            return false
                        }
                    } else if (question_id == 60) {
                        // you can get values of total fram land from list view.
                        if (value.is_values.trim().length > 0){
                            total_farmland = value.is_values.trim().toLong()
                            Log.e("total_farmland--",""+total_farmland )

                            return true
                        }else{
                            Log.e("total_farmland--",""+total_farmland )

                            return false
                        }


                    } else if (question_id == 61) {
                        if (value.is_values.trim().length <= 0 ||  value.is_values.trim().toLong() > total_farmland) {
                            // error_contmsg = error_contmsg+"\n\n- निःशक्तता वाले कुल सदस्य should be 0 or less then to कुल परिवार के सदस्य"
                            return false
                        }
                    } else {
                        if (value.is_values.trim().length <= 0) {
                            return false
                        }
                    }
                }
                return true

            }
            3, 4, 5 -> {
                // true if all check/radio have atleast one select
                for (value in list) {

                    if (value.is_selected) {
                        return true
                    }
                }
                return false
            }
            else -> {
                return false
            }
        }


    }

    private fun submitAnswer(list: ArrayList<SectionWiseModel>) {

        thread {

            val result = databaseclient!!.answerdao().getAnswerbySecId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), section_id = sectionId!!)

            if (result.size > 0) {
                loge(TAG, "data is already present--")
                // data is already present.
                for (value in list) {
                    databaseclient!!.answerdao().update(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                            section_id = value.section_id, answer = Gson().toJson(value.answer), question_id = value.question_id)
                }


            } else {
                loge(TAG, "no data is match/present--")
                // no data is match/present.
                for (value in list) {

                    databaseclient!!.answerdao().insert(
                            Answer(serial_number = null,
                                    form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                                    section_id = sectionId!!,
                                    section_name = section_name,
                                    question_id = value.question_id,
                                    question_type = value.question_type,
                                    question = value.question,
                                    answer = Gson().toJson(value.answer),
                                    is_pwd = value.is_pwd
                            )
                    )
                }
            }


        }

    }


    class MyClickHandler(val sectionwise: SectionWise) {


        fun onNext(view: View) {

            if (sectionwise.checkValidation()) {
                // (sectionwise.parentFragment as QuestionAnswer).addQuestion(sectionwise.setctionId!!)
                sectionwise.submitAnswer(sectionwise.list!!)

                if(sectionwise.sectionId == 8){
                    // save this form and exit.
                    DialogUtils.openDialog(context = sectionwise.context!!, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(sectionwise.context!!, R.color.theme_color), msg = "Please make sure that You can't edit this form after this submission, if you want to continue press yes", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                        override fun onPositiveButtonClick(position: Int) {

                            (sectionwise.parentFragment as QuestionAnswer).addQuestion(sectionwise.sectionId!!)

                        }
                        override fun onNegativeButtonClick() {
                        }
                    })

                }else{
                    if (sectionwise.isAnyPwdQuestion) {
                        // create nested PWD questions

                        sectionwise.createPwdQuestion()


                        //---------------**-----------


                    } else {
                        // No PWD question
                        (sectionwise.parentFragment as QuestionAnswer).addQuestion(sectionwise.sectionId!!)

                    }

                }


            } else {
                DialogUtils.openDialog(context = sectionwise.context!!, btnNegative = "", btnPositive = sectionwise.getString(R.string.ok), color = ContextCompat.getColor(sectionwise.context!!, R.color.theme_color), msg = "Please fill the all details first" + sectionwise.error_contmsg, title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                    override fun onPositiveButtonClick(position: Int) {
                    }

                    override fun onNegativeButtonClick() {
                    }
                })
            }



        }

        fun onCancel(view: View) {

            DialogUtils.openDialog(context = sectionwise.context!!, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(sectionwise.context!!, R.color.theme_color), msg = "Are you sure to go home?", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {
                    val intent = Intent()
                    sectionwise.activity!!.setResult(Activity.RESULT_OK, intent)
                    sectionwise.activity!!.finish()
                }

                override fun onNegativeButtonClick() {
                }
            })
        }

        fun onBack(view: View) {
            sectionwise.onBackPressed()
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