package com.pasaoglu.basemvvmapp.data.network



import com.pasaoglu.basemvvmapp.data.model.BaseCardDetailResponseModel
import com.pasaoglu.basemvvmapp.data.model.BaseCardListResponseModel
import retrofit2.http.GET
import retrofit2.http.Path


interface Service {

    @GET("cards")
    suspend fun getListWithQuery(/*@Query("query")query:String, @Query("page")page:Int*/): BaseCardListResponseModel

    @GET("cards/{id}")
    suspend fun getCardDetailById(@Path("id")cardId:String): BaseCardDetailResponseModel

}