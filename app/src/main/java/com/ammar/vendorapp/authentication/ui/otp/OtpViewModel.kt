package com.ammar.vendorapp.authentication.ui.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.models.TokenResponse
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<OtpUiEvents>()
    val events = _events.asSharedFlow()

    val execute: (OtpEvents) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is OtpEvents.ChangeOtp -> {
                    _state.changeState(otp = event.otp)
                }
                is OtpEvents.VerifyOtp -> {
                    if(state.value.otp != "") {
                        repository.verifyOtp(state.value.key, state.value.otp).collectLatest { result ->
                            when (result) {
                                is Result.Success -> {
                                    _state.changeState(loading = false, error = "", data = result.data)
                                    _events.emit(OtpUiEvents.Success(result.data!!))
                                }
                                is Result.Loading -> {
                                    _state.changeState(loading = true, error = "")
                                }
                                is Result.Error -> {
                                    _state.changeState(loading = false, error = result.message)
                                    _events.emit(OtpUiEvents.Error(result.message))
                                }
                            }
                        }
                    } else {
                        _events.emit(OtpUiEvents.InvalidInputParameters)
                    }
                }
                is OtpEvents.ResendOtp -> {
                    repository.resendOtp(state.value.email).collectLatest { result ->
                        when (result) {
                            is Result.Success -> {
                                _state.changeState(
                                    loading = false,
                                    error = "",
                                    key = result.data?.data?.verificationKey
                                )
                                _events.emit(OtpUiEvents.OtpResendSuccessful)
                            }
                            is Result.Loading -> {
                                _state.changeState(loading = true, error = "")
                            }
                            is Result.Error -> {
                                _state.changeState(loading = false, error = "")
                                _events.emit(OtpUiEvents.Error(result.message))
                            }
                        }
                    }
                }
                is OtpEvents.ChangeParameters -> {
                    _state.changeState(key = event.key, email = event.email)
                }
            }
        }
    }
}

sealed class OtpUiEvents {
    object InvalidInputParameters : OtpUiEvents()
    data class Success(val token: TokenResponse) : OtpUiEvents()
    data class Error(val error: String) : OtpUiEvents()
    object OtpResendSuccessful : OtpUiEvents()
}