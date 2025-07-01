package com.fintrack.rotidigitalent.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fintrack.rotidigitalent.data.Bread

class BreadDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "bakery.db"
        const val DATABASE_VERSION = 1

        const val TABLE_BREADS = "breads"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_BREADS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BREADS")
        onCreate(db)
    }

    fun insertBread(bread: Bread): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, bread.name)
            put(COLUMN_DESCRIPTION, bread.description)
            put(COLUMN_IMAGE, bread.imageUrl)
        }
        return writableDatabase.insert(TABLE_BREADS, null, values)
    }

    fun getAllBreads(): List<Bread> {
        val breads = mutableListOf<Bread>()
        val cursor = readableDatabase.query(
            TABLE_BREADS, null, null, null, null, null, "$COLUMN_ID DESC"
        )
        with(cursor) {
            while (moveToNext()) {
                val bread = Bread(
                    getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    getString(getColumnIndexOrThrow(COLUMN_IMAGE))
                )
                breads.add(bread)
            }
            close()
        }
        return breads
    }

    fun updateBread(bread: Bread): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, bread.name)
            put(COLUMN_DESCRIPTION, bread.description)
            put(COLUMN_IMAGE, bread.imageUrl)
        }
        return writableDatabase.update(TABLE_BREADS, values, "$COLUMN_ID = ?", arrayOf(bread.id.toString()))
    }

    fun deleteBread(id: Int): Int {
        return writableDatabase.delete(TABLE_BREADS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
