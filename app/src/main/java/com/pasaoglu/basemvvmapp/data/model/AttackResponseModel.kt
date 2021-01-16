package com.pasaoglu.basemvvmapp.data.model

import com.google.gson.annotations.SerializedName

data class AttackResponseModel(
    @SerializedName("damage")
    var damage: Int? = null,
    @SerializedName("name")
    var name: String? = null
)
