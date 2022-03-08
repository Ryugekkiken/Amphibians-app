package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi.retrofitService
import kotlinx.coroutines.launch

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    // Create properties to represent MutableLiveData and LiveData for the API status
    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status

    // Create properties to represent MutableLiveData and LiveData for a list of amphibian objects
    private val _amphibianList = MutableLiveData<List<Amphibian>>()
    val amphibianList: LiveData<List<Amphibian>> = _amphibianList

    //  Create properties to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    private val _amphibianSingle = MutableLiveData<Amphibian>()
    val amphibianSingle: LiveData<Amphibian> = _amphibianSingle

    //  Create a function that gets a list of amphibians from the api service and sets the
    //  status via a Coroutine
    private fun getAmphibianList()
    {
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try
            {
                _amphibianList.value = retrofitService.getAmphibians()
                _status.value = AmphibianApiStatus.DONE
            }
            catch(e: Exception)
            {
                _status.value = AmphibianApiStatus.ERROR
                _amphibianList.value = listOf()
            }
        }
    }

    init
    {
        getAmphibianList()
    }

    fun onAmphibianClicked(amphibian: Amphibian)
    {
        // Set the amphibian object
        _amphibianSingle.value = amphibian
    }
}
