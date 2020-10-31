package com.felipefernandes.desafiousemobile.entities.contact

import java.io.Serializable

open class ContactModel(
    val id: Int,
    val photo: String,
    val name: String,
    val email: String,
    val isVerified: Boolean,
): Serializable
