package com.example.salud.ui.medicamentos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedicamentosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Medicamentos Fragment"
    }
    val text: LiveData<String> = _text
}