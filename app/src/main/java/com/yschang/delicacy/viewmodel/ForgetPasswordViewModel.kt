package com.yschang.delicacy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yschang.delicacy.MyApplication
import com.yschang.delicacy.util.EmailUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ForgetPasswordViewModel : ViewModel() { /***** Forget Password Screen *****/
    val state = MutableStateFlow(ForgetPasswordState())
    private val dao = MyApplication.database.userDao()
    var code = "0000" // Default code for email verification

    init {

    }

    fun send(username: String) { // Send a verification code via email
        //send email
        viewModelScope.launch(Dispatchers.IO) {
            if (username.isEmpty()) {
                state.value = //Remind user to...
                    ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                        "Please fill in the username"))
                return@launch //Terminate the current coroutine without executing the subsequent code block
            }
            val user = dao.findByUsername(username)
            if (user == null) { // If user doesn't exist
                state.value = //Error message
                    ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                        "User does not exist"))
                return@launch
            }
            val email = user.email
            if (email.isEmpty()) { // If email doesn't exist
                state.value =
                    ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                        "Email does not exist"))
                return@launch
            }
            val code = EmailUtil.generateVerificationCode() // Generate a verification code
            this@ForgetPasswordViewModel.code = code // Store the code in the ViewModel
            EmailUtil.sendEmail( // Send the verification code via email
                email,
                "Registration verification code", // Email subject
                "The verification code for this registration is "
                        + code // Email content
            )
            state.value =
                ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_SUCCESS(
                    "Verification code sent successfully"))
        }
    }

    fun forgetPassword(
        username: String, password: String,
        repeatPassword: String,
        code: String
    ) {
        // Validates the input data
        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            state.value =
                ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                    "Please fill in all fields"))
            return
        }
        if (password != repeatPassword) { // Checks if passwords match
            state.value =
                ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                    "Passwords do not match"))
            return
        }
        if (code != this.code) { // Checks if the verification code matches
            state.value =
                ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                    "Verification code is incorrect"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = dao.findByUsername(username)
            if (user == null) {
                state.value =
                    ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_FAILED(
                        "User does not exist"))
                return@launch
            }
            dao.updateUser(user.copy(password = password))
            state.value =
                ForgetPasswordState(ForgetPasswordEvent.FORGET_PASSWORD_SUCCESS(
                    "Password reset successfully"))
        }
    }

    data class ForgetPasswordState(
        val forgetPasswordEvent: ForgetPasswordEvent = ForgetPasswordEvent.FORGET_PASSWORD_IDLE
    )

    /*使用密封類別可以提供更好的程式碼組織、安全的狀態處理以及明確的繼承結構*/
    sealed class ForgetPasswordEvent { // Sealed class representing different forget password events
        data object FORGET_PASSWORD_IDLE : ForgetPasswordEvent() // Initial idle state
        data class FORGET_PASSWORD_SUCCESS(val reason: String) : ForgetPasswordEvent()
        data class FORGET_PASSWORD_FAILED(val reason: String) : ForgetPasswordEvent()
    }

}