package com.ammar.vendorapp.authentication.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.models.UserLogin

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<LoginUiEvents>()
    val events = _events.asSharedFlow()

    val execute: (LoginEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is LoginEvents.Login -> {
                    if(state.value.email.error.isNotBlank() && state.value.password.error.isNotBlank()) {
                        _events.emit(LoginUiEvents.InvalidInputParameters)
                    } else {
                        val user = UserLogin(state.value.email.value, state.value.password.value)
                        repository.login(user).collectLatest { result ->
                            when(result) {
                                is Result.Success -> print(result.data)
                                is Result.Error -> print(result.message)
                                is Result.Loading -> print("Loading")
                            }
                        }
                    }
                }
                is LoginEvents.ChangeEmail -> {
                    _state.changeEmailState(event.email, event.error)
                }
                is LoginEvents.ChangePassword -> {
                    _state.changePasswordState(event.password, event.error)
                }
            }
        }
    }

    private fun print(data: String?, tag: String = "TAG") {
        Log.d(tag, "print: ${data.toString()}")
    }

}

sealed class LoginUiEvents {
    object InvalidInputParameters: LoginUiEvents()
}