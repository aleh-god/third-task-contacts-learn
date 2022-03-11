package by.godevelopment.thirdtask.di

import by.godevelopment.thirdtask.data.ContactsRepositoryImp
import by.godevelopment.thirdtask.data.database.ContactsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DataBaseModule::class, BindsModule::class, ViewModelModule::class])
object AppModule {

    @Provides
    @Singleton
    fun provideContactsRepositoryImp(
        contactsDao: ContactsDao
    ): ContactsRepositoryImp = ContactsRepositoryImp(contactsDao)
}