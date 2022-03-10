package by.godevelopment.thirdtask.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.usecase.GetAllContactsUseCase
import by.godevelopment.thirdtask.domain.usecase.InsertContactUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllContactsUseCase: GetAllContactsUseCase,
    private val insertContactUseCase: InsertContactUseCase
): ViewModel() {
    private val _stateUI = MutableStateFlow(StateUI())
    val stateUI: StateFlow<StateUI> = _stateUI

    private val _eventUI = MutableSharedFlow<EventUI>(0)
    val eventUI: SharedFlow<EventUI> = _eventUI

    init {
        viewModelScope.launch {
            populateDB()
            populateUI()
        }
    }

    private suspend fun populateUI() {
        getAllContactsUseCase()
            .onStart {
                _stateUI.value = StateUI(
                    isFetchingData = true
                )
            }
            .catch { exception ->
                _stateUI.value = StateUI(
                    isFetchingData = false
                )
                _eventUI.emit(
                    EventUI(
                        exception.message ?: "Unknown error"
                    )
                )
            }
            .collect {
                _stateUI.value = StateUI(
                    isFetchingData = false,
                    contacts = it
                )
            }
    }

    private suspend fun populateDB() {
        (0..20).forEach {
            val logInsert = insertContactUseCase(
                ContactModel(
                    id = it,
                    name = "name $it",
                    surname = "surname $it",
                    taskPhoneNumber = "+37529$it$it$it$it$it$it$it",
                    email = "$it@email.com"
                )
            )
            Log.i(TAG, "populateDB: insert = $logInsert")
        }
    }

    data class StateUI(
        val isFetchingData: Boolean = false,
        val contacts: List<ContactEntity> = listOf()
    )

    data class EventUI(
        val message: String
    )

}