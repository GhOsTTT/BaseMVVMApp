package com.pasaoglu.basemvvmapp.data.model

import com.google.gson.annotations.SerializedName


data class CardListResponseModel(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("imageUrlHiRes")
    var imageUrlHiRes: String? = null,
    @SerializedName("nationalPokedexNumber")
    var nationalPokedexNumber: String? = null

){
    fun getPosterImagePath():String?{
       // return if (poster_path.isNullOrBlank()) null else "${BuildConfig.IMAGE_URL}$poster_path"
        return if (imageUrl.isNullOrBlank()) null else imageUrlHiRes
    }

    fun getBackDropImagePath():String?{
       // return if (backdropPath.isNullOrBlank()) null else "${BuildConfig.IMAGE_URL}$backdropPath"
        return if (imageUrl.isNullOrBlank()) null else imageUrlHiRes

    }
}