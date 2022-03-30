package com.ammar.vendorapp.authentication.ui.email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
): ViewModel() {
    
    private val _state = MutableStateFlow(EmailState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<EmailUiEvents>()
    val events = _events.asSharedFlow()

    val execute: (EmailEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is EmailEvents.ChangeEmail -> {
                    _state.changeEmailState(event.email, event.error)
                }
                is EmailEvents.ValidateUser -> {
                    if(state.value.email.error.isBlank()) {
                        repository.validateUser(state.value.email.value).collectLatest { result ->
                            when(result) {
                                is Result.Success -> {
                                    val data = result.data?.data!!
                                    _state.changeState(data = data, loading = false)
                                    _events.emit(EmailUiEvents.ValidateUserSuccess(data.verificationKey))
                                }
                                is Result.Error -> {
                                    _state.changeState(loading = false, error = result.message)
                                    _events.emit(EmailUiEvents.Error(result.message))
                                }
                                is Result.Loading -> {
                                    _state.changeState(loading = true, error = "")
                                }
                            }
                        }
                    } else {
                        _events.emit(EmailUiEvents.InvalidInputParameters)
                    }
                }
                is EmailEvents.ForgotPassword -> {
                    if(state.value.email.error.isBlank()) {
                        repository.forgotPassword(state.value.email.value).collectLatest { result ->
                            when(result) {
                                is Result.Success -> {
                                    val data = result.data?.data!!
                                    _state.changeState(data = data, loading = false)
                                    _events.emit(EmailUiEvents.ForgotPasswordSuccess(data.verificationKey))
                                }
                                is Result.Error -> {
                                    _state.changeState(loading = false, error = result.message)
                                    _events.emit(EmailUiEvents.Error(result.message))
                                }
                                is Result.Loading -> {
                                    _state.changeState(loading = true, error = "")
                                }
                            }
                        }
                    } else {
                        _events.emit(EmailUiEvents.InvalidInputParameters)
                    }
                }
            }
        }
    }
    
}

sealed class EmailUiEvents {
    data class ValidateUserSuccess(val key: String): EmailUiEvents()
    data class ForgotPasswordSuccess(val key: String): EmailUiEvents()
    data class Error(val error: String): EmailUiEvents()
    object InvalidInputParameters: EmailUiEvents()
}