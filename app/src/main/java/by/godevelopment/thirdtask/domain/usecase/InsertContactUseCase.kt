package by.godevelopment.thirdtask.domain.usecase

import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import javax.inject.Inject

class InsertContactUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    suspend operator fun invoke(contactModel: ContactModel): Boolean {
        return contactsRepository.insertContact(contactModel)
    }
}