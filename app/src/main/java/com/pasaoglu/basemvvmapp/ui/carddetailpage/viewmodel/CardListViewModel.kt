package com.pasaoglu.basemvvmapp.ui.carddetailpage.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.pasaoglu.basemvvmapp.data.model.BaseCardListResponseModel
import com.pasaoglu.basemvvmapp.data.repository.MainRepository
import com.pasaoglu.basemvvmapp.util.Resource
import kotlinx.coroutines.launch

class CardListViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel(), LifecycleObserver {

    private val _cards: MutableLiveData<Resource<BaseCardListResponseModel>> = MutableLiveData()
    val cards: LiveData<Resource<BaseCardListResponseModel>>
        get() = _cards

    fun getCardList() = viewModelScope.launch {
        _cards.value = Resource.loading(data = null)
        _cards.value = Resource.success(data = mainRepository.getListWithQuery())
    }

    /*fun getCardList() = liveData(Dispatchers.IO) {
        try {
            emit(Resource.success(data = mainRepository.getListWithQuery()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/

}