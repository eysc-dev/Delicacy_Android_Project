package com.yschang.delicacy.viewmodel

import com.yschang.delicacy.viewmodel.home.CartViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/*The'state' is initialized with the default state of the CartViewModel.CartState().
Thismeans that any collector of this flow will observe these cart state updates.*/
object SharedState{
    val state = MutableStateFlow(CartViewModel.CartState())
}