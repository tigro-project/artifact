package com.example.tigro.util.annotation

import android.content.Context
import android.util.Log
import com.example.tigro.data.Contact
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.util.Server.TigroServer
import com.example.tigro.util.crypto.KeyLoader
import com.example.tigro.util.crypto.keyset.SecretKeySet

/**
 * AnnotationManager handles get/submit for annotations. Provides a cleaner interface that can
 * combine interaction with the database and repeated calls to the server
 */
class AnnotationManager {


    private fun getAllContactsFromDB(context: Context): List<Contact> {
        val db: AppDatabase = AppDatabaseSingleton.getDatabase(context)
        var contacts: List<Contact> = db.contactDao().getAllContacts()
        return contacts
    }

    private fun getAllMatchingAnnotations(query: String, contacts: List<Contact>): MutableList<TigroAnnotation> {
        var allAnnotations: MutableList<TigroAnnotation> = mutableListOf()
        for (contact in contacts) {
            try {
                val contactKeyset: SecretKeySet = KeyLoader().getKeySet(contact.securityKeyAlias)
                var mailRaw = TigroServer.sendGetMailRequest(query, contactKeyset)
                val annotation = TigroAnnotation.deserialize(mailRaw)
                allAnnotations.add(annotation)
            } catch (e: Exception) {
                // Do nothing, keep looping
            }
        }
        return allAnnotations
    }

    /**
     * Returns any annotation (across all possible PO boxes) with a matching label
     */
    fun getAnnotationsByLabel(query: String, context: Context): MutableList<TigroAnnotation> {
        // Step 1: get all db contacts
        val contacts = getAllContactsFromDB(context)

        // Step 2: for each contact, try to get label. Parse into tigro annotation
        return getAllMatchingAnnotations(query, contacts)
    }

    fun getAnnotationsForFeed(context: Context): MutableList<TigroAnnotation> {
        // Step 1: get all db contacts
        val contacts = getAllContactsFromDB(context)

        var allAnnotationsInFeed = mutableListOf<TigroAnnotation>()
        for (contact in contacts) {
            val alias: String = "feed-" + contact.securityKeyAlias
            allAnnotationsInFeed.addAll(getAllMatchingAnnotations(alias, contacts))
        }
        return allAnnotationsInFeed
    }

    fun submitAnnotationToBoxes(annotation: TigroAnnotation, targetContacts: List<Contact>) {
        // Step 1: get label and mail from annotation
        val label: String = annotation.label;
        val mail: String = annotation.serialize()

        for (contact in targetContacts) {
            // Step 2: get keyset for each contact
            Log.d("jdebug","security keyset alias ${contact.securityKeyAlias}")
            val contactKeySet: SecretKeySet = KeyLoader().getKeySet(contact.securityKeyAlias)

            // Step 3: submit annotation for each
            try {
                TigroServer.sendDropMailRequest(label, mail, contactKeySet)
            } catch (e: Exception) {
                Log.d("jdebug", e.toString())
                throw e
            }
        }
    }


}