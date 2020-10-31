package com.felipefernandes.desafiousemobile.entities.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.extensions.loadImage

class ContactsAdapter(
    private val contacts: List<ContactModel>,
    private val onClick: (Int, String) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var imageAvatar: ImageView = item.findViewById(R.id.contactItemAvatarImage)
        private var nameTextView: TextView = item.findViewById(R.id.contactItemNameText)
        private var emailTextView: TextView = item.findViewById(R.id.contactItemEmailText)

        fun bind(contact: ContactModel) {
            imageAvatar.loadImage(contact.photo, true)
            nameTextView.text = contact.name
            emailTextView.text = contact.email

            if (contact.isVerified) {
                nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_outline_24, 0)
                nameTextView.compoundDrawablePadding = nameTextView.resources.getDimension(R.dimen.item_contact_name_to_check_margin).toInt()
            }

            itemView.setOnClickListener {
                onClick(contact.id, contact.name)
            }
        }
    }
}
