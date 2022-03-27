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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    val execute: (LoginEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is LoginEvents.Login -> {
                    repository.login(event.user).collectLatest { result ->
                        when(result) {
                            is Result.Success -> print(result.data)
                            is Result.Error -> print(result.message)
                            is Result.Loading -> print("Loading")
                        }
                    }
                }
            }
        }
    }

    private fun print(data: String?, tag: String = "TAG") {
        Log.d(tag, "print: ${data.toString()}")
    }

}

sealed class UiEvents {

}