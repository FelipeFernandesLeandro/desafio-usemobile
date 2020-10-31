package com.felipefernandes.desafiousemobile.entities.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.felipefernandes.desafiousemobile.R
import com.felipefernandes.desafiousemobile.extensions.loadImage
import java.util.*

class ContactsAdapter(
    private val contacts: List<ContactModel>,
    private val onClick: (Int, String) -> Unit,
    private val onNothingFound: (Boolean) -> Unit,
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {
    private var searchableList: MutableList<ContactModel> = arrayListOf()

    init {
        searchableList.addAll(contacts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchableList[position])
    }

    override fun getItemCount() = searchableList.size

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var imageAvatar: ImageView = item.findViewById(R.id.contactItemAvatarImage)
        private var nameTextView: TextView = item.findViewById(R.id.contactItemNameText)
        private var emailTextView: TextView = item.findViewById(R.id.contactItemEmailText)

        fun bind(contact: ContactModel) {
            imageAvatar.loadImage(contact.photo, true)
            nameTextView.text = contact.name
            emailTextView.text = contact.email

            if (contact.isVerified) {
                nameTextView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_outline_24,
                    0
                )
                nameTextView.compoundDrawablePadding =
                    nameTextView.resources.getDimension(R.dimen.item_contact_name_to_check_margin)
                        .toInt()
            }

            itemView.setOnClickListener {
                onClick(contact.id, contact.name)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()

                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(contacts)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }

                    for (item in 0..contacts.size) {
                        val match = contacts[item].name.toLowerCase(Locale.getDefault())
                            .contains(filterPattern)
                        if (match) {
                            searchableList.add(contacts[item])
                        }
                    }
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val noResultsFound = searchableList.isNullOrEmpty()
                onNothingFound(noResultsFound)

                notifyDataSetChanged()
            }
        }
    }
}
