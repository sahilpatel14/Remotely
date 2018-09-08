package com.ninthsemester.android.remotely.extras

import java.io.IOException
import java.net.MalformedURLException

fun createErrorMessage(url: String): String {
    return "Url $url could not be found"
}

class InvalidBaseUrlException(url: String) :
        Exception(createErrorMessage(url), Throwable(MalformedURLException(url)))

class EndpointNotFoundException : Exception("the url was not found")
class UnexpectedException(throwable: Throwable) : Exception("Something unexpected has happened", throwable)
class NetworkConnectionException : Exception("We were unable to connect to the network", IOException())