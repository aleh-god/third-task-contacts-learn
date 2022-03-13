package by.godevelopment.thirdtask.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.helpers.NotificationHelper
import by.godevelopment.thirdtask.domain.helpers.SharedPreferencesHelper
import by.godevelopment.thirdtask.domain.helpers.StringHelper
import by.godevelopment.thirdtask.domain.usecase.ConvertEntityToArrayListUseCase
import by.godevelopment.thirdtask.domain.usecase.GetContactEntityByIndexUseCase
import by.godevelopment.thirdtask.domain.usecase.GetNameAndSurnameByNumberUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val convertEntityToArrayListUseCase: ConvertEntityToArrayListUseCase,
    private val getContactEntityByIndexUseCase: GetContactEntityByIndexUseCase,
    private val getNameAndSurnameByNumberUseCase: GetNameAndSurnameByNumberUseCase,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val notificationHelper: NotificationHelper,
    private val stringHelper: StringHelper
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

    fun showNotification(message: String) {
        notificationHelper.createNotification(message)
    }

    fun showNameAndSurnameByNumber() {
        viewModelScope.launch {
            try {
                val key = sharedPreferencesHelper.getCurrentPhoneNumber()
                showNotification(getNameAndSurnameByNumberUseCase(key))
            } catch (e: Exception) {
                _eventUI.emit(
                    EventUI(e.message ?: stringHelper.getString(R.string.alert_text_error_unknown))
                )
            }
        }
    }

    data class EventUI(
        val message: String
    )
}