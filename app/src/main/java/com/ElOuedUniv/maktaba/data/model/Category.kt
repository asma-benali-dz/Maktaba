package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("id") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Description") val description: String,
    @SerialName("Icon_res") val iconRes: Int
)