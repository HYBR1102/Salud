package com.example.salud.ui.recordatorios

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordatoriosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Recordatorios Fragment"
    }
    val text: LiveData<String> = _text
}