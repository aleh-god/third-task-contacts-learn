package by.godevelopment.thirdtask.presentation.main

import androidx.lifecycle.ViewModel
import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.helpers.SharedPreferencesHelper
import by.godevelopment.thirdtask.domain.usecase.ConvertEntityToArrayListUseCase
import by.godevelopment.thirdtask.domain.usecase.GetContactEntityByIndexUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val convertEntityToArrayListUseCase: ConvertEntityToArrayListUseCase,
    private val getContactEntityByIndexUseCase: GetContactEntityByIndexUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper
): ViewModel() {
    private val _eventUI = MutableSharedFlow<EventUI>(0)
    val eventUI: SharedFlow<EventUI> = _eventUI

    fun getArrayFlow(): Flow<ArrayList<String>> =
        convertEntityToArrayListUseCase()

    fun getContactByIndexFlow(index: Int): Flow<ContactEntity>
        = getContactEntityByIndexUseCase(index)


    fun saveContactByIndexInSharedPref(contact: ContactEntity) {
        sharedPreferencesHelper.setCurrentPhoneNumber(contact.taskPhoneNumber)
    }

    fun getNumberFromSP(): String {
        return sharedPreferencesHelper.getCurrentPhoneNumber()
    }

    data class EventUI(
        val message: String
    )
}