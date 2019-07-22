package com.softtech360.totalservey.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object ApiClient{

    val BASE_URL = "http://192.168.1.20/survey/backend/web/index.php/api/"
    //val BASE_URL = "https://nbjk.org/survey/backend/web/index.php/api/"
    //https://nbjk.org/survey/


    private var retrofit: Retrofit? = null


    fun getClient(): Retrofit? {


        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).
                addInterceptor(interceptor).build()

        if (retrofit == null) {
            //val builder = GsonBuilder().disableHtmlEscaping().create()


            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
        }
        return retrofit
    }


}
