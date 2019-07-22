package com.softtech360.totalservey.fragment


import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.R
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.adapter.universal.statuswise.StatusParentAdapter
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

class SubSectionWise : BaseFragment(), OnBackPressedListener {


    var user_type: Int? = null
    var list: ArrayList<SectionWise.SectionWiseModel>? = null
    var section_name: String = ""
    var sectionId: Int? = null
    var p_sectionId: Int? = null
    var connectedHeader: String = ""
    var pwd_size: Int = 0
    var error_contmsg = ""


    companion object {

        val TAG = "SubSectionWise"
        fun newInstance(list: ArrayList<SectionWise.SectionWiseModel>, user_type: Int, sectionName: String, sectionId: Int, p_sectionId: Int, connectedHeader: String): SubSectionWise {

            val fragment = SubSectionWise()
            val bundle = Bundle()
            bundle.putSerializable("QUESTION", list)
            bundle.putInt("USER_TYPE", user_type)
            bundle.putString("SECTIONNAME", sectionName)
            bundle.putInt("SECTIONID", sectionId)
            bundle.putInt("P_SECTIONID", p_sectionId)
            bundle.putString("CONNECTEDHEADER", connectedHeader)
            fragment.arguments = bundle

            return fragment
        }

    }


    override fun onBackPressed() {
        loge(TAG, "onBackPressed--SubSectionWise-")
        (parentFragment as SectionWise).pop()
    }


    private fun setToolbar() {

        //onlytxt_toolbar.text = ""

    }


    override fun getLayout(): Int {
        return R.layout.subsectionwise

    }


    private lateinit var binding: SubsectionwiseBinding
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

            // list= ArrayList()
            binding.onclick = myclickhandler


            list = arguments!!.getSerializable("QUESTION") as ArrayList<SectionWise.SectionWiseModel>
            user_type = arguments!!.getInt("USER_TYPE")
            section_name = arguments!!.getString("SECTIONNAME")!!
            sectionId = arguments!!.getInt("SECTIONID")
            connectedHeader = arguments!!.getString("CONNECTEDHEADER")!!
            p_sectionId = arguments!!.getInt("P_SECTIONID")

//                        connectedHeader="(${user_type + 1}/${answer[0].is_values})"
            //  section_name=question[0].section_name

            onlytxt_toolbar.text = "${user_type}. ${connectedHeader} ${section_name}"

            thread {
                pwd_size = getPwdSize(sectionId!!)

            }

            //  convertlisttoArray(question)
            refresh()


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

