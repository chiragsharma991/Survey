package com.softtech360.totalservey.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jakewharton.fliptables.FlipTableConverters
import com.softtech360.totalservey.R
import com.softtech360.totalservey.adapter.universal.RecyclerCallback
import com.softtech360.totalservey.adapter.universal.UniversalAdapter
import com.softtech360.totalservey.databinding.ActivityMainBinding
import com.softtech360.totalservey.databinding.RowStatusBinding
import com.softtech360.totalservey.fragment.BaseFragment
import com.softtech360.totalservey.model.Answers
import com.softtech360.totalservey.model.QuestionAnswer
import com.softtech360.totalservey.model.Questions
import com.softtech360.totalservey.rest.ApiCall
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.room.database.DatabaseClient
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.room.entity.Status
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import kotlin.concurrent.thread
import org.json.JSONObject
import com.google.gson.JsonArray
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.provider.Settings
import androidx.appcompat.widget.PopupMenu
import android.view.MenuItem
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.crashlytics.android.Crashlytics
import com.softtech360.totalservey.model.Data
import com.softtech360.totalservey.utils.Constants
import com.softtech360.totalservey.utils.MyAlarm
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity(), AnkoLogger, PopupMenu.OnMenuItemClickListener {


    private lateinit var mYourBroadcastReceiver: BroadcastReceiver


    companion object {
        val TAG = "MainActivity"
        fun newInstance(): MainActivity {
            return MainActivity()
        }
    }


    private var databaseclient: AppDatabase? = null
    private var is_online: Boolean = false

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView(savedInstanceState)

    }

    var count: Int = 0
    var version_name : String =""
    val myclickhandler: MyClickHandler = MyClickHandler(this)
    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {

            loge(MainActivity.TAG, "saveInstance Not NULL")
            more.visibility = View.VISIBLE
            // synch_btn.visibility=View.VISIBLE

            is_online = intent.getBooleanExtra("IS_ONLINE", false)
            binding.onclick = myclickhandler
            databaseclient = DatabaseClient
                    .getInstance(applicationContext)
                    .getappDatabase()

            try
            {
                val pInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
                val verCode = pInfo.versionCode
                version_name = pInfo.versionName
                val result = "v${verCode}_${version_name}"
                version_txt.text = String.format(getString(R.string.app_version),result)

            }
            catch (e:PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }



            more.setOnClickListener { view ->
                val popup = PopupMenu(this@MainActivity, view)
                popup.setOnMenuItemClickListener(this@MainActivity)
                popup.inflate(R.menu.menu_section)
                popup.show()
            }

            callLocationFunction()

            if (is_online) {
                // online : delete pre data and insert new data.
                loge(TAG, "online data--")
               // deleteAllTables(databaseclient!!)
                fetchQuestionAnswer()

            } else {
                // offline
                loge(TAG, "offline data--")
                refreshlist()
            }

/*
            if (!::mYourBroadcastReceiver.isInitialized) {

                mYourBroadcastReceiver = object : BroadcastReceiver(){
                    override fun onReceive(p0: Context?, p1: Intent?) {


                    }

                }

                LocalBroadcastManager.getInstance(this).registerReceiver(mYourBroadcastReceiver, IntentFilter(Constants.BROADCAST_TIMEOUT))

            }
*/




            } else {

            logd(MainActivity.TAG, "saveInstance Not NULL")

        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        when (item!!.itemId) {

            R.id.help -> {
                startActivity(Intent(this, Help::class.java))
                return true
            }
            R.id.logout -> {
                logOut()
                return true
            }
            R.id.exit -> {
                onBackPressed()
                return true
            }
        }

        return false
    }


    private fun syncAllRecords(start_form_id : Int) {


        if(!isInternetAvailable()){

            // No internet connection:
            clearProgressDialog()
            DialogUtils.openDialog(this@MainActivity, "Please check your internet connection and try again", "",
                    getString(R.string.ok), "", ContextCompat.getColor(this@MainActivity, R.color.black), object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(p: Int) {
                }

                override fun onNegativeButtonClick() {
                }
            })

           return
        }


        thread {
            loge(TAG,"syncAllRecords --")
            val totalform = databaseclient!!.statusdao().all

            if (totalform.size > 0) {

                // this is the condition if you don't want to go ahead where pending servey is present.
           /*     var result = true
                for (i in 0.until(totalform.size)) {

                    if (!totalform[i].submission_status!!) {
                        result = false
                    }
                }*/



                    // if all form are successfully submited.

                //    var start_form_id = 1
                    val end_form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 1)

                    if (start_form_id <= end_form_id) {


                        val statuslist = databaseclient!!.statusdao().getStatusListByformId(start_form_id)

                        //submission_status if true then go ahead another wise list is on pending
                        if (statuslist.size > 0 && statuslist[0].submission_status!!) {

                            val final_jsonObject = JsonObject() // final array

                            val user_jsonObject = JsonObject() // user object
                            user_jsonObject.addProperty("date_time", statuslist[0].date_time)
                            user_jsonObject.addProperty("completed_at", statuslist[0].completed_at)
                            user_jsonObject.addProperty("latitude", statuslist[0].latitude)
                            user_jsonObject.addProperty("longitude", statuslist[0].longitude)
                            user_jsonObject.addProperty("organization", PreferenceUtil.getString(PreferenceUtil.ORGANIZATION_ID, "0"))
                            user_jsonObject.addProperty("state", statuslist[0].state)
                            user_jsonObject.addProperty("district", statuslist[0].district)
                            user_jsonObject.addProperty("section", statuslist[0].section)
                            user_jsonObject.addProperty("village_council", statuslist[0].village_council)
                            user_jsonObject.addProperty("village", statuslist[0].village)
                            user_jsonObject.addProperty("colony", statuslist[0].colony)
                            user_jsonObject.addProperty("user_id", statuslist[0].user_id)

                            final_jsonObject.add("survey_data", user_jsonObject)
                            val question_jsonArray = Gson().fromJson(statuslist[0].question_answer, JsonArray::class.java)
                            final_jsonObject.add("question_answer", question_jsonArray)

                          //  start_form_id = start_form_id + 1

                            runOnUiThread {


                                SubmitCreateSurvey(final_jsonObject, start_form_id)

                            }



                        } else {
                            // continue the loop
                            //start_form_id = start_form_id + 1
                            syncAllRecords(start_form_id + 1)
                        }

                    } else{

                        runOnUiThread {
                            // this is call only when one pending form is present and all remaining form have been completed.
                            showProgressDialog()
                            PreferenceUtil.putValue(PreferenceUtil.INITIAL_TIME,getCurrentDate())
                            PreferenceUtil.save()
                            Toast.makeText(this, "Thank you! Record has been sucessfully pushed on server :)", Toast.LENGTH_SHORT).show()
                        }

                    }







            } else {

                runOnUiThread {
                    // this is call when all form have been succefully pushed then update table and version.
                    showProgressDialog()
                    PreferenceUtil.putValue(PreferenceUtil.INITIAL_TIME,getCurrentDate())
                    PreferenceUtil.save()
                    fetchQuestionAnswer()
                    Toast.makeText(this, "Thank you! Record has been sucessfully pushed on server :)", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun editAllRecords() {


        if (edit_btn.alpha == 0.9f && callLocationFunction()) {


            thread {

                val answerList = databaseclient!!.answerdao().getAnswerbyFormId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))

                runOnUiThread {

                    val intent = Intent(this@MainActivity, HostActivity::class.java)
                    intent.putExtra("RESUME", true)
                    startActivityForResult(intent, 1)

              /*      if (answerList.size > 0) {
                        val intent = Intent(this@MainActivity, HostActivity::class.java)
                        intent.putExtra("RESUME", true)
                        startActivityForResult(intent, 1)

                    } else {

                        val intent = Intent(this@MainActivity, NewUser::class.java)
                        startActivityForResult(intent, 1)
                    }*/
                }
            }
        }

    }

    private fun deleteCurrentRecord() {

        if (delete_edt.alpha == 0.2f ) { return }

        DialogUtils.openDialog(context = this@MainActivity, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(this@MainActivity, R.color.theme_color), msg = "Are you sure to delete?.", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
            override fun onPositiveButtonClick(position: Int) {
                thread {
                    databaseclient!!.statusdao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))
                    // delete entry form wise if data is saved in status table.
                    databaseclient!!.pwddao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))
                    // delete Answer form
                    databaseclient!!.answerdao().deleteFormbyId(form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0))

                    runOnUiThread {
                        // finally refresh the list.
                        refreshlist()

                    }
                }
            }

            override fun onNegativeButtonClick() {

            }
        })

    }



    private fun logOut() {

        DialogUtils.openDialog(context = this@MainActivity, btnNegative = "No", btnPositive = "Yes", color = ContextCompat.getColor(this@MainActivity, R.color.theme_color), msg = "You are going to logout", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
            override fun onPositiveButtonClick(position: Int) {
                PreferenceUtil.putValue(PreferenceUtil.ISLOGIN, false)
                PreferenceUtil.save()
                startActivity(Intent(this@MainActivity, Login::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }

            override fun onNegativeButtonClick() {
            }
        })

    }


    private fun callLocationFunction(): Boolean {

        if (is_location_PermissionGranted()) {

            if (isGpsEnable()) {
                // call get lat long
                getLocationWithoutInternet(databaseclient!!)
                return true

            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 2)
                return false
            }
        }

        return false

    }

    private var mAdapter: UniversalAdapter<Status, RowStatusBinding>? = null

    private fun refreshlist() {


        thread {

            val list: List<Status> = databaseclient!!.statusdao().all
            loge(TAG, "refresh--" + list.toString())

            val question: List<Question> = databaseclient!!.questiondao().all



            runOnUiThread {
//                ingredientview.subitem_name.text = String.format(getString(R.string.minues), list[i].removed_ingredients!!.get(j).ingredient_name)
                completed_survey.text=String.format(getString(R.string.total_completed_survey),PreferenceUtil.getString(PreferenceUtil.SURVEY_COUNT, "0")!!.trim())

                survey_count_txt.text = totalunsync_survey(list).toString()

                if(survey_count_txt.text.trim().toString() == "0"){
                    syncing_btn.alpha = 0.2f
                }else{
                    syncing_btn.alpha = 0.9f
                }


                if (caniDoNewservey(list) && question.size > 0) {
                    start_btn.alpha = 0.9f
                } else {
                    start_btn.alpha = 0.4f
                }

                // false means one is present but if it is true then it is stored in local
                var anypendinglist: Boolean = false
                for (i in 0.until(list.size)) {
                    if (!list[i].submission_status!!) {
                        anypendinglist = true
                    }

                }

                if (anypendinglist) {
                    // 1 pending list is present.
                    pending_survey_txt.text = "1 Pending survey"
                    edit_btn.alpha = 0.9f
                    delete_edt.alpha = 0.9f


                } else {
                    // 0 pending list is present.
                    pending_survey_txt.text = "0 Pending survey"
                    edit_btn.alpha = 0.2f
                    delete_edt.alpha = 0.2f


                }

            }


        }

    }

    private fun caniDoNewservey(list: List<Status>): Boolean {

        for (value in list) {
            if (value.submission_status!! == false)
                return false
        }


        return true

    }

    private fun totalunsync_survey (list: List<Status>): Int {

        var count = 0

        for (value in list) {
            if (value.submission_status!! && !value.synch_status!!)
                count = count+1
        }

        return count

    }


    private fun onstartNewservey() {



        if(PreferenceUtil.getBoolean(PreferenceUtil.UPDATE_VERSION_CODE,false)){

            DialogUtils.openDialog(context = this@MainActivity, btnNegative = "", btnPositive = "ok", color = ContextCompat.getColor(this@MainActivity, R.color.theme_color), msg = "Sorry Application version has been changed, Please checkout new version and try again", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {

                }
                override fun onNegativeButtonClick() {
                }
            })

            return
        }


        val simpledateformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val initial_time : Date  = simpledateformat.parse(PreferenceUtil.getString(PreferenceUtil.INITIAL_TIME,""))
        val current_time : Date = simpledateformat.parse(getCurrentDate())

        val diff : Long = current_time.getTime() - initial_time.getTime();
        val seconds : Long = diff / 1000;
        val minutes : Long = seconds / 60;
        val hours : Long = minutes / 60;
        val days : Long = hours / 24;
        // 2880 min for 48 hours

        //loge(TAG," Initial time - "+initial_time+" current "+current_time)

        //loge(TAG," difference ms - "+diff+" second "+seconds+" min "+minutes+" hour "+hours+" day "+days)


        if (start_btn.alpha == 0.4f) {
            return
        }

        if(survey_count_txt.text.trim().toString().toInt() >= 20){

            DialogUtils.openDialog(context = this@MainActivity, btnNegative = "", btnPositive = "ok", color = ContextCompat.getColor(this@MainActivity, R.color.theme_color), msg = "Please do sync the data before start the new survey", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {

                }
                override fun onNegativeButtonClick() {
                }
            })
        }
        else if(survey_count_txt.text.trim().toString().toInt() > 0 && minutes > 2880){

            DialogUtils.openDialog(context = this@MainActivity, btnNegative = "", btnPositive = "ok", color = ContextCompat.getColor(this@MainActivity, R.color.theme_color), msg = "Please do sync the data before start the new survey", title = "", onDialogClickListener = object : DialogUtils.OnDialogClickListener {
                override fun onPositiveButtonClick(position: Int) {

                }
                override fun onNegativeButtonClick() {
                }
            })
        }

        else{

            val intent = Intent(this, NewUser::class.java)
            startActivityForResult(intent, 1)

           /* thread {

                //  val list: List<Status> = databaseclient!!.statusdao().all
                //   val question: List<Question> = databaseclient!!.questiondao().all

                var form_id: Int = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0)
                form_id = form_id + 1
                loge(TAG, "form id - " + form_id)
                PreferenceUtil.putValue(PreferenceUtil.FORM_ID, form_id)
                PreferenceUtil.save()

                databaseclient!!.statusdao().insert(
                        Status(
                                serial_number = null,
                                form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID, 0),
                                user_id = PreferenceUtil.getInt(PreferenceUtil.USER_ID, 0),
                                organization = PreferenceUtil.getString(PreferenceUtil.ORGANIZATION_NAME, "")!!,
                                email = PreferenceUtil.getString(PreferenceUtil.EMAIL, "")!!,
                                submission_status = false
                        )
                )


                runOnUiThread {
                    val intent = Intent(this, NewUser::class.java)
                    startActivityForResult(intent, 1)
                }

            }*/

        }





