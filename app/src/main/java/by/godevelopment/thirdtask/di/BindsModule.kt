package by.godevelopment.thirdtask.di

import by.godevelopment.thirdtask.data.ContactsRepositoryImp
import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class BindsModule {
    @Binds
    abstract fun bindContactsRepositoryImp(contactsRepositoryImp: ContactsRepositoryImp): ContactsRepository

}