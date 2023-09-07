package com.bkcoding.contactsyncapp.adapter

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
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
    /**
     * Return list of deleted contacts
     */
    private fun fetchDeletedContacts(): List<String> {
        val deletedContactsCursor = context.contentResolver.query(
            ContactsContract.DeletedContacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val deletedContacts = mutableListOf<String>()
        deletedContactsCursor?.let {
            val id = deletedContactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID)
            while (deletedContactsCursor.moveToNext()) {
                val contactId = deletedContactsCursor.getString(id)
                deletedContacts.add(contactId)
            }
            deletedContactsCursor.close()
        }
        return deletedContacts
    }

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

    private fun fetchDeviceContacts(): List<ContactModel> {
        val cursorContact = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val deviceContacts = mutableListOf<ContactModel>()
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
                deviceContacts.add(newContact)
            }
            cursorContact.close()
        }
        return deviceContacts
    }

    fun syncContacts() {
        /**
         * Sync new or updated contacts inside local database
         */
        val deviceContacts = fetchDeviceContacts()
        if (deviceContacts.isNotEmpty()) {
            Log.e("device contacts", "Size ${deviceContacts.size}")
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                val dbContacts = contactRepository.fetchContactsAsList()
                Log.e("db contacts", "Size ${dbContacts.size}")
                /**
                 * contains function will check if the item exits or not
                 * and also can inform us if object has been edited with the hashCode
                 */
                val newContacts = deviceContacts.mapNotNull { contact ->
                    if (!dbContacts.contains(contact))
                        contact
                    else
                        null
                }
                Log.e("updated of new items", "${newContacts.size}")
                /**
                 * Save new and updated contacts
                 */
                if (newContacts.isNotEmpty()) {
                    contactRepository.insertContacts(
                        newContacts.map { contactModel ->
                            contactModel.asEntity()
                        }
                    )
                }
            }
        }

        /**
         * mark removed app contacts as is_deleted = true in the local database
         */
        val deletedContacts = fetchDeletedContacts()
        if (deletedContacts.isNotEmpty()) {
            Log.e("items to remove", deletedContacts.toString())
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                contactRepository.deleteContacts(
                    ids = deletedContacts
                )
            }
        }
    }
}