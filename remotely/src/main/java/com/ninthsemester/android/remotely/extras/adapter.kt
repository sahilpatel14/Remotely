package com.ninthsemester.android.remotely.extras

import com.ninthsemester.android.remotely.Remote
import retrofit2.*
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor


/**
 * An object that overrides the [CallAdapter] interface for retrofit
 * and provides a way to convert Retrofit [Call] object to [Remote.Call] object.
 *
 * We pass the executor object and the type of response.
 */
class RemoteCallAdapter<R>(
        private val responseType: Type,
        private val callbackExecutor: Executor?,
        private val retrofit: Retrofit,
        private val errorHandler: (errorResponse: Response<*>, retrofit: Retrofit) -> Exception) : CallAdapter<R, Remote.Call<R>> {


    override fun adapt(call: Call<R>): Remote.Call<R> {
        return CallAdapter(call, callbackExecutor, retrofit, errorHandler)
    }

    override fun responseType(): Type {
        return responseType
    }


    /**
     * A Factory object which creates a [Remote.Call] object when passed on with
     * required preparing parameters. We extract the callback executor and response type
     * from the passed data and return a [RemoteCallAdapter] object.
     */
    class Factory(
            private val errorDeserializer: (errorResponse: Response<*>, retrofit: Retrofit) -> Exception)
        : CallAdapter.Factory() {
        override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {

            if(returnType == null || getRawType(returnType) != Remote.Call::class.java) {
                return null
            }

            if (returnType !is ParameterizedType) {
                throw IllegalStateException(
                        "Remote.Call must have generic type (e.g., MyCall<ResponseBody>)")
            }

            val responseType: Type = getParameterUpperBound(0, returnType)
            val callbackExecutor = retrofit?.callbackExecutor()
            return RemoteCallAdapter<Any>(responseType, callbackExecutor, retrofit!!, errorDeserializer)
        }
    }
}

/**
 * A simple adapter class that takes a [call] object from the Retrofit API and
 * converts it into [Remote.Call] object. We are also passing the [callExecutor]
 * object which will describe the thread on which the call will be made.
 */
class CallAdapter<T>(
        private val call: Call<T>,
        private val callExecutor: Executor?,
        private val retrofit: Retrofit,
        private val errorHandler: (errorResponse: Response<*>, retrofit: Retrofit) -> Exception
        ) : Remote.Call<T> {


    /**
     * Intercepts the enqueue call made from the api request and
     * generates [Remote.Call] methods based on response code.
     */
    override fun enqueue(callback: Remote.CallBack<T>) {
        call.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>?) {

                if (response == null) {
                    callback.onFailure(UnexpectedException(NullPointerException("Retrofit response object is null")))
                }

                response?.let {
                    val responseCode = it.code()


                    when(responseCode) {

                        //  We are good to handle the response. Redirecting to success callback
                        in 200..299 -> callback.onSuccess(response)

                        //  Unauthorized to view the request
                        401 -> {
                            val error = errorHandler(response, retrofit)
                            callback.onFailure(error)
                        }

                        //  Page not found
                        404 -> {
                            val error = errorHandler(response, retrofit)
                            callback.onFailure(error)
                        }

                        //  We are not able to handle this response code.
                        else -> callback.onFailure(UnexpectedException(
                                IllegalStateException("We are not handling this response code : $responseCode"))
                        )
                    }
                }


            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                if (t is IOException) {
                    callback.onFailure(NetworkConnectionException())
                } else {
                    callback.onFailure(UnexpectedException(
                            IllegalStateException("We have an unknown error in our failure callback")))
                }
            }
        })
    }


    /**
     * In case of cancel request, we will call the cancel
     * method of [call]
     */
    override fun cancel() {
        call.cancel()
    }


    /**
     * Create a new object of same class. This time pass a clone
     * of [call] object.
     */
    override fun clone(): Remote.Call<T> {
        return CallAdapter<T>(call.clone(), callExecutor, retrofit, errorHandler)
    }
}