package com.example.contatospdm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.contatospdm.R
import com.example.contatospdm.adapter.ContactAdapter
import com.example.contatospdm.controller.ContactController
import com.example.contatospdm.databinding.ActivityMainBinding
import com.example.contatospdm.model.Constant.EXTRA_CONTACT
import com.example.contatospdm.model.Constant.VIEW_CONTACT
import com.example.contatospdm.model.Contact

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data source
    private val contactList: MutableList<Contact> by lazy {
        contactController.getContacts()
    }

    //Controller
    private val contactController: ContactController by lazy {
        ContactController(this)
    }

    //Adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            this,
            contactList
        )
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)

        //fillContacts()
        amb.contatoLv.adapter = contactAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        { result ->
            if(result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                contact?.let{_contact ->
                    if (contactList.any { it.id == contact.id }) {
                        val position = contactList.indexOfFirst { it.id == contact.id }
                        contactList[position] = _contact
                        contactList.sortBy { it.name }
                        contactController.editContact(_contact)
                    }
                    else {
                        val newId = contactController.insertContact(_contact)
                        val newContact = Contact(
                            newId,
                            _contact.name,
                            _contact.address,
                            _contact.phone,
                            _contact.email
                        )
                        contactList.add(newContact)
                    }
                    contactList.sortBy { it.name }
                    contactAdapter.notifyDataSetChanged()
                }
            }

        }

        amb.contatoLv.setOnItemClickListener { parent, view, position, id ->
            val contact = contactList[position]
            val viewContactIntent = Intent(this, ContactActivity::class.java)
            viewContactIntent.putExtra(EXTRA_CONTACT, contact)
            viewContactIntent.putExtra(VIEW_CONTACT, true)
            startActivity(viewContactIntent)
        }

        registerForContextMenu(amb.contatoLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val contact = contactList[position]

        return when (item.itemId){
            R.id.removeContactMI -> {
                contactController.removeContact(contact.id)
                contactList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contact Removed.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMI -> {
                val editContactIntent = Intent(this, ContactActivity::class.java)
                editContactIntent.putExtra(EXTRA_CONTACT, contact)
                carl.launch(editContactIntent)
                true
            }
            else -> { false }
        }
    }

    private fun fillContacts() {
        for (i in 1..50){
            contactList.add(
                Contact(
                    i,
                    "Nome $i",
                    "Endereço $i",
                    "Telefone $i",
                    "Email $i"
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.contatoLv)
    }
}