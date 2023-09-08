package com.bkcoding.contactsyncapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bkcoding.contactsyncapp.model.ContactModel
import java.util.Date

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "given_name")
    val givenName: String? = null,
    @ColumnInfo(name = "middle_name")
    val middleName: String? = null,
    @ColumnInfo(name = "family_name")
    val familyName: String? = null,
    @ColumnInfo(name = "phone_numbers")
    val phoneNumbers: List<String>,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Date? = null,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false
)

fun ContactEntity.asExternalModel(): ContactModel {
    return ContactModel(
        id,
        displayName,
        givenName,
        middleName,
        familyName,
        phoneNumbers
    )
}