package com.bkcoding.contactsyncapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bkcoding.contactsyncapp.db.entity.ContactEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * DAO for [ContactEntity]
 */
@Dao
interface ContactDao {
    @Query(value = "SELECT * FROM contacts where is_deleted = 0")
    fun getContactEntities(): Flow<List<ContactEntity>>

    @Query(
        """
        SELECT * FROM contacts where phone_numbers
        LIKE '%' || :query || '%' OR display_name LIKE '%' || :query || '%' 
        and is_deleted = 0
         """
    )
    fun getContactEntities(query: String): Flow<List<ContactEntity>>

    @Query(value = "SELECT * FROM contacts where is_deleted = 0")
    suspend fun getContactEntitiesAsList(): List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE id IN (:ids) and is_deleted = 0")
    fun getContactEntities(ids: Set<String>): Flow<List<ContactEntity>>

    /**
     * Inserts [ContactEntity] into the db if they don't exist, replace them
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contactEntities: List<ContactEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertContacts(entities: List<ContactEntity>)

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query("UPDATE contacts set is_deleted = 1, deleted_at = :date WHERE id in (:ids) and is_deleted = 0")
    suspend fun deleteContacts(ids: List<String>, date: Date)

    /**
     * return number of contacts inside database
     */
    @Query("SELECT count(*) FROM contacts where is_deleted = 0")
    fun getCount(): Flow<Int>
}