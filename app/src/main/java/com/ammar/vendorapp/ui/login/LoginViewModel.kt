package com.ammar.vendorapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.domain.entities.User
import com.ammar.vendorapp.domain.repositories.AuthRepository
import com.ammar.vendorapp.ui.sign_up.UiEvents
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ammar.vendorapp.common.utils.Result

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
                is LoginEvents.LoggedInWithGoogle -> {
                    try{
                        val result = event.task.result
                        val user = User(
                            id = result.id.toString(),
                            username = result.displayName.toString(),
                            email = result.email.toString(),
                            fullName = result.givenName.toString() + " " + result.familyName.toString(),
                            photoUrl = result.photoUrl.toString(),
                            phone = null
                        )
                        repository.loginWithGoogle(user).collectLatest { result ->
                            when(result) {
                                is Result.Success -> {
                                    _state.value = state.value.copy(
                                        data = result.data,
                                        loading = false,
                                        error = ""
                                    )
                                    print(result.data.email, "tag")
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
                    }catch (e: ApiException) {
                        e.printStackTrace()
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