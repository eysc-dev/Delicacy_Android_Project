package com.yschang.delicacy.viewmodel.home

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.MyApplication.Companion.usernamePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() { /***** Profile Screen *****/
    val state = MutableStateFlow(ProfileState())
    val dao = MyApplication.database.userDao()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[usernamePreferences]
            val user = dao.findByUsername(username!!)
            state.update {
                it.copy(
                    username = username,
                    email = user?.email ?: ""
                )
            }
        }
    }

    fun logout(toLogin: () -> Unit) // Function to log out and navigate to the login screen
    {
        viewModelScope.launch(Dispatchers.IO) {
            MyApplication.myDataStore.edit {
                it[MyApplication.loginPreferences] = false
                it[usernamePreferences] = ""
            }
            withContext(Dispatchers.Main) {//Switch to the Main coroutine context
                toLogin() // Call the function to navigate to the login screen
            }
        }
    }

    data class ProfileState(
        val username: String = "",
        val email: String = "",
    )
}