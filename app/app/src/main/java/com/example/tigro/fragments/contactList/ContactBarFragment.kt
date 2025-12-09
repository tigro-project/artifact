package com.example.tigro.fragments.contactList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.data.Contact
import com.example.tigro.fragments.ContactNotesPopupFragment
import com.example.tigro.util.annotation.SendAnnotation.ContactBarFragmentCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactBarFragment : Fragment() {

    private lateinit var contactBarRootLayout: FrameLayout
    private lateinit var contactNameTextView: TextView
    private lateinit var contactBarBtn: Button
    private lateinit var contactBarCheckbox: CheckBox

    private lateinit var contact: Contact

    private var callback: ContactBarFragmentCallback? = null

    fun setCallback(callback: ContactBarFragmentCallback) {
        this.callback = callback
    }
    companion object {
        fun newInstance(contact: Contact): ContactBarFragment {
            val fragment = ContactBarFragment()
            fragment.contact = contact
            return fragment
        }
    }

    // TODO: smaller ? button, highlight bar when clicked
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_bar, container, false)

        contactBarRootLayout = view.findViewById(R.id.contactBarRootLayout)
        contactBarRootLayout.setOnClickListener { contactBarClicked() }

        contactNameTextView = view.findViewById(R.id.contactBarName)
        setContactName()

        contactBarBtn = view.findViewById(R.id.contactBarBtn)
        contactBarBtn.setOnClickListener { displayContactNotesClicked() }

        contactBarCheckbox = view.findViewById(R.id.contactBarCheckbox)

        return view
    }


    private fun contactBarClicked() {
        println("contact bar clicked")

        if (!contactBarCheckbox.isChecked) {
            // contact selected
            contactBarCheckbox.visibility = View.VISIBLE
            contactBarCheckbox.isChecked = true
            callback?.onContactSelected(contact)
        } else {
            // contact de-selected
            contactBarCheckbox.isChecked = false
            contactBarCheckbox.visibility = View.INVISIBLE
            callback?.onContactDeselected(contact)
        }
    }

    private fun setContactName() {
        contactNameTextView.text = contact.name
    }

    // Opens a fragment with notes on the current contact
    // Looks through DB to find appropriate contact by contact ID
    private fun displayContactNotesClicked() {
        lifecycleScope.launch {
            val reloadedContact = reloadContactFromDB()
            val contactNotesPopupFragment = ContactNotesPopupFragment.newInstance(reloadedContact)
            val fragmentManager = requireActivity().supportFragmentManager
            contactNotesPopupFragment.show(fragmentManager, "ContactNotesPopupFragment")
        }
    }

    // TODO: this method is slow, since it must access the DB every time the note button is pressed. Maybe can use caching/viewmodel?
    private suspend fun reloadContactFromDB(): Contact {
        return withContext(Dispatchers.IO) {
            val db: AppDatabase = AppDatabaseSingleton.getDatabase(requireContext())
            return@withContext db.contactDao().getContactByID(contact.id)
        }
    }

}