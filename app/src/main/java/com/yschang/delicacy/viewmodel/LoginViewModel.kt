package com.yschang.delicacy.viewmodel

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.MyApplication.Companion.emailPreferences
import com.yschang.delicacy.MyApplication.Companion.foodPreferences
import com.yschang.delicacy.MyApplication.Companion.loginPreferences
import com.yschang.delicacy.MyApplication.Companion.passwordPreferences
import com.yschang.delicacy.MyApplication.Companion.usernamePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() { /***** Login Screen *****/
    private val dao = MyApplication.database.userDao()
    val state = MutableStateFlow(LoginState())

    init {

    }

    fun login(
        username: String, password: String,
        toHome: () -> Unit // Navigate to the home screen upon successful login
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(loginEvent = LoginEvent.LOGIN_IDLE)
            }
            val user = dao.findByUsername(username) // Find the user by username

            // If the user exists and the password matches
            if (user != null && user.password == password) {
                state.update {
                    it.copy(loginEvent = LoginEvent.LOGIN_SUCCESS)
                }
                // Edit the DataStore to remember login status and username
                MyApplication.myDataStore.edit { settings ->
                    settings[loginPreferences] = true
                    settings[usernamePreferences] = username
                }
                withContext(Dispatchers.Main) {// Switch to the main thread
                    toHome() // Navigate to the home screen
                }
            } else { // If login fails
                state.update {
                    it.copy(loginEvent = LoginEvent.LOGIN_FAILED)
                }
            }
        }
    }

    // Data class representing the state of the login screen
    data class LoginState(
        val loginEvent: LoginEvent = LoginEvent.LOGIN_IDLE
    )

    // Enum class representing different login events
    enum class LoginEvent {
        LOGIN_SUCCESS,
        LOGIN_FAILED,
        LOGIN_IDLE
    }
}