package com.julen.trailpack.vistas.marcogeneral

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    var selectedTab by mutableStateOf(2)

    fun cambiarTab(tab:Int) {
        selectedTab = tab
    }
}