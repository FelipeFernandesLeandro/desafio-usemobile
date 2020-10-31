package com.felipefernandes.desafiousemobile.config.retrofit

import java.io.Serializable

class ResponseError(
        var code: Int = -1,
        var error: String = ""
) : Serializable
