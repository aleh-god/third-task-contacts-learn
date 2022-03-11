package by.godevelopment.thirdtask.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.godevelopment.thirdtask.domain.models.ContactModel
import by.godevelopment.thirdtask.domain.usecase.GetAllEntityAndConvertToModelUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAllEntityAndConvertToModelUseCase: GetAllEntityAndConvertToModelUseCase
): ViewModel() {
    private val _stateUI = MutableStateFlow(StateUI())
    val stateUI: StateFlow<StateUI> = _stateUI

    private val _eventUI = MutableSharedFlow<EventUI>(0)
    val eventUI: SharedFlow<EventUI> = _eventUI

    init {
        viewModelScope.launch {
            getAllContactModel()
        }
    }

    private suspend fun getAllContactModel() {
        getAllEntityAndConvertToModelUseCase()
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

    data class StateUI(
        val isFetchingData: Boolean = false,
        val contacts: List<ContactModel> = listOf()
    )

    data class EventUI(
        val message: String
    )
}