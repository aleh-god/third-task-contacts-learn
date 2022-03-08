package by.godevelopment.thirdtask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.godevelopment.thirdtask.data.entities.ContactEntity

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class ContactsDataBase : RoomDatabase() {
    abstract fun getContactDao(): ContactsDao
}