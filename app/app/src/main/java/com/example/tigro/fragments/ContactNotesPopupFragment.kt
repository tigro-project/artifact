package com.example.tigro.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.data.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: auto update notes
class ContactNotesPopupFragment : DialogFragment() {

    private lateinit var contactNotesContactNameText: TextView
    private lateinit var editTextNotesPopup: EditText
    private lateinit var notesPopupCloseBtn: Button

    private lateinit var contact: Contact
    companion object {
        fun newInstance(contact: Contact): ContactNotesPopupFragment {
            val fragment = ContactNotesPopupFragment()
            fragment.contact = contact
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_notes_popup, container, false)

        contactNotesContactNameText = view.findViewById(R.id.contactNotesContactNameText)
        editTextNotesPopup = view.findViewById(R.id.editTextNotesPopup)
        loadNotesContent()

        notesPopupCloseBtn = view.findViewById(R.id.notesPopupCloseBtn)
        notesPopupCloseBtn.setOnClickListener { notesPopupCloseBtnClicked() }

        return view
    }

    private fun notesPopupCloseBtnClicked() {
        dismiss()
    }

    private fun loadNotesContent() {
        contactNotesContactNameText.setText(contact.name)
        editTextNotesPopup.setText(contact.notes)
    }

    /**  Executes whenever the DialogFragment is dismissed, either from close button clicked or
     * from a click elsewhere on the screen
     *
     * Saves changes to the contents of the note before closing
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // TODO: must save edited contents of popup
        updateContactInDB()
        println("notes popup dismissed")
    }

    private fun updateContactInDB() {
        val tempContact = Contact(contact.name, editTextNotesPopup.text.toString(), contact.securityKeyAlias, contact.id)
        lifecycleScope.launch(Dispatchers.IO) {
            val db: AppDatabase = AppDatabaseSingleton.getDatabase(requireContext())
            db.contactDao().updateContact(tempContact)
        }
    }


}