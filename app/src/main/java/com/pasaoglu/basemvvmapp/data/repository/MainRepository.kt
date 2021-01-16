package com.pasaoglu.basemvvmapp.data.repository

import com.pasaoglu.basemvvmapp.data.network.Service
import javax.inject.Inject

class  MainRepository @Inject constructor(private val service: Service){

    //suspend fun getListWithQuery(query: String, page: Int) = service.getListWithQuery(query = query, page = page)
    suspend fun getListWithQuery() = service.getListWithQuery()

    suspend fun getCardDetailById(cardId: String) = service.getCardDetailById(cardId = cardId)

}