package com.felipefernandes.desafiousemobile.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.felipefernandes.desafiousemobile.R
import kotlinx.android.synthetic.main.fragment_contact_list.*

class ContactListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentContactListButtonNext.setOnClickListener {
            findNavController().navigate(R.id.action_contactListFragment_to_profileFragment)
        }
    }
}
