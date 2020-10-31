package com.felipefernandes.desafiousemobile.config.retrofit

import com.felipefernandes.desafiousemobile.api.viewModels.ContactsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependenciesModules {

    val appModule = module {

        factory { BaseClient.contactsService }

        viewModel { ContactsViewModel(get()) }
    }
}
