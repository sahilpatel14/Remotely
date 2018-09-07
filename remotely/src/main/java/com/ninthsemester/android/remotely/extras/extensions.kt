package com.ninthsemester.android.remotely.extras

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.ninthsemester.android.remotely.Remote
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.net.MalformedURLException
import java.net.URL

fun Remote.validateBaseUrl() {
    try {
        URL(baseUrl)
    } catch (ex : MalformedURLException) {
        throw InvalidBaseUrlException(baseUrl)
    }
}


fun <T> deSerialiseErrorWithModel(errorResponse: Response<*>, retrofit: Retrofit, clazz: Class<T>) : T {
    val errorConverter : Converter<ResponseBody, T> =
            retrofit.responseBodyConverter(clazz, arrayOfNulls<Annotation>(0))
    return errorConverter.convert(errorResponse.errorBody()!!)
}


fun <T> convertJsonObjectToModel(responseJson : JsonArray) : List<T> {


    val listType = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(responseJson, listType)
}