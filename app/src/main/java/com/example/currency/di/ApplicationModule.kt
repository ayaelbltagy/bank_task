package com.example.currency.di

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.currency.ui.MainActivity
import com.example.currency.data.data.api.ApiHelper
import com.example.currency.data.data.api.ApiHelperImpl
import com.example.currency.data.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLException


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object{
        val access_key = "0d1a3e7af52c268e9ad4e1598a0a1053"

    }

    private val CONTENT_TYPE = "Content-Type"
    private val CONTENT_TYPE_VALUE = "application/json"
    private val TIMEOUT_CONNECT = 30   //In seconds
    private val TIMEOUT_READ = 30   //In seconds

    @Provides
    fun provideBaseUrl() = "http://data.fixer.io/api/"

    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    private var headerInterceptor = Interceptor { chain ->
        val original = chain.request()

        val request = original.newBuilder()

//        request.header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
//        request.header("Accept", CONTENT_TYPE_VALUE)
  //      request.header("access_key", "0d1a3e7af52c268e9ad4e1598a0a1053"  )
        request.method(original.method, original.body)

        try {
            // Check network connectivity
            if (!isNetworkConnected()) {
                // Handle network not connected scenario
                throw IOException("Network not connected")
            }

            val response = chain.proceed(request.build())

            response
        } catch (e: SSLException) {
            // Handle SSLException
            e.printStackTrace()
            // Log the error or perform any necessary actions
            throw IOException("SSLException occurred: ${e.message}")
        } catch (e: IOException) {
            // Handle IOException
            e.printStackTrace()
            // Log the error or perform any necessary actions
            throw e
        }
    }

    // Helper function to check network connectivity
    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            MainActivity.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected == true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.apply {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }.level = HttpLoggingInterceptor.Level.BODY

        okHttpBuilder.addInterceptor(headerInterceptor)
        okHttpBuilder.addInterceptor(loggingInterceptor)
        okHttpBuilder.connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)

        return okHttpBuilder.build()

    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}