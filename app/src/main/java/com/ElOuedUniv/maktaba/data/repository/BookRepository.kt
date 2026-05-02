package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getAllBooks(): Flow<List<Book>>

    fun getBookByIsbn(isbn: String): Book?

    fun addBook(book: Book)

    suspend fun deleteBook(isbn: String): Boolean

    suspend fun addBookWithFiles(
        title: String,
        isbn: String,
        nbPages: Int,
        imageBytes: ByteArray?,
        pdfBytes: ByteArray?
    ): Boolean
}