package com.felipefernandes.desafiousemobile.config.retrofit

import com.felipefernandes.desafiousemobile.BuildConfig
import com.felipefernandes.desafiousemobile.api.routes.ContactsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object BaseClient {
    fun getApi(timeOutSeconds: Int = 50): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor{
                    chain ->
                    val request = chain
                            .request()
                            .newBuilder()
                            .addHeader("Locale", Locale.getDefault().language)

                    chain.proceed(request.build())
                }
                .readTimeout(timeOutSeconds.toLong(), TimeUnit.SECONDS)
                .connectTimeout(timeOutSeconds.toLong(), TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
    }

    val contactsService: ContactsRepository = getApi().create(ContactsRepository::class.java)
}
