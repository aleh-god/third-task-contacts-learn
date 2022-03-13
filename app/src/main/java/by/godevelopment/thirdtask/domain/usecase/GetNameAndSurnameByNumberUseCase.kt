package by.godevelopment.thirdtask.domain.usecase

import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import javax.inject.Inject

class GetNameAndSurnameByNumberUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(number: String): String {
        val contact = repository.getContactByNumber(number)
        return "${contact.name} ${contact.surname}"
    }
}