package com.example.tigro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.activities.MainActivity
import com.example.tigro.data.Contact
import com.example.tigro.data.TargetGroup
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NameNewContactGroupFragment : DialogFragment() {

    private lateinit var createGroupNameEditText: EditText
    private lateinit var createGroupNotesEditText: EditText
    private lateinit var createGroupCancelBtn: Button
    private lateinit var createGroupApplyBtn: Button

    private lateinit var targetGroup: TargetGroup

    fun newInstance(targetGroup: TargetGroup): NameNewContactGroupFragment {
        val fragment = NameNewContactGroupFragment()
        fragment.targetGroup = targetGroup
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_name_new_contact_group, container, false)

        createGroupNameEditText = view.findViewById(R.id.createGroupNameEditText)
        createGroupNotesEditText = view.findViewById(R.id.createGroupNotesEditText)
        createGroupCancelBtn = view.findViewById(R.id.createGroupCancelBtn)
        createGroupApplyBtn = view.findViewById(R.id.createGroupApplyBtn)

        createGroupCancelBtn.setOnClickListener { createGroupCancelBtnClicked() }
        createGroupApplyBtn.setOnClickListener { createGroupApplyBtnClicked() }

        setNotesContent()

        return view
    }

    private fun setNotesContent() {
        var notesContent: String = "Members: \n"
        for (target in targetGroup.getAllContacts()) {
            notesContent += target.name + "\n"
        }
        createGroupNotesEditText.setText(notesContent)
    }

    // TODO: add contact group, not contact
    private fun createGroupApplyBtnClicked() {
        Toast.makeText(requireContext(), "Group Created", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), MainActivity::class.java))

        // TODO: implement security key
        val newContact = Contact(
            createGroupNameEditText.text.toString(),
            createGroupNotesEditText.text.toString(),
            "NONE"
        )
        val db: AppDatabase = AppDatabaseSingleton.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            db.contactDao().insertContact(newContact)
        }
        dismiss()
    }

    private fun createGroupCancelBtnClicked() {
        dismiss()
    }


}