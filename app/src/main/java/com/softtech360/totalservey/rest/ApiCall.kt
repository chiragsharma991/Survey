package com.softtech360.totalservey.rest

import com.google.gson.JsonObject
import retrofit2.Call


class ApiCall {
    // username,password_hash,type,login_type

    companion object {

        private fun getApiInterface(): ApiInterface {
            return ApiClient.getClient()!!.create(ApiInterface::class.java)
        }

        fun createSurvey(jsonObject: JsonObject) : Call<JsonObject> {
            return getApiInterface().createSurvey(jsonObject)
        }
        fun getQuestionAnswer() : Call<JsonObject> {
            return getApiInterface().getQuestionAnswer()
        }

        fun login(jsonObject: JsonObject) : Call<JsonObject> {
            return getApiInterface().login(jsonObject)
        }

        fun reset_password(jsonObject: JsonObject) : Call<JsonObject> {
            return getApiInterface().reset_password(jsonObject)
        }
    }
}