package mp.iamuproject

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import mp.iamuproject.dao.HNRepository
import mp.iamuproject.factory.getHNRepository
import mp.iamuproject.model.Item

private const val AUTHORITY = "mp.iamuproject.api.provider"
private const val PATH = "items"
val HN_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 20
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)){
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class HNProvider : ContentProvider() {

    private lateinit var repository: HNRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.delete(selection, selectionArgs)
            // "mp.iamuproject.api.provider/items/55"
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.delete("${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }

    override fun getType(uri: Uri): String? {
        return when (URI_MATCHER.match(uri)) {
            ITEMS -> "vnd.android.cursor.dir/vnd.mp.iamuproject.items"
            ITEM_ID -> "vnd.android.cursor.item/vnd.mp.iamuproject.items"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(HN_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getHNRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? = repository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.update(values, selection, selectionArgs)
            // "mp.iamuproject.api.provider/items/55"
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.update(values, "${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }
}