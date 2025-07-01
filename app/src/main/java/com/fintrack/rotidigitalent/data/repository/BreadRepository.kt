package com.fintrack.rotidigitalent.data.repository

import com.fintrack.rotidigitalent.data.Bread
import com.fintrack.rotidigitalent.data.database.BreadDatabaseHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreadRepository @Inject constructor(
    private val dbHelper: BreadDatabaseHelper
) {

    fun getAllBreads(): List<Bread> {
        return dbHelper.getAllBreads()
    }

    fun addBread(name: String, description: String, image: String): Long {
        val bread = Bread(0, name, description, image)
        return dbHelper.insertBread(bread)
    }

    fun updateBread(bread: Bread): Int {
        return dbHelper.updateBread(bread)
    }

    fun deleteBread(id: Int): Int {
        return dbHelper.deleteBread(id)
    }
}
