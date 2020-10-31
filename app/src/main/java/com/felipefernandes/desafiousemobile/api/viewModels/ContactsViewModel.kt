package com.felipefernandes.desafiousemobile.api.viewModels

import com.felipefernandes.desafiousemobile.config.retrofit.ResponseError
import com.felipefernandes.desafiousemobile.config.retrofit.SingleLiveEvent
import androidx.lifecycle.ViewModel
import com.felipefernandes.desafiousemobile.api.routes.ContactsRepository
import com.felipefernandes.desafiousemobile.config.retrofit.BaseClient
import com.felipefernandes.desafiousemobile.config.retrofit.ErrorParser
import com.felipefernandes.desafiousemobile.entities.contact.ContactModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactWithAboutModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContactsViewModel(
    private var api: ContactsRepository
) : ViewModel() {
    private var job: Job? = null

    val isLoading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<ResponseError>()
    val contactList = SingleLiveEvent<List<ContactModel>>()
    val singleContact = SingleLiveEvent<ContactWithAboutModel>()

    fun fetchContactList() {
        isLoading.postValue(true)

        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = api.fetchContactList()

                when (result?.isSuccessful) {
                    true -> {
                        isLoading.postValue(false)
                        contactList.postValue(result.body()?.result)
                    }
                    else -> {
                        isLoading.postValue(false)
                        error.postValue(ErrorParser.parse(BaseClient.getApi(), result))
                    }
                }
            } catch (e: Exception) {
                isLoading.postValue(false)
                error.postValue(null)
            }
        }
    }

    fun fetchContact(contactId: Int) {
        isLoading.postValue(true)

        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = api.fetchContact(contactId)

                when (result?.isSuccessful) {
                    true -> {
                        isLoading.postValue(false)
                        singleContact.postValue(result.body())
                    }
                    else -> {
                        isLoading.postValue(false)
                        error.postValue(ErrorParser.parse(BaseClient.getApi(), result))
                    }
                }
            } catch (e: Exception) {
                isLoading.postValue(false)
                error.postValue(null)
            }
        }
    }

    fun cancelPendingJobs() {
        job?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}
