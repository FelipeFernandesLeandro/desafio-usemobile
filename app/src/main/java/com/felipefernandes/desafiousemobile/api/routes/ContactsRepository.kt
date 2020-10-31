package com.felipefernandes.desafiousemobile.api.routes

import com.felipefernandes.desafiousemobile.entities.contact.ContactWithAboutModel
import com.felipefernandes.desafiousemobile.entities.response.ContactListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactsRepository {
    @GET("user/")
    suspend fun fetchContactList(): Response<ContactListResponse>?

    @GET("user/{contactId}")
    suspend fun fetchContact(
        @Path("contactId") contactId: Int
    ): Response<ContactWithAboutModel>?
}
