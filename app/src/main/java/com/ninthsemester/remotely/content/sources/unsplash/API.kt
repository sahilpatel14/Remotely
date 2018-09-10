package com.ninthsemester.remotely.content.sources.unsplash

import com.ninthsemester.android.remotely.Remote
import com.ninthsemester.remotely.content.models.PictureResponse
import retrofit2.http.GET

interface API {

    @GET("photos/")
    fun topPicksOfTheDay() : Remote.Call<List<PictureResponse>>

}