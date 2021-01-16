package com.pasaoglu.basemvvmapp.data.model

import com.google.gson.annotations.SerializedName

data class CardDetailResponseModel(
    @SerializedName("text")
    var overview: List<String?>? = null,
    @SerializedName("hp")
    var hp: String? = null,
    @SerializedName("attacks")
    var attacks: List<AttackResponseModel>? = null
)