//        startActivity(intentFor<NewUser>())


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loge(TAG, "onActivityResult -- "+requestCode+" - "+resultCode)

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                refreshlist()
            }else if(resultCode == 3){
                refreshlist()
                showProgressDialog()
                Handler().postDelayed({
                    onstartNewservey()
                    showProgressDialog()
                },3000)
            }


        } else if (requestCode == 2) {
            // call get lat long
            if (resultCode == Activity.RESULT_OK) {
                getLocationWithoutInternet(databaseclient!!)
            }

        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {


        loge(TAG, "permission result---")
        when (requestCode) {
            1 -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    loge(TAG, "location permission accepted---")
                    if (isGpsEnable()) {
                        // call get lat long
                        getLocationWithoutInternet(databaseclient!!)

                    } else {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent, 2)
                    }

                } else {
                    Toast.makeText(this, "Please check the permission setting.", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }


    private fun fetchQuestionAnswer() {

        showProgressDialog()
        callAPI(ApiCall.getQuestionAnswer(),
                object : BaseFragment.OnApiCallInteraction {

                    override fun <T> onSuccess(body: T?) {
                        showProgressDialog()
                        val response = body as JsonObject
                        val questionAnswer = GsonBuilder().create().fromJson(response.toString(), QuestionAnswer::class.java)
                        // check version then changed
                        if(questionAnswer.data.app_version!!.trim().toDouble() > version_name.trim().toString().toDouble()){
                            // new version is greater
                            PreferenceUtil.putValue(PreferenceUtil.UPDATE_VERSION_CODE,true)
                            PreferenceUtil.save()

                        }else{
                            // version is ok
                            PreferenceUtil.putValue(PreferenceUtil.UPDATE_VERSION_CODE,false)
                            PreferenceUtil.save()

                            passdataInTable(questionAnswer)
                        }

/*
                        Thread{

                            deleteAllTables(databaseclient!!)
                            runOnUiThread(){
                                passdataInTable(questionAnswer)
                            }

                        }*/

                    }

                    override fun onFail(error: Int) {
                        showProgressDialog()
                        when (error) {
                            404 -> {
                                DialogUtils.openDialog(this@MainActivity, getString(R.string.error_404), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@MainActivity, R.color.black), object : DialogUtils.OnDialogClickListener {
                                    override fun onPositiveButtonClick(p: Int) {
                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                })

                            }
                            100 -> {

                                DialogUtils.openDialog(this@MainActivity, getString(R.string.internet_not_available), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@MainActivity, R.color.black), object : DialogUtils.OnDialogClickListener {
                                    override fun onPositiveButtonClick(p: Int) {
                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                })
                            }
                        }

                    }
                })
    }


    private fun SubmitCreateSurvey(jsonObject: JsonObject, form_id: Int): Boolean {
        var result = false
        //showProgressDialog()
        loge(TAG,"SubmitCreateSurvey --")


        callAPI(ApiCall.createSurvey(jsonObject),
                object : BaseFragment.OnApiCallInteraction {

                    override fun <T> onSuccess(body: T?) {
                      //  showProgressDialog()

                        val response = body as JsonObject
                        loge(TAG, "response - " + response + " " + form_id)
                        //{"status":true,"data":"Survey is successfully updated","survey_count":"8"}
                        PreferenceUtil.putValue(PreferenceUtil.SURVEY_COUNT, response.get("survey_count").asString)
                        PreferenceUtil.save()

                        thread {
                            databaseclient!!.statusdao().deleteFormbyId(form_id = form_id)
                            runOnUiThread {
                                // finally refresh the list.
                                refreshlist()
                                syncAllRecords(form_id + 1)

                            }
                        }



                        result = true

                    }

                    override fun onFail(error: Int) {
                     //   showProgressDialog()
                        //   showProgressDialog()
                        /*      when (error) {
                                      404 -> {
                                      DialogUtils.openDialog(this@MainActivity, getString(R.string.error_404), "",
                                              getString(R.string.ok), "", ContextCompat.getColor(this@MainActivity, R.color.black), object : DialogUtils.OnDialogClickListener {
                                          override fun onPositiveButtonClick(p: Int) {
                                          }

                                          override fun onNegativeButtonClick() {
                                          }
                                      })

                                  }
                                  100 -> {

                                      DialogUtils.openDialog(this@MainActivity, getString(R.string.internet_not_available), "",
                                              getString(R.string.ok), "", ContextCompat.getColor(this@MainActivity, R.color.black), object : DialogUtils.OnDialogClickListener {
                                          override fun onPositiveButtonClick(p: Int) {
                                          }

                                          override fun onNegativeButtonClick() {
                                          }
                                      })
                                  }
                              }
      */
                    }
                })

        return result
    }


    private fun passdataInTable(questionAnswer: QuestionAnswer) {

        /*  PreferenceUtil.putValue(PreferenceUtil.USER_STATES, Gson().toJson(questionAnswer.data.user_states))
          PreferenceUtil.putValue(PreferenceUtil.USER_DISTRICTS, Gson().toJson(questionAnswer.data.user_districts))
          PreferenceUtil.putValue(PreferenceUtil.USER_SECTIONS, Gson().toJson(questionAnswer.data.user_sections))
          PreferenceUtil.putValue(PreferenceUtil.USER_VILLAGE_COUNCIL, Gson().toJson(questionAnswer.data.user_village_council))
          PreferenceUtil.save()*/


        // pass Question data into  Question Table:

        thread {
            //databaseclient = DatabaseCopier.getInstance(applicationContext).roomDatabase
            deleteAllTables(databaseclient!!)

            PreferenceUtil.putValue(PreferenceUtil.TOTAL_SECTION, questionAnswer.data.form_sections.size)
            PreferenceUtil.save()
            val empty_answer = ArrayList<Answers>()
            empty_answer.add(Answers())

            for (i in 0.until(questionAnswer.data.form_sections.size)) {
                // Section:
                val section_id = questionAnswer.data.form_sections[i].section_id
                val section_name = questionAnswer.data.form_sections[i].name
                for (j in 0.until(questionAnswer.data.form_sections[i].questions.size)) {
                    // question:
                    val question = questionAnswer.data.form_sections[i].questions[j]

                    databaseclient!!.questiondao().insert(
                            Question(serial_number = null,
                                    section_id = section_id,
                                    is_pwd = questionAnswer.data.form_sections[i].is_pwd,
                                    section_name = section_name,
                                    question_id = question.question_id,
                                    question_type = question.question_type,
                                    question = question.question,
                                    answer = Gson().toJson(getAnswerList(question)))
                    )


                }


                /*      // question
                      val question = Question(question_id = count++, question = "${"How it is posible" + count}")
                      databaseclient!!.questiondao().insert(question)
                      println("Flip table question Table =\n ${FlipTableConverters.fromIterable(databaseclient!!.questiondao().all, Question::class.java)}")

                      // answer
                      val answer = Answer(question_id = count++, question = "${"How it is Answer" + count}")
                      databaseclient!!.answerdao().insert(answer)
                      println("Flip table answer Table =\n ${FlipTableConverters.fromIterable(databaseclient!!.answerdao().all, Answer::class.java)}")
      */
            }

            println("Flip table question Table =\n ${FlipTableConverters.fromIterable(databaseclient!!.questiondao().all, Question::class.java)}")
            refreshlist()


        }


    }


    private fun getAnswerList(questions: Questions): ArrayList<Answers> {

        if (questions.question_type == 5) {
            val answerlist = ArrayList<Answers>()
            answerlist.add(Answers(answer = "हाँ"))
            answerlist.add(Answers(answer = "नही"))
            return answerlist

        } else if (questions.answers.size > 0) {
            return questions.answers

        } else {
            val answerlist = ArrayList<Answers>()
            answerlist.add(Answers())
            return answerlist
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Login.login_instance!!.finish()
        finish()
    }


    class MyClickHandler(val mainactivity: MainActivity) {


        fun onStart(view: View) {

            if (mainactivity.callLocationFunction()) {

                mainactivity.onstartNewservey()
               // Crashlytics.getInstance().crash() // Force a crash

            }
            //   (sectionwise.parentFragment as QuestionAnswer).addQuestion()

        }

        fun onEdit(view: View) {
            mainactivity.editAllRecords()
        }

        fun onDelete(view: View) {
            mainactivity.deleteCurrentRecord()
        }

        fun onSync(view: View) {

            if(mainactivity.syncing_btn.alpha == 0.2f){ return }

            mainactivity.showProgressDialog()
            mainactivity.syncAllRecords(1)
        }


    }


    override fun onStop() {
        super.onStop()
        loge(TAG,"onStop--")
    }

    override fun onResume() {
        super.onResume()
        loge(TAG,"onResume--")
    }

    override fun onRestart() {
        super.onRestart()
        loge(TAG,"onRestart--")
    }

    override fun onDestroy() {
        super.onDestroy()
        loge(TAG,"onDestroy--")
        removeLocationListner()


    }


}



