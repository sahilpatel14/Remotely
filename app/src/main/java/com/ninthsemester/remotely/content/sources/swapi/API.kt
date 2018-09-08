package com.ninthsemester.remotely.content.sources.swapi

import com.google.gson.JsonObject
import com.ninthsemester.android.remotely.Remote
import com.ninthsemester.remotely.content.models.StarShip
import com.ninthsemester.remotely.content.models.StarShipListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface API {

    @GET("starships/{id}")
    fun getStarShip(@Path("id") starShipId: String) : Remote.Call<StarShip>

    @GET("starships")
    fun getStarShips() : Remote.Call<StarShipListResponse>
}
