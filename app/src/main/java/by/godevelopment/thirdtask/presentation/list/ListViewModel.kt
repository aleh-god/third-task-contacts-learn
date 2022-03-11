package by.godevelopment.thirdtask.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.domain.helpers.ContactsContractHelper
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.usecase.InsertContactUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val contractHelper: ContactsContractHelper,
    private val insertContactUseCase: InsertContactUseCase
): ViewModel() {
    private val _stateUI = MutableStateFlow(StateUI())
    val stateUI: StateFlow<StateUI> = _stateUI

    private val _eventUI = MutableSharedFlow<EventUI>(0)
    val eventUI: SharedFlow<EventUI> = _eventUI

    init {
        Log.i(TAG, "ListViewModel: init")
        viewModelScope.launch {
            populateUiState()
        }
    }

    private suspend fun populateUiState() {
        contractHelper.getTestList()
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

    fun saveContactModel(contactModel: ContactModel) {
        viewModelScope.launch {
            val logResult = insertContactUseCase(contactModel)
            if (logResult) {
                _eventUI.emit(EventUI(
                    "Contact saved successful."
                ))
            } else {
                _eventUI.emit(EventUI(
                    "Contact don't saved."
                ))
            }
        }
    }

    data class StateUI(
        val isFetchingData: Boolean = false,
        val contacts: List<ContactModel> = listOf()
    )

    data class EventUI(
        val message: String
    )
}