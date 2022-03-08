package by.godevelopment.thirdtask.di

import android.content.Context
import androidx.room.Room
import by.godevelopment.thirdtask.data.database.ContactsDao
import by.godevelopment.thirdtask.data.database.ContactsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Provides
    fun provideContactsDao(database: ContactsDataBase): ContactsDao {
        return database.getContactDao()
    }

    @Provides
    @Singleton
    fun provideContactsDatabase(
        @ApplicationContext appContext: Context,
        providerContacts: Provider<ContactsDao>
    ): ContactsDataBase {
        return Room
            .databaseBuilder(
                appContext,
                ContactsDataBase::class.java,
                "contacts_database.db"
            )
            .build()
    }
}