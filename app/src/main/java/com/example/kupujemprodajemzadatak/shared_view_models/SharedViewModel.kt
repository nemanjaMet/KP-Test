package com.example.kupujemprodajemzadatak.shared_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var isSplashVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    var previewAdPosition = 0

    fun hideSplashScreen() {
        if (isSplashVisible.value == true)
            isSplashVisible.postValue(false)
    }

}