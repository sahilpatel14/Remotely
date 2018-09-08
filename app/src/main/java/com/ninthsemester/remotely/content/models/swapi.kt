package com.ninthsemester.remotely.content.models

data class StarShip(val name: String, val model: String, val manufacturer: String)

data class StarShipListResponse (
        val count : Int,
        val results : List<StarShip>
)


data class SwapiErrorResponse(
        val detail: String)

class SwapiException(errorMessage: String) : Exception(errorMessage)