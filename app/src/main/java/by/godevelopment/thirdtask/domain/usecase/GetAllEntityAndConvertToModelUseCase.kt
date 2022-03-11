package by.godevelopment.thirdtask.domain.usecase

import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllEntityAndConvertToModelUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(): Flow<List<ContactModel>>
    = contactsRepository.getAllContacts()
        .map {
            it.map {
                convertFromEntityToModel(it)
            }
        }

    private fun convertFromEntityToModel(contactEntity: ContactEntity): ContactModel {
        return ContactModel(
            id = contactEntity.id,
            taskPhoneNumber = contactEntity.taskPhoneNumber,
            name = contactEntity.name,
            surname = contactEntity.surname,
            email = contactEntity.email,
            isSelected = false
        )
    }
}