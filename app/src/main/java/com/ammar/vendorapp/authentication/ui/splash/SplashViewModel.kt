package com.ammar.vendorapp.authentication.ui.splash

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.repositories.DatastoreRepository
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import com.ammar.vendorapp.common.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SplashViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository,
    private val datastore: DatastoreRepository
): ViewModel() {

    private val _events = MutableSharedFlow<SplashUiEvents>()
    val events = _events.asSharedFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val token = datastore.userPreferences.first()
            if(token.isNotBlank()) {
                repository.getUserInfo(token).collectLatest { result ->
                    when(result) {
                        is Result.Success -> {
                            _events.emit(SplashUiEvents.Success(result.data.data))
                        }
                        is Result.Error -> {
                            _events.emit(SplashUiEvents.Error(result.message))
                        }
                        is Result.Loading -> {
                            _events.emit(SplashUiEvents.InitialState)
                        }
                    }
                }
            } else {
                _events.emit(SplashUiEvents.InvalidToken)
            }
        }
    }
}

sealed class SplashUiEvents {
    data class Success(val user: User): SplashUiEvents()
    data class Error(val error: String): SplashUiEvents()
    object InitialState: SplashUiEvents()
    object InvalidToken: SplashUiEvents()
}