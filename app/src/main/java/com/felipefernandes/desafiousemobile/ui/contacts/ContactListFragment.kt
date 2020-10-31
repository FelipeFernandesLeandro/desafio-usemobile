package com.felipefernandes.desafiousemobile.ui.contacts

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.api.viewModels.ContactsViewModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactsAdapter
import com.felipefernandes.desafiousemobile.extensions.navigateWithAnimations
import com.felipefernandes.desafiousemobile.extensions.setViewVisibility
import kotlinx.android.synthetic.main.fragment_contact_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Locale

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

        setUi()
    }

    private fun setContactsAdapter(contacts: List<ContactModel>) {
        val contactsAdapter = ContactsAdapter(
            contacts,
            onClick = { id, name ->
                navigateToProfile(id, name)
            },
            onNothingFound = { noResultsFound ->
                setNotFoundTextVisibility(noResultsFound)
            },
        )

        contactListFragmentRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context))
            adapter = contactsAdapter
        }

        contactListFragmentSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                contactsAdapter.filter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun setUi() {
        contactListFragmentSearchInput.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (drawableTouchCondition(event)) {
                    v.performClick()
                    startVoiceToText()
                }
            }
            false
        }
    }

    private fun drawableTouchCondition(
        event: MotionEvent,
        drawableRightConstant: Int = 2
    ): Boolean {
        val editTextBounds =
            contactListFragmentSearchInput.right - contactListFragmentSearchInput.compoundDrawables[drawableRightConstant].bounds.width()
        return event.rawX >= editTextBounds
    }

    private fun startVoiceToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            //alterar p search
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }.also {
            val local = Locale("pt", "BR")
            it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, local)
        }.also {
            it.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.profile_im_listening)
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEAK_TO_TEXT)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "Your device does not support STT.", Toast.LENGTH_LONG).show()
        }
    }

    private fun setObservers() {
        viewModel.isLoading.observe(this, { loading ->
            onLoading(loading)
        })

        viewModel.contactList.observe(this, { contacts ->
            localContactList = contacts
            setContactsAdapter(contacts)
        })
    }

    private fun onLoading(loading: Boolean) {
        if (loading) {
            contactListFragmentPlaceholder.startShimmer()
        } else {
            contactListFragmentPlaceholder.stopShimmer()
        }

        contactListFragmentPlaceholder.setViewVisibility(loading)
        contactListFragmentRecyclerView.setViewVisibility(loading.not())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setObservers()
        loadContacts()
    }

    private fun loadContacts() {
        viewModel.fetchContactList()
    }

    private fun navigateToProfile(id: Int, name: String) {
        val directions = ContactListFragmentDirections
            .actionContactListFragmentToProfileFragment(id, name)

        findNavController().navigateWithAnimations(directions)
    }

    private fun setNotFoundTextVisibility(visible: Boolean) {
        contactListFragmentNotFoundText.setViewVisibility(visible)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEAK_TO_TEXT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (result.isNullOrEmpty().not()) {
                        val recognizedText = result?.get(0)
                        contactListFragmentSearchInput.setText(recognizedText)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.cancelPendingJobs()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        contactListFragmentPlaceholder.startShimmer()
    }

    override fun onPause() {
        contactListFragmentPlaceholder.stopShimmer()
        super.onPause()
    }

    companion object {
        private const val REQUEST_CODE_SPEAK_TO_TEXT = 1
    }
}
