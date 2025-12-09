package com.example.tigro.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.SHARED_KEYSET_INTENT
import com.example.tigro.data.Contact
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.util.crypto.KeyLoader
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.example.tigro.util.overwrite.MAX_LENGTH_STRING
import com.example.tigro.util.overwrite.OverwriteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Comes from: ScanQrActivity
 * Leads to: MainActivity (cancel, submit)
 *
 * on successful submission, a contact is added to the contact database
 */
class AddContactActivity : AppCompatActivity(), OverwriteListener {


    private lateinit var cancelContactBtn: Button
    private lateinit var submitContactBtn: Button
    private lateinit var editTextAlias: EditText
    private lateinit var editTextNotes: EditText
    private lateinit var sharedKeyTextView: TextView

    private lateinit var sharedKeyAlias: String
    private lateinit var sharedKeys: SecretKeySet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        cancelContactBtn = findViewById(R.id.cancelContactBtn)
        submitContactBtn = findViewById(R.id.submitContactBtn)
        cancelContactBtn.setOnClickListener { cancelContactBtnClicked() }
        submitContactBtn.setOnClickListener { submitContactBtnClicked() }

        editTextAlias = findViewById(R.id.editTextAlias)
        editTextNotes = findViewById(R.id.editTextNotes)
        sharedKeyTextView = findViewById(R.id.textViewSharedKey)


        val extras = intent.extras
        if (extras == null) {
            throw Exception("shared keys were not found - missing in intent")
        } else {
            sharedKeys = extras.getSerializable(SHARED_KEYSET_INTENT) as SecretKeySet
            Log.d("DEBUG", "SHARED KEYS: $sharedKeys")
        }
    }

    private fun cancelContactBtnClicked() {
        println("cancel btn clicked")
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun submitContactBtnClicked() {
        println("submit btn clicked")
        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))

        val contactName = editTextAlias.text.toString()
        this.sharedKeyAlias = contactName + "-" + UUID.randomUUID().toString().substring(0, 6)

        val newContact = Contact(contactName, editTextNotes.text.toString(), sharedKeyAlias)
        val db: AppDatabase = AppDatabaseSingleton.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            val addContactJob = async { addContactToDatabase(db, newContact) }
            val keyLoadJob = async { KeyLoader().loadKeySet(sharedKeyAlias, sharedKeys) }

            val contactAdded = addContactJob.await()
            val keyLoaded = keyLoadJob.await()

            Log.d("jdebug", "added contact $contactName $sharedKeyAlias")
            Log.d("jdebug", "loaded key for $contactName")
        }
    }

    private fun addContactToDatabase(database: AppDatabase, newContact: Contact) {
        database.contactDao().insertContact(newContact)
        val contacts = database.contactDao().getAllContacts()
        println(contacts)
    }

    override fun onRequestOverwrite() {
        editTextAlias.setText(MAX_LENGTH_STRING)
        editTextNotes.setText(MAX_LENGTH_STRING)
        println("current notes: " + editTextNotes.text.toString())
    }
}