package com.ninthsemester.remotely.content.sources.swapi

import android.content.Context
import com.ninthsemester.android.remotely.Remote
import com.ninthsemester.android.remotely.extras.deSerialiseErrorWithModel
import com.ninthsemester.remotely.content.models.StarShip
import com.ninthsemester.remotely.content.models.StarShipListResponse
import com.ninthsemester.remotely.content.models.SwapiErrorResponse
import com.ninthsemester.remotely.content.models.SwapiException
import retrofit2.Response
import retrofit2.Retrofit

class Swapi(context: Context) {

    private val errorResponseHandler : (Response<*>, Retrofit) -> Exception = { errorResponse, retrofit ->

        val error = deSerialiseErrorWithModel(errorResponse, retrofit, SwapiErrorResponse::class.java)
        val errorMessage = error.detail
        SwapiException(errorMessage)
    }

    private val baseUrl = "https://swapi.co/api/"
    private val remote = Remote(
            baseUrl,
            errorResponseHandler = errorResponseHandler)


    private val service = remote.service(API::class.java)


    fun getStarShip(
            starShipId : String,
            onStarShipReceived : (starShip: StarShip) -> Unit,
            onError : (Exception) -> Unit) {
        
        service.getStarShip(starShipId).enqueue(object : Remote.CallBack<StarShip> {

            override fun onSuccess(response: Response<StarShip>?) {
                response?.let {

                    it.body()?.let {
                        onStarShipReceived(it)
                    } ?: onError(RuntimeException("No response found"))

                } ?: onError(RuntimeException("No response found"))

            }

            override fun onFailure(t: Throwable?) {
                onError(Exception(t))
            }
        })
        
    }

    fun getStarShips(
            onStarShipListReceived : (starShipList : List<StarShip>) -> Unit,
            onError: (Exception) -> Unit) {

        service.getStarShips().enqueue(object : Remote.CallBack<StarShipListResponse> {


            override fun onSuccess(response: Response<StarShipListResponse>?) {
                response?.let {

                    it.body()?.let {
                        onStarShipListReceived(it.results)
                    } ?: onError(RuntimeException("No response found"))

                } ?: onError(RuntimeException("No response found"))
            }

            override fun onFailure(t: Throwable?) {
                onError(Exception(t))
            }
        })
    }

    companion object {
        const val TAG = "Swapi : ";
    }
}