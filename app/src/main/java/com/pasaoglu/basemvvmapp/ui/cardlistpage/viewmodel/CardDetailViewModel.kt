package com.pasaoglu.basemvvmapp.ui.cardlistpage.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.pasaoglu.basemvvmapp.data.repository.MainRepository
import com.pasaoglu.basemvvmapp.util.Resource
import kotlinx.coroutines.Dispatchers


class CardDetailViewModel @ViewModelInject constructor(private val mainRepository:MainRepository ) : ViewModel() , LifecycleObserver {

    fun getCardDetailById(cardId: String) = liveData(Dispatchers.IO) {
            try {
                emit(Resource.success(data = mainRepository.getCardDetailById(cardId = cardId)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
}