    private fun refresh() {

        mAdapter = StatusParentAdapter(context!!, list!!, this, user_type!!,connectedHeader)
        recyclerview_subsection.layoutManager = LinearLayoutManager(getActivityBase())
       (recyclerview_subsection.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(false)
        recyclerview_subsection.adapter = mAdapter

        //  questiontitle.text= list!![0].question

    }


    /*   private fun convertlisttoArray(question : ArrayList<Pwd>){

           for (i in 0.until(question.size)){

               val type = object : TypeToken<ArrayList<Answers>>() {}.type
               val answer : ArrayList<Answers> = Gson().fromJson(question[i].answer, type)
               val answerlist = ArrayList<SectionWise.SectionWiseAnswerModel>()

               for (j in 0.until(answer.size)){
                   answerlist.add(SectionWise.SectionWiseAnswerModel(answer_id = answer[j].answer_id, answer = answer[j].answer, question_id = answer[j].question_id, is_selected = answer[j].is_selected, is_values = answer[j].is_values))
               }

               list!!.add(SectionWise.SectionWiseModel(question = question[i].question, question_id = question[i].question_id, question_type = question[i].question_type, answer = answerlist))
               loge(TAG,"list contain - "+list!![0].answer[0].is_values)

               // answerlist.clear()

           }
       }

   */

    fun checkValidation(): Boolean {


//         TYPE_EDTTEXT = 1
//         TYPE_EDITNUM = 2
//         TYPE_RADIO = 3
//         TYPE_CHECK = 4
//         TYPE_OPTIONAL = 5
        return  true
        error_contmsg = ""

        if (list == null) return false

        for (i in 0.until(list!!.size)) {
           // loge(TAG, "view hide is " + list!![i].view_hide_is + " " + i + " " + list!!.size)

            if (list!![i].view_hide_is) {
                // bypass acording to conditional question.
                loge(TAG,"hide pos-"+i)
                list!![i].is_any_error = false
                continue
            }

            // if someone get false , result has been false

            if (!AnswerCheck(list!![i].answer, list!![i].question_type, list!![i].question_id)) {
                loge(TAG,"AnswerCheck pos-"+i+" "+list!![i].question_id)

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
             //   loge(TAG, "error on --" + i)

            } else {
                list!![i].is_any_error = false

            }
        }

        mAdapter.notifyDataSetChanged()

        return false

    }

    fun domark_check(list: ArrayList<SectionWise.SectionWiseAnswerModel>, question_type: Int, question_id: Int): Boolean {

        when (question_type) {
            1, 2 -> {
                // true if all are edit text have atleast one values
                for (value in list) {

                    if (question_id == 7) {
                        // Adhar number (not mandatory but if present then check validation )

                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 16) {
                                return true
                            } else {
                                error_contmsg = error_contmsg + "\n\n- आधार नंबर should be 16 digit"
                                return false
                            }

                        }

                    } else if (question_id == 8) {
                        // mobile number
                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 10) {
                                return true
                            } else {
                                error_contmsg = error_contmsg + "\n\n- मोबाइल नंब should be 10 digit"
                                return false
                            }

                        }


                    } else if (question_id == 9) {
                        // whats up
                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 10) {
                                return true
                            } else {
                                error_contmsg = error_contmsg + "\n\n- व्हाट्सएप न should be 10 digit"
                                return false
                            }

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


    fun AnswerCheck(list: ArrayList<SectionWise.SectionWiseAnswerModel>, question_type: Int, question_id: Int): Boolean {

        when (question_type) {
            1, 2 -> {
                // true if all are edit text have atleast one values
                for (value in list) {

                    if (question_id == 7) {
                        // Adhar number (not mandatory but if present then check validation )

                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 16) {
                                return true
                            } else {
                             //   error_contmsg = error_contmsg + "\n\n- आधार नंबर should be 7 digit"
                                return false
                            }

                        }

                    } else if (question_id == 8) {
                        // mobile number
                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 10) {
                                return true
                            } else {
                             //   error_contmsg = error_contmsg + "\n\n- मोबाइल नंब should be 10 digit"
                                return false
                            }

                        }


                    } else if (question_id == 9) {
                        // whats up
                        if (value.is_values.trim().length <= 0) {
                            return true
                        } else {

                            if (value.is_values.trim().length == 10) {
                                return true
                            } else {
                                error_contmsg = error_contmsg + "\n\n- व्हाट्सएप न should be 10 digit"
                                return false
                            }

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
                    }else{
                        loge("AnswerCheck in for",""+value.answer_id+" "+value.question_id)

                    }
                }
                return false
            }
            else -> {

                return false
            }
        }


    }

    private fun submitAnswer(list: ArrayList<SectionWise.SectionWiseModel>) {

        thread {

            for (value in list) {
                databaseclient!!.pwddao().update(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                        user_type = user_type!!, answer = Gson().toJson(value.answer), question_id = value.question_id, is_saved = true)
            }

            val totalsection = databaseclient!!.pwddao().getlastSectionid(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0), p_section_id = p_sectionId!!)

            context!!.runOnUiThread {

                // default sectionid = 1 , total section = 7
                if (sectionId!! < totalsection) {

                    if (sectionId!! == 3 && HostActivity.statusofAge[user_type]!! <= 14) {

                        // if age is like this then we bypass one screen.

                        (parentFragment as SectionWise).addSubQuestion(user_type!! - 1, sectionId!! + 1)


                    } else {
                        (parentFragment as SectionWise).addSubQuestion(user_type!! - 1, sectionId!!)
                    }

                } else {

                    (parentFragment as SectionWise).addSubQuestion(user_type!!, p_sectionId!!)
                }


            }


        }

    }


    class MyClickHandler(val subsectionwise: SubSectionWise) {


        fun onNext(view: View) {

            if (subsectionwise.checkValidation()) {
                // (sectionwise.parentFragment as QuestionAnswer).addQuestion(sectionwise.setctionId!!)
                subsectionwise.submitAnswer(subsectionwise.list!!)

            } else {
                DialogUtils.openDialog(context = subsectionwise.context!!, btnNegative = "", btnPositive = subsectionwise.getString(R.string.ok), color = ContextCompat.getColor(subsectionwise.context!!, R.color.theme_color), msg = "Please fill the all details first"+subsectionwise.error_contmsg, title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                    override fun onPositiveButtonClick(position: Int) {
                    }

                    override fun onNegativeButtonClick() {
                    }
                })
            }

        }

        fun onCancel(view: View) {

            DialogUtils.openDialog(context = subsectionwise.context!!, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(subsectionwise.context!!, R.color.theme_color), msg = "Are you sure to go home?", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {
                    val intent = Intent()
                    subsectionwise.activity!!.setResult(Activity.RESULT_OK, intent)
                    subsectionwise.activity!!.finish()
                }

                override fun onNegativeButtonClick() {
                }
            })
        }

        fun onBack(view: View) {
            subsectionwise.onBackPressed()
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