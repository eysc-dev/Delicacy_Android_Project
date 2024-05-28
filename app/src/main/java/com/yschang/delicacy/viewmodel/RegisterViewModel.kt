package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.util.EmailUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() { /***** Register Screen *****/
    val state = MutableStateFlow(RegisterState())
    val dao = MyApplication.database.userDao()
    var code = "0000" // Default email verification code

    fun send(email: String) { //Send a verification code via email
        if (email.isEmpty()) { // Check if the email field is empty
            state.update {
                it.copy(registerEvent = RegisterEvent.REGISTER_FAILED("Please fill in the email"))
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val code = EmailUtil.generateVerificationCode() // Generate a email verification code
            this@RegisterViewModel.code = code // Store the generated code in the ViewModel
            EmailUtil.sendEmail(
                email,
                "Registration verification code", // Email subject
                "The verification code for this registration is "
                        + code // Email content with the verification code
            )
        }
    }

    //Register a new user
    fun register(
        username: String, password: String, email: String, code: String, fullName: String,
        phoneNumber: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(registerEvent = RegisterEvent.REGISTER_IDLE)
            }
            if (username.isEmpty() or password.isEmpty() or email.isEmpty() or code.isEmpty() or fullName.isEmpty() or phoneNumber.isEmpty()) {
                state.update {
                    it.copy(registerEvent = RegisterEvent.REGISTER_FAILED(
                        "Please fill in all the fields"))
                }
                return@launch //stops further execution within this coroutine block
            }
            val user = dao.findByUsername(username) // Check if the user already exists
            if (user != null) {
                state.update {
                    it.copy(registerEvent = RegisterEvent.REGISTER_FAILED("User already exists"))
                }
                return@launch
            }
            if (code != this@RegisterViewModel.code) { // Checks if the verification code matches
                state.value = RegisterState( RegisterEvent.REGISTER_FAILED(
                            "Verification code is incorrect"
                        )
                )
                return@launch
            }
            else {
                dao.insertUser( // Insert the new user into the database
                    com.yschang.delicacy.entity.MyUser(
                        0, // Default ID
                        username = username,
                        password = password,
                        email = email,
                        code = code, // Set the email verification code
                        fullName = fullName,
                        phoneNumber = phoneNumber
                    )
                )
                state.update {// Update the state to indicate successful registration
                    it.copy(
                        registerEvent = RegisterEvent.REGISTER_SUCCESS("Registration successful")
                    )
                }
            }
        }
    }
    // Data class for the registration state
    data class RegisterState(
        val registerEvent: RegisterEvent = RegisterEvent.REGISTER_IDLE
    )
    // Sealed class representing different registration events
    sealed class RegisterEvent {
        data object REGISTER_IDLE : RegisterEvent()
        data class REGISTER_SUCCESS(val reason: String) : RegisterEvent()
        data class REGISTER_FAILED(val reason: String) : RegisterEvent()
    }
}