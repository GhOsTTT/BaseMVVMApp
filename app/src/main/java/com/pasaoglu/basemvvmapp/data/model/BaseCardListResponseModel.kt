package com.pasaoglu.basemvvmapp.data.model

import com.google.gson.annotations.SerializedName

data class BaseCardListResponseModel(@SerializedName("cards") var cards: List<CardListResponseModel>)
