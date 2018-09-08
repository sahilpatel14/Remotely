package com.ninthsemester.android.remotely.extras

import android.util.Log
import com.ninthsemester.android.remotely.Remote
import okhttp3.logging.HttpLoggingInterceptor


/**
 * Logs request and response calls made to a network.
 */
fun loggingIntercept () : HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor.Logger { Log.d(Remote.TAG, it) }
    val loggingInterceptor = HttpLoggingInterceptor(logger)
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return loggingInterceptor
}


val loggingInterceptor = loggingIntercept()