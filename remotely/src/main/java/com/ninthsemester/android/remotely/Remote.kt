package com.ninthsemester.android.remotely

import com.ninthsemester.android.remotely.extras.InvalidBaseUrlException
import com.ninthsemester.android.remotely.extras.RemoteCallAdapter
import com.ninthsemester.android.remotely.extras.loggingInterceptor
import com.ninthsemester.android.remotely.extras.isValidUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Remote(
        baseUrl : String,
        interceptors: List<Interceptor> = emptyList(),
        errorResponseHandler : (errorResponse: Response<*>, retrofit: Retrofit) -> Exception) {

    private val retrofit : Retrofit
    private val clientBuilder = OkHttpClient.Builder()
    private val callAdapterFactory : RemoteCallAdapter.Factory

    init {

        //  throws an exception if the base url is not valid
        val isValid = isValidUrl(baseUrl)
        if (!isValid) {
            throw InvalidBaseUrlException(baseUrl)
        }

        //  Adding a logging interceptor
        clientBuilder.addNetworkInterceptor(loggingInterceptor)

        //  Adding all passed interceptors. These will possibly inject,
        //  modify header parameters.
        interceptors.forEach { clientBuilder.addInterceptor(it) }


        //  Creating the adapter that transforms Retrofit.Call into Remote.Call
        //  and Retrofit.Callback into Remote.Callback
        callAdapterFactory = RemoteCallAdapter.Factory(errorResponseHandler)


        //  Creating the retrofit object with passed arguments
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RemoteCallAdapter.Factory(errorResponseHandler))
                .build()
    }


    fun <T>service(clazz: Class<T>) : T {
        return retrofit.create(clazz)
    }


    interface CallBack<T> {
        fun onSuccess(response: Response<T>?)
        fun onFailure(t: Throwable?)
    }

    interface Call<T> {
        fun cancel()
        fun enqueue(callback: CallBack<T>)
        fun clone(): Call<T>
    }

    companion object {
        const val TAG: String = "Remote : "
    }
}