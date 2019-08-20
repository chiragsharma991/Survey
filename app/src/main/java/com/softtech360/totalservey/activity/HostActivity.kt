package com.softtech360.totalservey.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.HostactivityBinding
import com.softtech360.totalservey.fragment.QuestionAnswer
import com.softtech360.totalservey.utils.OnBackPressedListener
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softtech360.totalservey.utils.PreferenceUtil


class HostActivity : BaseActivity(){

    private lateinit var binding: HostactivityBinding

    companion object {
        val TAG = "HostActivity"
        var questionIsResume : Boolean = false
        var any_toilet : Boolean = false
        var oneMore_notify : Boolean = false
        var statusofstudy : Int = -1
        var statusofAge : HashMap<Int,Int>? = HashMap()
        var pwdName : HashMap<Int,String> = HashMap()
        val conditionalQuestion = ArrayList<ConditionalModel>()
        fun newInstance(): HostActivity {
            return HostActivity()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.hostactivity)
        initView(savedInstanceState)

    }

    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            logd(QuestionAnswer.TAG, "saveInstance Not NULL")
            questionIsResume=intent.getBooleanExtra("RESUME",false)
            oneMore_notify = false

            if(PreferenceUtil.getString(PreferenceUtil.STATUSOFAGE,"")!!.trim().length > 0){
                val type = object : TypeToken<HashMap<Int,Int>>() {}.type
                val statusofage: HashMap<Int,Int> = Gson().fromJson(PreferenceUtil.getString(PreferenceUtil.STATUSOFAGE,""), type)
                statusofAge=statusofage
                loge(TAG,"statusofAge length--"+statusofAge!!.size)
            }


            conditionalQuestion.add(ConditionalModel(question_id=14,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =13 ,target_section_id=2)))
            conditionalQuestion.add(ConditionalModel(question_id=20,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =19 ,target_section_id=2)))
            conditionalQuestion.add(ConditionalModel(question_id=23,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =21 ,target_section_id=2)))
            conditionalQuestion.add(ConditionalModel(question_id=25,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =24 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=26,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =24 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=27,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =24 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=32,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =31 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=35,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =32 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=33,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =32 ,target_section_id=3)))
            conditionalQuestion.add(ConditionalModel(question_id=40,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =20 ,target_section_id=2)))
            conditionalQuestion.add(ConditionalModel(question_id=41,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =40 ,target_section_id=3)))

            conditionalQuestion.add(ConditionalModel(question_id=44,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =43 ,target_section_id=4)))
            conditionalQuestion.add(ConditionalModel(question_id=45,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =43 ,target_section_id=4)))
            conditionalQuestion.add(ConditionalModel(question_id=47,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =46 ,target_section_id=4)))
            conditionalQuestion.add(ConditionalModel(question_id=49,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =48 ,target_section_id=4)))
            conditionalQuestion.add(ConditionalModel(question_id=50,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =48 ,target_section_id=4)))
            conditionalQuestion.add(ConditionalModel(question_id=72,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =48 ,target_section_id=4)))

            conditionalQuestion.add(ConditionalModel(question_id=59,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =58 ,target_section_id=7)))
            conditionalQuestion.add(ConditionalModel(question_id=74,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =73 ,target_section_id=7)))
            conditionalQuestion.add(ConditionalModel(question_id=75,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =73 ,target_section_id=7)))
            conditionalQuestion.add(ConditionalModel(question_id=76,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =73 ,target_section_id=7)))

           // conditionalQuestion.add(ConditionalModel(question_id=57,section_id =0,conditional_target=ConditionalTargetModel(target_question_id =56 ,target_section_id=6)))


        }else{
            logd(QuestionAnswer.TAG, "saveInstance Not NULL")

        }

    }





    override fun onBackPressed() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_fragment)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount
        loge(TAG,"backStackEntryCount"+backStackEntryCount)


        val currentFragment = navHostFragment?.childFragmentManager?.getFragments()!!.get(0)
        loge(TAG,"currentFragment"+currentFragment)
        val controller = Navigation.findNavController(this, R.id.navhost_fragment)
        if (currentFragment is OnBackPressedListener){
            (currentFragment as OnBackPressedListener).onBackPressed()
        }

    }


    data class ConditionalModel(
            var question_id : Int = 0,
            var section_id : Int = 0,
            val conditional_target :ConditionalTargetModel
    )
    data class ConditionalTargetModel(
            val target_question_id : Int = 0,
            val target_section_id : Int = 0
    )








}