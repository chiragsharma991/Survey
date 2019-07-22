package com.softtech360.totalservey.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.softtech360.totalservey.R
import com.softtech360.totalservey.fragment.BaseFragment
import com.softtech360.totalservey.rest.ApiCall
import com.softtech360.totalservey.utils.Constants
import com.softtech360.totalservey.utils.DialogUtils
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.forget_password.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.toolbar.*

class ForgetPassword : BaseActivity() {



    companion object {
        val TAG = "ForgetPassword"
        fun newInstance(): ForgetPassword {
            return ForgetPassword()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_password)
        txt_toolbar.visibility = View.VISIBLE
        txt_toolbar.text="Forget Password"
        img_toolbar_back.visibility=View.VISIBLE
        img_toolbar_back.setOnClickListener{ finish() }
  /*      continue_btn.setOnClickListener {

            if(email.text!!.trim().length > 0 && validMail(email.text!!.trim().toString())){
                resetPassword(email.text!!.trim().toString())
            }else{
                showSnackBar(forget_password_container,"Please enter your valid email address")
            }

        }*/

    }

    private fun resetPassword (email: String) {

        showProgressDialog()

        val postParam = JsonObject()
        postParam.addProperty(Constants.EMAIL, email)

        callAPI(ApiCall.reset_password(postParam),
                object : BaseFragment.OnApiCallInteraction {

                    override fun <T> onSuccess(body: T?) {
                        showProgressDialog()
                        val response = body as JsonObject
                        if(response.get("status").asBoolean){
                            // true
                            Toast.makeText(this@ForgetPassword,response.get("message").asString,Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            // false
                            showSnackBar(forget_password_container,"Email is not a valid email address")

                        }


                    }

                    override fun onFail(error: Int) {
                        showProgressDialog()
                        when (error) {
                            404 -> {
                                DialogUtils.openDialog(this@ForgetPassword, getString(R.string.error_404), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@ForgetPassword, R.color.black), object : DialogUtils.OnDialogClickListener {
                                    override fun onPositiveButtonClick(p: Int) {
                                    }

                                    override fun onNegativeButtonClick() {
                                    }
                                })

                            }
                            100 -> {

                                DialogUtils.openDialog(this@ForgetPassword, getString(R.string.internet_not_available), "",
                                        getString(R.string.ok), "", ContextCompat.getColor(this@ForgetPassword, R.color.black), object : DialogUtils.OnDialogClickListener {
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

}
