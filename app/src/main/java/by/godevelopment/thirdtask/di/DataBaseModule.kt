package by.godevelopment.thirdtask.di

import android.content.Context
import androidx.room.Room
import by.godevelopment.thirdtask.data.database.ContactsDao
import by.godevelopment.thirdtask.data.database.ContactsDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
object DataBaseModule {

    @Provides
    fun provideContactsDao(database: ContactsDataBase): ContactsDao {
        return database.getContactDao()
    }

    @Provides
    @Singleton
    fun provideContactsDatabase(
        context: Context,
        providerContacts: Provider<ContactsDao>
    ): ContactsDataBase {
        return Room
            .databaseBuilder(
                context,
                ContactsDataBase::class.java,
                "contacts_database.db"
            )
            .build()
    }
}