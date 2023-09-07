package com.bkcoding.contactsyncapp.repository

import com.bkcoding.contactsyncapp.db.dao.ContactDao
import com.bkcoding.contactsyncapp.db.entity.ContactEntity
import com.bkcoding.contactsyncapp.db.entity.asExternalModel
import com.bkcoding.contactsyncapp.model.ContactModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface IContactRepository {
    fun fetchContacts(query: String = ""): Flow<List<ContactModel>>
    suspend fun insertContacts(contacts: List<ContactEntity>): List<Long>
    suspend fun fetchContactsAsList(): List<ContactModel>
    suspend fun deleteContacts(ids: List<String>)
    fun count(): Flow<Int>
}

@Singleton
class ContactRepository @Inject constructor(
    private val dao: ContactDao
) : IContactRepository {

    override fun fetchContacts(query: String): Flow<List<ContactModel>> {
        val flow = if (query.isEmpty())
            dao.getContactEntities()
        else
            dao.getContactEntities(query = query)

        return flow.map { contacts ->
            contacts.map { contactEntity ->
                contactEntity.asExternalModel()
            }
        }
    }

    override suspend fun fetchContactsAsList(): List<ContactModel> {
        return dao.getContactEntitiesAsList().map { it.asExternalModel() }
    }

    override suspend fun insertContacts(contacts: List<ContactEntity>) =
        dao.insertContacts(contactEntities = contacts)

    override suspend fun deleteContacts(ids: List<String>) {
        dao.deleteContacts(ids = ids)
    }

    override fun count(): Flow<Int> {
        return dao.getCount()
    }
}