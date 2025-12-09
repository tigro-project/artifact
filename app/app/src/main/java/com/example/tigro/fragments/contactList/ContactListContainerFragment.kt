package com.example.tigro.fragments.contactList

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.data.Contact
import com.example.tigro.util.annotation.SendAnnotation.ContactBarFragmentCallback
import com.example.tigro.data.TargetGroup
import com.example.tigro.util.annotation.SetAnnotation.TigroAnnotationListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactListContainerFragment : Fragment(), ContactBarFragmentCallback {

    private var annotationListener: TigroAnnotationListener? = null
    private var targetGroupHolder: TargetGroup = TargetGroup()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TigroAnnotationListener) {
            annotationListener = context
        } else {
            throw IllegalStateException("The fragment must implement TigroAnnotationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list_container, container, false)
        val llItemsContainer: LinearLayout = view.findViewById(R.id.llItemsContainer)

        lifecycleScope.launch {
            val contacts = getAllContactsFromDB()

            for (contact in contacts) {
                val itemFragment = ContactBarFragment.newInstance(contact)
                itemFragment.setCallback(this@ContactListContainerFragment)

                childFragmentManager.beginTransaction()
                    .add(llItemsContainer.id, itemFragment)
                    .commit()
            }
        }
        return view
    }

    /**
     * loads contacts from database, called on start
     */
    private suspend fun getAllContactsFromDB(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val db: AppDatabase = AppDatabaseSingleton.getDatabase(requireContext())
            return@withContext db.contactDao().getAllContacts()
        }
    }


    public fun getNewTargetGroup(): TargetGroup {
        return targetGroupHolder
    }

    override fun onContactSelected(contact: Contact) {
        targetGroupHolder.addContact(contact)
        setTargetContact()
        println(targetGroupHolder)
    }

    override fun onContactDeselected(contact: Contact) {
        targetGroupHolder.removeContact(contact)
        setTargetContact()
        println(targetGroupHolder)
    }

    /**
     * Triggers the callback in SubmitAnnotationActivity
     */
    private fun setTargetContact() {
        annotationListener?.setContactTargetList(targetGroupHolder.getAllContacts())
    }
}