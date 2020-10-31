package com.felipefernandes.desafiousemobile.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.api.viewModels.ContactsViewModel
import com.felipefernandes.desafiousemobile.entities.contact.ContactWithAboutModel
import com.felipefernandes.desafiousemobile.extensions.loadImage
import com.felipefernandes.desafiousemobile.extensions.setViewVisibility
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.placeholder_fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private val viewModel: ContactsViewModel by viewModel()
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactId = args.contactId

        setObservers()
        loadContact(contactId)
    }

    private fun setObservers() {
        viewModel.isLoading.observe(this, { loading ->
            onLoading(loading)
        })

        viewModel.singleContact.observe(this, { contact ->
            setUi(contact)
        })
    }

    private fun onLoading(loading: Boolean) {
        if (loading) {
            placeholderProfileFragment.startShimmer()
        } else {
            placeholderProfileFragment.stopShimmer()
        }

        placeholderProfileFragment.setViewVisibility(loading)
        profileFragmentContactRoot.setViewVisibility(loading.not())
    }

    private fun setUi(contact: ContactWithAboutModel) {
        profileFragmentContactAvatar.loadImage(contact.photo, true)
        profileFragmentContactName.text = contact.name
        profileFragmentContactEmail.text = contact.email
        profileFragmentContactAbout.text = contact.about
    }

    private fun loadContact(id: Int) {
        viewModel.fetchContact(id)
    }

    override fun onDestroy() {
        viewModel.cancelPendingJobs()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        placeholderProfileFragment.startShimmer()
    }

    override fun onPause() {
        placeholderProfileFragment.stopShimmer()
        super.onPause()
    }
}
