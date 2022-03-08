package by.godevelopment.thirdtask.di

import by.godevelopment.thirdtask.data.ContactsRepositoryImp
import by.godevelopment.thirdtask.data.database.ContactsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideContactsRepositoryImp(
        contactsDao: ContactsDao
    ): ContactsRepositoryImp
            = ContactsRepositoryImp(contactsDao)
}