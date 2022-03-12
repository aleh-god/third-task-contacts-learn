package by.godevelopment.thirdtask.domain.usecase

import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConvertEntityToArrayListUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
) {
    operator fun invoke(): Flow<ArrayList<String>> = contactsRepository.getAllContacts()
        .map {  list ->
            ArrayList(
                list.map {
                    "[${it.id} ${it.name} ${it.surname} ${it.taskPhoneNumber} ${it.email}]"
                }
            )
        }
}