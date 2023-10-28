package com.example.contatospdm.controller

import com.example.contatospdm.model.Contact
import com.example.contatospdm.model.ContactDao
import com.example.contatospdm.model.ContactDaoSQLite
import com.example.contatospdm.view.MainActivity

class ContactController(mainActivity: MainActivity) {
    private val contactDaoImpl : ContactDao = ContactDaoSQLite(mainActivity)

    fun insertContact(contact: Contact): Int = contactDaoImpl.createContact(contact)
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() = contactDaoImpl.retrieveContacts()
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(id: Int) = contactDaoImpl.deleteContact(id)
}