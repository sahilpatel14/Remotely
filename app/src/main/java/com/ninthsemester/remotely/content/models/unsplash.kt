package com.ninthsemester.remotely.content.models

import java.util.*



data class UnsplashErrorResponse(
        val errors : Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnsplashErrorResponse
        if (!Arrays.equals(errors, other.errors)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(errors)
    }
}


class UnSplashException(errorMessage : String) : Exception(errorMessage)

