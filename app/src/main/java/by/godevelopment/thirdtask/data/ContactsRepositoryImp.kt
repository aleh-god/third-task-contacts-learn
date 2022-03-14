package by.godevelopment.thirdtask.data

import by.godevelopment.thirdtask.data.database.ContactsDao
import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsRepositoryImp @Inject constructor(
    private val contactsDao: ContactsDao
) : ContactsRepository {
    override suspend fun insertContact(contactModel: ContactModel): Boolean {
        val logInsert = contactsDao.insertContact(
            convertFromModelToEntity(contactModel)
        )
        return logInsert > -1
    }

    override suspend fun getContactByNumber(number: String): ContactEntity =
        contactsDao.getContactByNumber(number)

    override fun getAllContacts(): Flow<List<ContactEntity>> =
        contactsDao.getAllContacts()

    private fun convertFromModelToEntity(contactModel: ContactModel): ContactEntity {
        return ContactEntity(
            id = contactModel.id,
            key = contactModel.key,
            taskPhoneNumber = contactModel.taskPhoneNumber,
            name = contactModel.name,
            surname = contactModel.surname,
            email = contactModel.email
        )
    }
}