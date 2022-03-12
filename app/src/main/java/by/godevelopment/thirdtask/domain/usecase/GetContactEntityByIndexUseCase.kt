package by.godevelopment.thirdtask.domain.usecase

import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetContactEntityByIndexUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    operator fun invoke(index: Int): Flow<ContactEntity> {
            return repository.getAllContacts().map { list ->
                    if (index < list.size ) {
                        list[index]
                    } else {
                        throw IndexOutOfBoundsException()
                    }
        }
    }
}