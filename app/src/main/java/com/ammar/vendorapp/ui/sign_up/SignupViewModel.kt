package com.ammar.vendorapp.ui.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.common.data.BaseState
import com.ammar.vendorapp.common.utils.Result
import com.ammar.vendorapp.domain.entities.User
import com.ammar.vendorapp.domain.repositories.AuthRepository
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
                is SignupEvents.SignupInWithGoogle -> {
                    val result = event.task.result
                    val user = User(
                        id = result.id.toString(),
                        username = result.displayName.toString(),
                        email = result.email.toString(),
                        fullName = result.givenName.toString() + " " + result.familyName.toString(),
                        photoUrl = result.photoUrl.toString(),
                        phone = null
                    )
                    repository.register(user).collectLatest {  result ->
                        when(result) {
                            is Result.Success -> {
                                _state.value = state.value.copy(
                                    data = result.data,
                                    loading = false,
                                    error = ""
                                )
                                print(result.data.email, "tag: data: ")
                            }
                            is Result.Error -> {
                                _state.value = state.value.copy(
                                    data = null,
                                    loading = false,
                                    error = result.message
                                )
                                print(result.message, "tag: error: ")
                            }
                            is Result.Loading -> {
                                _state.value = state.value.copy(
                                    data = null,
                                    loading = true,
                                    error = ""
                                )
                            }
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