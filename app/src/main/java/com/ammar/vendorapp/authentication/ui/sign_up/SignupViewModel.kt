package com.ammar.vendorapp.authentication.ui.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.domain.entities.User
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<UiEvents>()
    val events = _events.asSharedFlow()

    val execute: (SignupEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is SignupEvents.Register -> {
                    repository.register(event.user).collectLatest { result ->
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

    /*fun <T> MutableStateFlow<BaseState<T>>.copy(data: T, error: String, loading: Boolean) {
        this.value = this.value.copy(
            data = data,
            error = error,
            loading = loading
        )
    }*/
}

sealed class UiEvents {
    object Loading: UiEvents()
    data class Success(val user: User): UiEvents()
    data class Error(val message: String): UiEvents()
}