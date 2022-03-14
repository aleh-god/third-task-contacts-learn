package by.godevelopment.thirdtask.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.domain.helpers.ContactsContractHelper
import by.godevelopment.thirdtask.domain.helpers.StringHelper
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.usecase.InsertContactUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val contractHelper: ContactsContractHelper,
    private val insertContactUseCase: InsertContactUseCase,
    private val stringHelper: StringHelper
): ViewModel() {
    private val _stateUI = MutableStateFlow(StateUI())
    val stateUI: StateFlow<StateUI> = _stateUI

    private val _eventUI = MutableSharedFlow<EventUI>(0)
    val eventUI: SharedFlow<EventUI> = _eventUI

    init {
        populateUiState()
    }

    private fun populateUiState() {
        viewModelScope.launch {
            contractHelper.getContactList()
                .onStart {
                    Log.i(TAG, "ListViewModel: .onStart")
                    _stateUI.value = StateUI(
                        isFetchingData = true
                    )
                }
                .catch { exception ->
                    Log.i(TAG, "ListViewModel: .catch = ${exception.message}")
                    _stateUI.value = StateUI(
                        isFetchingData = false
                    )
                    delay(1000)
                    _eventUI.emit(
                        EventUI(
                            stringHelper.getString(R.string.alert_list_contact_not_loaded)
                        )
                    )
                }
                .collect {
                    Log.i(TAG, "ListViewModel: size = ${it.size}")
                    _stateUI.value = StateUI(
                        isFetchingData = false,
                        contacts = it
                    )
                }
        }
    }

    fun saveContactModel(contactModel: ContactModel) {
        viewModelScope.launch {
            val logResult = insertContactUseCase(contactModel)
            if (logResult) {
                _eventUI.emit(EventUI(
                    stringHelper.getString(R.string.alert_list_contact_good)
                ))
            } else {
                _eventUI.emit(EventUI(
                    stringHelper.getString(R.string.alert_list_contact_bad)
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