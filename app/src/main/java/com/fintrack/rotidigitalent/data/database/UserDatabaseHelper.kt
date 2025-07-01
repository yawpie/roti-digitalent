package com.fintrack.rotidigitalent.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.util.getColumnIndexOrThrow
import com.fintrack.rotidigitalent.data.User

class UserDatabaseHelper(context: Context)  : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return writableDatabase.insert(TABLE_USERS, null, values)
    }

    fun getUserByUsername(username: String): User? {
        val cursor: Cursor = readableDatabase.query(
            TABLE_USERS,
            null,
            "$COLUMN_NAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        with(cursor) {
            return if (moveToFirst()) {
                val user = User(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    username = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    password = getString(getColumnIndexOrThrow(COLUMN_PASSWORD))
                )
                close()
                user
            } else {
                close()
                null
            }
        }
    }


    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val cursor: Cursor = readableDatabase.query(TABLE_USERS, null, null, null, null, null, "$COLUMN_ID DESC")
        with(cursor) {
            while (moveToNext()) {
                val user = User(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    username = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    password = getString(getColumnIndexOrThrow(COLUMN_PASSWORD))
                )
                users.add(user)
            }
            close()
        }
        return users
    }

    fun updateUser(user: User): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.username)
            put(COLUMN_PASSWORD, user.password)
        }
        return writableDatabase.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(user.id.toString()))
    }

    fun deleteUser(id: Int): Int {
        return writableDatabase.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }



    companion object {
        const val DATABASE_NAME = "bakery.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USERS = "user"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "username"
        const val COLUMN_PASSWORD = "password"
    }
}