package com.felipefernandes.desafiousemobile.entities.contact

import java.io.Serializable

class ContactWithAboutModel(
    id: Int,
    photo: String,
    name: String,
    email: String,
    isVerified: Boolean,
    val about: String,
) :
    ContactModel(
        id,
        photo,
        name,
        email,
        isVerified
    ), Serializable
