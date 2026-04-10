package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor() : BookRepository {

    private val _booksList = mutableListOf(
        Book(isbn = "9780132350884", title = "Clean Code", nbPages = 464),
        Book(isbn = "9780135957059", title = "The Pragmatic Programmer", nbPages = 352),
        Book(isbn = "9780134757599", title = "Refactoring", nbPages = 448),
        Book(isbn = "9780596007126", title = "Head First Design Patterns", nbPages = 638),
        Book(isbn = "9780451524935", title = "1984", nbPages = 328),
        Book(isbn = "9780547928227", title = "The Hobbit", nbPages = 320),
        Book(isbn = "9780735211292", title = "Atomic Habits", nbPages = 320),
        Book(isbn = "9780374275631", title = "Thinking, Fast and Slow", nbPages = 499),
        Book(isbn = "9780062316097", title = "Sapiens", nbPages = 443),
        Book(isbn = "9781455586691", title = "Deep Work", nbPages = 304),
        Book(isbn = "9780134171500", title = "Android Programming", nbPages = 600),
        Book(isbn = "9780136870487", title = "Kotlin Programming", nbPages = 600),
        Book(isbn = "9781491903117", title = "Designing Data-Intensive Applications", nbPages = 616),
        Book(isbn = "9781617293290", title = "Kotlin in Action", nbPages = 360),
        Book(isbn = "9780134685991", title = "Effective Java", nbPages = 416)
    )

    private val booksFlow = MutableSharedFlow<List<Book>>(replay = 1).apply {
        tryEmit(_booksList.toList())
    }
    
    override fun getAllBooks(): Flow<List<Book>> = flow {
        delay(1000) // Simulate slight network delay
        emitAll(booksFlow)
    }

    override fun getBookByIsbn(isbn: String): Book? {
        return _booksList.find { it.isbn == isbn }
    }

    override fun addBook(book: Book) {
        _booksList.add(book)
        booksFlow.tryEmit(_booksList.toList())
    }
}
