package com.softtech360.totalservey.activity

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.softtech360.totalservey.R
import com.softtech360.totalservey.databinding.LoginBinding
import com.softtech360.totalservey.fragment.BaseFragment
import com.softtech360.totalservey.model.QuestionAnswer
import com.softtech360.totalservey.rest.ApiCall
import com.softtech360.totalservey.utils.Constants
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.*
import java.util.*

class Login : BaseActivity(), AnkoLogger {


    companion object {
        val TAG = "Login"
        var login_instance : Activity? = null
        fun newInstance(): Login {
            return Login()
        }
    }



    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login)
        initView(savedInstanceState)
    }

    val myclickhandler: MyClickHandler = MyClickHandler(this)
    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            loge(Login.TAG, "saveInstance Not NULL")
            login_instance = this
            val email = PreferenceUtil.getString(PreferenceUtil.EMAIL,"")

            if(PreferenceUtil.getBoolean(PreferenceUtil.ISLOGIN,false) ){
                startActivity(intentFor<MainActivity>())
                finish()
            }else{
                binding.onclick = myclickhandler
                username.setText("User-d49b4281-NBJK")
                password.setText("07c07c08")

              //  username.setText("user-d49b4281-nbjk")
               // password.setText("07c07c08")

                // commit
            }



        } else {

            loge(Login.TAG, "saveInstance Not NULL")

        }

    }




    private fun onstartLogin() {


        if(isValidate()){

            if(PreferenceUtil.getString(PreferenceUtil.USER_NAME,"")!!.trim().length > 0 && PreferenceUtil.getString(PreferenceUtil.PASSWORD,"")!!.trim().length > 0 ){

                if( username.text!!.trim().toString().toLowerCase() == PreferenceUtil.getString(PreferenceUtil.USER_NAME,"")!!.toLowerCase() && password.text!!.trim().toString().toLowerCase() == PreferenceUtil.getString(PreferenceUtil.PASSWORD,"")!!.toLowerCase() ){
                    loge(TAG,"fetchQuestionAnswer offline")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("IS_ONLINE",false)
                    startActivity(intent)
                    PreferenceUtil.putValue(PreferenceUtil.ISLOGIN,true)
                    PreferenceUtil.save()
                    finish()

                } else{

                    fetchQuestionAnswer(username.text!!.toString(),password.text!!.toString())

                }


            }else{

                fetchQuestionAnswer(username.text!!.toString(),password.text!!.toString())
            }

        }


    }


    private fun fetchQuestionAnswer(username: String, password: String) {


        loge(TAG,"fetchQuestionAnswer online"+PreferenceUtil.getString(PreferenceUtil.USER_NAME,"")+" "+PreferenceUtil.getString(PreferenceUtil.PASSWORD,""))
        showProgressDialog()
        val postParam = JsonObject()
        postParam.addProperty(Constants.USERNAME, username)
        postParam.addProperty(Constants.PASSWORD, password)

        callAPI(ApiCall.login(postParam),
                object : BaseFragment.OnApiCallInteraction {

                    override fun <T> onSuccess(body: T?) {
                        showProgressDialog()
                        val response = body as JsonObject
                        val loginmodel = GsonBuilder().create().fromJson(response.toString(), LoginModel::class.java)
                        if(loginmodel.status)passdataInMemory(loginmodel)
                        else showSnackBar(logincontainer, "Incorrect username or password.")



                    }

                    override fun onFail(error: Int) {
                        showProgressDialog()
                        when (error) {
                            404 -> {
                                DialogUtils.openDialog(this@Login, getString(R.string.error_404), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@Login, R.color.black), object : DialogUtils.OnDialogClickListener {
                                    override fun onPositiveButtonClick(p: Int) {
                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                })

                            }
                            100 -> {

                                DialogUtils.openDialog(this@Login, getString(R.string.internet_not_available), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@Login, R.color.black), object : DialogUtils.OnDialogClickListener {
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


    fun isValidate(): Boolean {

        return when {
            TextUtils.isEmpty(username.text!!.trim().toString()) -> {
                showSnackBar(logincontainer, "Please Enter your username")
                false
            }
      /*      !validMail(username.text!!.trim().toString()) -> {
                showSnackBar(logincontainer, "Please Enter valid email address")
                false
            }*/
            TextUtils.isEmpty(password.text!!.trim().toString()) -> {
                showSnackBar(logincontainer, "Please Enter your password")
                false
            }
            else -> true
        }
    }



    private fun passdataInMemory(loginmodel: LoginModel) {

        PreferenceUtil.putValue(PreferenceUtil.USER_STATES, Gson().toJson(loginmodel.user_states))
        PreferenceUtil.putValue(PreferenceUtil.USER_DISTRICTS, Gson().toJson(loginmodel.user_districts))
        PreferenceUtil.putValue(PreferenceUtil.USER_SECTIONS, Gson().toJson(loginmodel.user_sections))
        PreferenceUtil.putValue(PreferenceUtil.USER_VILLAGE_COUNCIL, Gson().toJson(loginmodel.user_village_council))
        PreferenceUtil.putValue(PreferenceUtil.USER_ID, loginmodel.user.id)
        PreferenceUtil.putValue(PreferenceUtil.EMAIL, loginmodel.user.email)
        PreferenceUtil.putValue(PreferenceUtil.ORGANIZATION_ID, loginmodel.user.organization_id)
        PreferenceUtil.putValue(PreferenceUtil.ORGANIZATION_NAME, loginmodel.user.organization_name)
        PreferenceUtil.putValue(PreferenceUtil.SURVEY_COUNT, loginmodel.user.survey_count)
        PreferenceUtil.putValue(PreferenceUtil.USER_NAME, username.text!!.trim().toString())
        PreferenceUtil.putValue(PreferenceUtil.PASSWORD,password.text!!.trim().toString())
        PreferenceUtil.putValue(PreferenceUtil.ISLOGIN,true)
        PreferenceUtil.putValue(PreferenceUtil.INITIAL_TIME,getCurrentDate())

        PreferenceUtil.save()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("IS_ONLINE",true)
        startActivity(intent)
        finish()


    }



    data class LoginModel(
            val status : Boolean,
            val message : String ,
            val user : User ,
            val user_states: ArrayList<UserStates> = arrayListOf(),
            val user_districts: ArrayList<UserStates> = arrayListOf(),
            val user_sections: ArrayList<UserStates> = arrayListOf(),
            val user_village_council: ArrayList<UserStates> = arrayListOf()
    )

    data class User(

            val id : Int ,
            val first_name : String ,
            val last_name : String ,
            val username : String ,
            val email : String ,
            val phone_number : String ,
            val organization_id : String,
            val organization_name : String,
            val survey_count : String

    )

    data class UserStates(val is_deleted: Int,
                          val user_id: Int =-1,
                          val state_id: Int=-1,
                          val district_id: Int=-1,
                          val section_id: Int=-1,
                          val name: String = "",
                          val id: Int =-1)

    class MyClickHandler(val login: Login) {


        fun onLogin(view: View) {

            login.onstartLogin()

        }

        fun onForgetPassword(view: View) {

            login.startActivity(Intent(login,ForgetPassword::class.java))

        }


    }


}
