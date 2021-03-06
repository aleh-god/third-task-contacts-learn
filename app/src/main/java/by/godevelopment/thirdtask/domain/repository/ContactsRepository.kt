package by.godevelopment.thirdtask.domain.repository

import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.models.ContactModel
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    suspend fun insertContact(contactModel: ContactModel): Boolean
    suspend fun getContactByNumber(number: String): ContactEntity
    fun getAllContacts(): Flow<List<ContactEntity>>
}