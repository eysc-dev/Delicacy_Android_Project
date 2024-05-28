package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.MyApplication.Companion.loginPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() { /***** Main Screen *****/
    val state = MutableStateFlow(MainState())

    init { // Initializer block that runs when the ViewModel is created
        viewModelScope.launch(Dispatchers.IO) {
            state.update { mainState -> // Update the state based on the login status
                mainState.copy(
                    loginEvent = if (MyApplication.myDataStore.data.map {
                            it[loginPreferences] == true // Check if the user logs in
                        }.first()) {
                        LoginEvent.LOGIN_SUCCESS // Set the event to login success
                    } else {
                        LoginEvent.LOGIN_FAILED // Set the event to login failed
                    }
                )
            }
        }

    }

    data class MainState(
        val loginEvent: LoginEvent = LoginEvent.LOGIN_IDLE
    )

    // Sealed class representing different login events
    sealed class LoginEvent {
        data object LOGIN_SUCCESS : LoginEvent()
        data object LOGIN_FAILED : LoginEvent()
        data object LOGIN_IDLE : LoginEvent()
    }
}
