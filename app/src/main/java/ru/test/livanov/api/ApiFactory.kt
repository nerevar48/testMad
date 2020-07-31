package ru.test.livanov.api

import android.util.Base64
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.test.livanov.util.GITHUB_API_BASE_URL
import ru.test.livanov.util.GITHUB_LOGIN
import ru.test.livanov.util.GITHUB_TOKEN
import java.nio.charset.Charset

object ApiFactory {

    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
            .newBuilder()
            .build()
        val credentials = "$GITHUB_LOGIN:$GITHUB_TOKEN"
        val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .addHeader("Authorization", basic)
            .build()

        chain.proceed(newRequest)
    }

    private val gitHubClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .build()


    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(gitHubClient)
        .baseUrl(GITHUB_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    fun getClient(): GitHubApi {
        return retrofit().create(GitHubApi::class.java)
    }

}