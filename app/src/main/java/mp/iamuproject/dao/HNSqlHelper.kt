package mp.iamuproject.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import mp.iamuproject.model.Item

private const val DB_NAME = "items.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "items"
private val CREATE_TABLE = """
    CREATE TABLE $TABLE_NAME (
        ${Item::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Item::title.name} TEXT NOT NULL,
        ${Item::description.name} TEXT NOT NULL,
        ${Item::picturePath.name} TEXT NOT NULL,
        ${Item::date.name} TEXT NOT NULL,
        ${Item::read.name} INTEGER NOT NULL,
        ${Item::author.name} TEXT NOT NULL,
        ${Item::guid.name} TEXT,
        ${Item::url.name} TEXT
    )""".trimIndent()
private const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

class HNSqlHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DB_NAME,
    null,
    DB_VERSION
), HNRepository {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?) = writableDatabase.delete(
        TABLE_NAME,
        selection,
        selectionArgs
    )

    override fun insert(values: ContentValues?) = writableDatabase.insert(
        TABLE_NAME,
        null,
        values
    )

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?,
    ) = writableDatabase.update(
        TABLE_NAME,
        values,
        selection,
        selectionArgs
    )

}