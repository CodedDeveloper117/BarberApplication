package com.ammar.vendorapp.authentication.ui.change_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import com.ammar.vendorapp.authentication.ui.login.LoginUiEvents
import com.ammar.vendorapp.authentication.ui.otp.OtpUiEvents
import com.ammar.vendorapp.authentication.ui.otp.changeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ChangePasswordUiEvents>()
    val events = _events.asSharedFlow()

    val execute: (ChangePasswordEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is ChangePasswordEvents.ChangePassword -> {
                    _state.changePasswordState(event.value, event.error)
                }
                is ChangePasswordEvents.ChangeRetypePassword -> {
                    _state.changeRetypePassword(event.value, event.error)
                }
                is ChangePasswordEvents.Continue -> {
                    if(state.value.password.error.isBlank() && state.value.retypePassword.value.isBlank()) {
                        repository.resetPassword(
                            state.value.password.value,
                            event.token
                        ).collectLatest { result ->
                            when(result) {
                                is Result.Success -> {
                                    _state.changeState(
                                        loading = false,
                                        error = "",
                                        data = result.data?.data
                                    )
                                    _events.emit(ChangePasswordUiEvents.Success(event.token))
                                }
                                is Result.Loading -> {
                                    _state.changeState(loading = true, error = "")
                                }
                                is Result.Error -> {
                                    _state.changeState(loading = false, error = "")
                                    _events.emit(ChangePasswordUiEvents.Error(result.message))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class ChangePasswordUiEvents {
    object InvalidInputParameters : ChangePasswordUiEvents()
    data class Success(val data: String) : ChangePasswordUiEvents()
    data class Error(val error: String) : ChangePasswordUiEvents()
}