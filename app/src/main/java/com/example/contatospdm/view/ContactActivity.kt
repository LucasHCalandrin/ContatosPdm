package com.example.contatospdm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.contatospdm.R
import com.example.contatospdm.databinding.ActivityContactBinding
import com.example.contatospdm.model.Constant.EXTRA_CONTACT
import com.example.contatospdm.model.Constant.INVALID_CONTACT_ID
import com.example.contatospdm.model.Constant.VIEW_CONTACT
import com.example.contatospdm.model.Contact
import java.util.Random

class ContactActivity : AppCompatActivity() {

    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Contact Details"

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let { _receivedContact ->
            val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
            with(acb) {
                if(viewContact){
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled= false
                    saveBt.visibility= View.GONE

                }
                nameEt.setText(_receivedContact.name)
                addressEt.setText(_receivedContact.address)
                phoneEt.setText(_receivedContact.phone)
                emailEt.setText(_receivedContact.email)
            }
        }

        with (acb) {
            saveBt.setOnClickListener {
                val contact = Contact(
                    id = receivedContact?.id?: INVALID_CONTACT_ID,
                    name = nameEt.text.toString(),
                    address = addressEt.text.toString(),
                    phone = phoneEt.text.toString(),
                    email = emailEt.text.toString()
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    //private fun generateId() = Random(System.currentTimeMillis()).nextInt()
}