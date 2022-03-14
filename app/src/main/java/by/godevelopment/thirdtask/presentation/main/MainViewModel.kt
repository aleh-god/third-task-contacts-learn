package by.godevelopment.thirdtask.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.common.START_KEY
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
        val key = sharedPreferencesHelper.getCurrentPhoneNumber()
        return if (key != START_KEY) key
        else stringHelper.getString(R.string.alert_sp_error)
    }

    fun showNameAndSurnameByNumber() {
        viewModelScope.launch {
            val key = sharedPreferencesHelper.getCurrentPhoneNumber()
            if (key != START_KEY) {
                notificationHelper.createNotification(getNameAndSurnameByNumberUseCase(key))
                _eventUI.emit(EventUI(stringHelper.getString(R.string.alert_main_notif_started)))
            }
            else _eventUI.emit(EventUI(stringHelper.getString(R.string.alert_sp_error)))
        }
    }

    data class EventUI(
        val message: String
    )
}