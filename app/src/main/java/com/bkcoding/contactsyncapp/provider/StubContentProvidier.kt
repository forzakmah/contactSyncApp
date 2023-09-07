package com.bkcoding.contactsyncapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/*
 * Define an implementation of ContentProvider that stubs out
 * all methods
 */
class StubProvider : ContentProvider() {

    /*
    * Always return true, indicating that the
    * provider loaded correctly.
    */
    override fun onCreate(): Boolean {
        return true
    }

    /*
    * query() always returns no results
    */
    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return null
    }

    /*
     * Return no type for MIME type
     */
    override fun getType(uri: Uri): String? = null

    /*
     * insert() always returns null (no URI)
    */
    override fun insert(
        uri: Uri,
        contentValues: ContentValues?
    ): Uri? {
        return null
    }


    /*
    * delete() always returns "no rows affected" (0)
    */
    override fun delete(
        uri: Uri,
        str: String?,
        array: Array<out String>?
    ): Int = 0


    /*
    * update() always returns "no rows affected" (0)
    */
    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0
}