package by.godevelopment.thirdtask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.godevelopment.thirdtask.data.entities.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long

    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("DELETE FROM contacts_table")
    suspend fun deleteAll()
}