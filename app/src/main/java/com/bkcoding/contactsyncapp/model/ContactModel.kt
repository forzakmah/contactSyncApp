package com.bkcoding.contactsyncapp.model

import com.bkcoding.contactsyncapp.db.entity.ContactEntity

data class ContactModel(
    val id: Long,
    val displayName: String,
    val givenName: String? = null,
    val middleName: String? = null,
    val familyName: String? = null,
    var phoneNumbers: List<String> = listOf()
)

fun ContactModel.asEntity(): ContactEntity {
    return ContactEntity(
        id,
        displayName,
        givenName,
        middleName,
        familyName,
        phoneNumbers
    )
}