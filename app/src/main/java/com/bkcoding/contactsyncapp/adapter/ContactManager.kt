package com.bkcoding.contactsyncapp.adapter

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.bkcoding.contactsyncapp.model.ContactModel
import com.bkcoding.contactsyncapp.model.asEntity
import com.bkcoding.contactsyncapp.repository.ContactRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactRepository: ContactRepository,
) {
    private fun fetchContactPhoneNumbers(
        contactId: String
    ): List<String> {
        val cursorPhone = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
            arrayOf(contactId),
            null
        )
        val phoneNumbers = mutableListOf<String>()
        cursorPhone?.let {
            while (cursorPhone.moveToNext()) {
                val pnString = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumValue = cursorPhone.getString(pnString)
                phoneNumbers.add(phoneNumValue)
            }
            /**
             * close cursor
             */
            cursorPhone.close()
        }
        /**
         * return list of phoneNumber
         */
        return phoneNumbers
    }

    private fun fetchContactName(
        contactId: String
    ): Triple<String?, String?, String?> {
        val whereName = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?"
        val whereNameParams = arrayOf(contactId)
        val nameCur: Cursor? = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            whereName,
            whereNameParams,
            ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID
        )
        var names = Triple<String?, String?, String?>(null, null, null)
        nameCur?.let {
            val givenNameIndex = nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
            val familyNameIndex = nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
            val middleNameIndex = nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
            while (nameCur.moveToNext()) {
                val givenName = nameCur.getString(givenNameIndex)
                val familyName = nameCur.getString(familyNameIndex)
                val middleName = nameCur.getString(middleNameIndex)
                names = names.copy(givenName, middleName, familyName)
            }
            nameCur.close()
        }
        return names
    }

    fun populateDatabase() {
        val resolver: ContentResolver = context.contentResolver
        val cursorContact = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val contacts = mutableListOf<ContactModel>()
        cursorContact?.let {
            while (cursorContact.moveToNext()) {

                val idColumnIndex = cursorContact.getColumnIndex(ContactsContract.Contacts._ID)
                val displayNameIndex = cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val phoneNumbersIndex = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                val id = cursorContact.getString(idColumnIndex)
                val displayName = cursorContact.getString(displayNameIndex)
                val hasPhoneNumber = cursorContact.getString(phoneNumbersIndex).toInt()

                val names = fetchContactName(id)

                val newContact = ContactModel(
                    id = id.toLong(),
                    displayName = displayName,
                    givenName = names.first,
                    middleName = names.second,
                    familyName = names.third,
                    phoneNumbers = if (hasPhoneNumber > 0)
                        fetchContactPhoneNumbers(contactId = id)
                    else
                        emptyList()
                )
                contacts.add(newContact)
            }
            cursorContact.close()

            /**
             * Save contacts inside database
             */
            if (contacts.isNotEmpty()) {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    contactRepository.insertContacts(
                        contacts.map { it.asEntity() }.toList()
                    )
                }
            }
        }
    }
}