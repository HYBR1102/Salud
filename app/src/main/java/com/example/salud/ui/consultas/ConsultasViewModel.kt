package com.example.salud.ui.consultas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsultasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Consultas Fragment"
    }
    val text: LiveData<String> = _text
}