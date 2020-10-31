package com.felipefernandes.desafiousemobile.ui.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.api.viewModels.ContactsViewModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactsAdapter
import com.felipefernandes.desafiousemobile.extensions.navigateWithAnimations
import kotlinx.android.synthetic.main.fragment_contact_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ContactListFragment : Fragment() {
    private val viewModel: ContactsViewModel by viewModel()
    private var localContactList: List<ContactModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localContactList?.let {
            setContactsAdapter(it)
        }
    }

    private fun setObservers() {
        viewModel.contactList.observe(this, { contacts ->
            localContactList = contacts
            setContactsAdapter(contacts)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setObservers()
        loadContacts()
    }

    private fun setContactsAdapter(contacts: List<ContactModel>) {
        val contactsAdapter = ContactsAdapter(contacts) { id, name ->
            val directions = ContactListFragmentDirections
                .actionContactListFragmentToProfileFragment(id, name)

            findNavController().navigateWithAnimations(directions)
        }

        contactListFragmentRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context))
            adapter = contactsAdapter
        }
    }

    private fun loadContacts() {
        viewModel.fetchContactList()
    }

    override fun onDestroy() {
        viewModel.cancelPendingJobs()
        super.onDestroy()
    }
}
