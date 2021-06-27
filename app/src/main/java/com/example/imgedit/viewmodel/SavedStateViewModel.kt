package com.example.imgedit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    /*val filteredData: LiveData<List<String>> =
        savedStateHandle.getLiveData<String>("query").switchMap { query ->
            repository.getFilteredData(query)
        }

    fun setQuery(query: String) {
        savedStateHandle["query"] = query
    }*/
}