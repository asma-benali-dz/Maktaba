package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("Isbn") val isbn: String,
    @SerialName("Title") val title: String,
    @SerialName("Nb_page") val nbPages: Int,
    @SerialName("Image_url") val imageUrl: String? = null,
    @SerialName("Pdf_url") val pdfUrl: String? = null
)
