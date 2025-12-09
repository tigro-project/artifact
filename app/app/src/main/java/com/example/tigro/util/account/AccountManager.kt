package com.example.tigro.util.account

import android.content.Context
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * AccountManager allows users to delete part or all of their account
 * Also allows logout
 */
class AccountManager(private val context: Context) {

    // TODO: implement user logout, including encrypt db
    fun logout() {
        println("logout not yet implemented")
    }

    /**
     * fully delete account, including:
     *  - delete all contacts
     *  - delete all annotations from TIGRO server
     *  - delete PIN and Nuclear PIN
     */
    fun deleteAccount() {
        deleteAccountData()
        val pm = PasswordManager(context)
        pm.destroyPin()
        pm.destroyNuclearPin()
    }

    /**
     * wipe account data, in this current implementation, that involves:
     *  - delete all contacts
     *  - delete all annotations from TIGRO server
     *
     *  NOTE: it may be better to replace contacts with dummy contacts, or some approach similar
     */
    fun wipeAccountData() {
        deleteAccountData()
    }

    // deletes contacts and annotations
    private fun deleteAccountData() {
        // TODO: GlobalScope is messy and could create a resource leak :(
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val db: AppDatabase = AppDatabaseSingleton.getDatabase(context)
                db.contactDao().deleteAllContacts()
                // TODO: Delete annotations from TIGRO server here
            }
        }
    }
}