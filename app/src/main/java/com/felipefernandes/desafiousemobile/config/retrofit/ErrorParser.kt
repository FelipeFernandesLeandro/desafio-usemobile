package com.felipefernandes.desafiousemobile.config.retrofit

import retrofit2.Response
import retrofit2.Retrofit

import java.io.IOException

object ErrorParser {
    fun parse(client: Retrofit, response: Response<*>?): ResponseError? {
        var error: ResponseError? = null
        val converter = client.responseBodyConverter<ResponseError>(ResponseError::class.java, arrayOfNulls(0))

        return try {
            response?.errorBody()?.let {
                error = ResponseError()
                error = converter.convert(it)
                error?.code = response.code()
            }
            error
        } catch (e: IOException) {
            error?.code = response?.code() ?: -1
            error
        }
    }
}
