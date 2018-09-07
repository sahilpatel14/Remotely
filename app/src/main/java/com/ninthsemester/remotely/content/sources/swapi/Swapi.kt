package com.ninthsemester.remotely.content.sources.swapi

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.ninthsemester.android.remotely.Remote
import com.ninthsemester.android.remotely.extras.convertJsonObjectToModel
import com.ninthsemester.android.remotely.extras.deSerialiseErrorWithModel
import com.ninthsemester.remotely.content.models.StarShip
import com.ninthsemester.remotely.content.models.UnSplashException
import com.ninthsemester.remotely.content.models.UnsplashErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Swapi(context: Context) {

    private val errorResponseHandler : (Response<*>, Retrofit) -> Exception = { errorResponse, retrofit ->

        val error = deSerialiseErrorWithModel(errorResponse, retrofit, UnsplashErrorResponse::class.java)
        val allErrors = error.errors.reduce { acc, s -> acc+s }
        UnSplashException(allErrors)
    }

    private val baseUrl = "https://swapi.co/api/"
    private val remote = Remote(
            baseUrl,
            errorResponseHandler = errorResponseHandler
    )
    private val swapiService = remote.service(API::class.java)



    fun getStarships() {
        swapiService.getStarShips().enqueue(object : Remote.CallBack<JsonObject> {

            override fun onSuccess(response: Response<JsonObject>?) {
                val results = response?.body()?.getAsJsonArray("results")
                val ships : List<StarShip> = convertJsonObjectToModel<StarShip>(results!!)
                Log.d(TAG, "fss")
            }

            override fun onFailure(t: Throwable?) {
                Log.d(TAG, "fss")
            }
        })
//        remote.swapiService(API::class.java).getStarShips().enqueue()
    }

    fun getShip() {
        swapiService.getStarShip("9").enqueue(object : Remote.CallBack<StarShip> {

            override fun onSuccess(response: Response<StarShip>?) {
                Log.d(TAG, "fss")
            }

            override fun onFailure(t: Throwable?) {
                Log.d(TAG, "fss")
            }
        })
    }

    fun getShips() {
        swapiService.getShips().enqueue(object : Callback<List<StarShip>> {

            override fun onFailure(call: Call<List<StarShip>>?, t: Throwable?) {
                Log.d(TAG, "fss")
            }

            override fun onResponse(call: Call<List<StarShip>>?, response: Response<List<StarShip>>?) {
                Log.d(TAG, "fss")
            }
        })
    }

    companion object {
        const val TAG = "Swapi : ";
    }
}