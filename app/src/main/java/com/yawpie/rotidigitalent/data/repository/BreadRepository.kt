package com.yawpie.rotidigitalent.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.yawpie.rotidigitalent.data.Bread
import com.yawpie.rotidigitalent.data.database.BreadDatabaseHelper
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await


@Singleton
class BreadRepository @Inject constructor(
    private val dbHelper: BreadDatabaseHelper
) {

    fun getAllBreads(): List<Bread> {
        return dbHelper.getAllBreads()
    }

    suspend fun addBreadWithImageUpload(
        name: String,
        description: String,
        imageUri: Uri
    ): Result<Boolean> {
        // 2. GUNAKAN try-catch untuk error handling yang lebih bersih
        return try {
            val fileName = "bread_images/${UUID.randomUUID()}.jpg"
            val storageRef = FirebaseStorage.getInstance().getReference(fileName)

            // 3. GUNAKAN await() untuk menunggu upload selesai
            storageRef.putFile(imageUri).await()

            // 4. GUNAKAN await() untuk menunggu URL didapatkan
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Lanjutkan setelah semua proses async selesai
            addBread(name, description, downloadUrl)

            // Kembalikan hasil sukses
            Result.success(true)
        } catch (e: Exception) {
            // Jika terjadi error di mana pun, tangkap dan kembalikan sebagai failure
            Result.failure(e)
        }
    }


    fun addBread(name: String, description: String, image: String): Long {
        val bread = Bread(0, name, description, image)
        return dbHelper.insertBread(bread)
    }
}
