package com.example.tigro.data

// TODO: change contact to target
class TargetGroup {

    private val contacts: MutableList<Contact> = mutableListOf()

    fun addContact(contact: Contact) {
        contacts.add(contact)
    }

    fun removeContact(contact: Contact) {
        contacts.remove(contact)
    }
    fun getAllContacts(): List<Contact> {
        return contacts.toList()
    }

    override fun toString(): String {
        var contactNames: MutableList<String> = mutableListOf()
        for (contact in contacts) {
            contactNames.add(contact.name)
        }
        return "TargetGroup: $contactNames"
    }
}