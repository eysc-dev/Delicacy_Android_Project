package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDetailViewModel : ViewModel() { /***** User Profile Screen *****/
    val state = MutableStateFlow(ProfileDetailState())
    val dao = MyApplication.database.userDao()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(isLoading = true)
            }
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)

            // Update the state with the user details
            state.value = state.value.copy(
                username = username,
                email = user?.email ?: "",
                fullName = user?.fullName ?: "",
                phoneNumber = user?.phoneNumber ?: "",
                isLoading = false
            )
        }
    }

    fun editProfile(
        email: String,
        fullName: String,
        phoneNumber: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(isLoading = true)
            }
            val preferences = MyApplication.myDataStore.data.first()
            val username = preferences[MyApplication.usernamePreferences]
            val user = dao.findByUsername(username!!)
            dao.updateUser(
                user!!.copy(
                    email = email,
                    fullName = fullName,
                    phoneNumber = phoneNumber
                )
            )
            state.value = state.value.copy( // Update the state with the new user details
                email = email,
                fullName = fullName,
                phoneNumber = phoneNumber,
                isLoading = false
            )
        }
    }

    data class ProfileDetailState(
        val username: String = "",
        val email: String = "",
        val fullName: String = "",
        val phoneNumber: String = "",
        val isLoading: Boolean = false
    )
}