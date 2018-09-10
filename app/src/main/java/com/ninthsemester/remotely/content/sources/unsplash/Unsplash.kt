package com.ninthsemester.remotely.content.sources.unsplash

import com.ninthsemester.android.remotely.Remote
import com.ninthsemester.android.remotely.extras.deSerialiseErrorWithModel
import com.ninthsemester.remotely.content.models.PictureResponse
import com.ninthsemester.remotely.content.models.UnSplashException
import com.ninthsemester.remotely.content.models.UnsplashErrorResponse
import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.Retrofit

class Unsplash {

    private val errorResponseHandler : (Response<*>, Retrofit) -> Exception = { errorResponse, retrofit ->

        val error = deSerialiseErrorWithModel(errorResponse, retrofit, UnsplashErrorResponse::class.java)
        val errorMessage = error.errors[0]
        UnSplashException(errorMessage)
    }


    private val clientIdInterceptor = Interceptor {
        val originalRequest = it.request()
        val originalRequestUrl = originalRequest.url();

        val newUrl = originalRequestUrl.newBuilder()
                .addQueryParameter("client_id", clientId)
                .build();

        val newRequest = originalRequest.newBuilder().url(newUrl).build();
        it.proceed(newRequest)
    }

    private val baseUrl = "https://api.unsplash.com/"
    private val clientId = "lolol"
    private val remote = Remote(
            baseUrl,
            listOf(clientIdInterceptor),
            errorResponseHandler)

    private val service = remote.service(API::class.java)


    fun topPicksOfTheDay(
            onPictureListReceived : (pictureResponseList : List<PictureResponse>) -> Unit,
            onError: (Exception) -> Unit
    ) {
        service.topPicksOfTheDay().enqueue(object : Remote.CallBack<List<PictureResponse>> {

            override fun onSuccess(response: Response<List<PictureResponse>>?) {
                response?.let {

                    it.body()?.let {
                        onPictureListReceived(it)
                    } ?: onError(RuntimeException("No response found"))

                } ?: onError(RuntimeException("No response found"))
            }

            override fun onFailure(t: Throwable?) {
                onError(Exception(t))
            }
        })
    }

    companion object {
        const val TAG = "Unsplash : ";
    }
}