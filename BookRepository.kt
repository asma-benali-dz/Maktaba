package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * Repository for managing book data
 * This follows the Repository pattern to abstract data sources
 */
class BookRepository {

    /**
     * TODO for Students (TP1 - Exercise 1):
     * Complete the book information for each book in the list below.
     * Add the following information for each book:
     * - isbn: Use a valid ISBN-13 format (e.g., "978-3-16-148410-0")
     * - nbPages: Add the actual number of pages
     *
     * Example:
     * Book(
     *     isbn = "978-0-13-468599-1",
     *     title = "Clean Code",
     *     nbPages = 46
     * )
     */
    private val booksList = listOf(
        Book(isbn = "9780132764094", title = "Clean Code", nbPages = 496),
        Book(isbn = "9782322017225", title = "The Pragmatic Programmer", nbPages = 540),
        Book(isbn = "9781484236215", title = " Design Patterns", nbPages = 207),
        Book(isbn = "9780596008741", title = "Refactoring", nbPages = 220),
        Book(isbn = "9780596007126", title = "Head First Design Patterns", nbPages =638),
        Book(isbn = "9780134171500", title = "Android Programming: The Big Nerd Ranch Guide", nbPages = 600),
        Book(isbn = "9780136870487", title = "Kotlin Programming: The Big Nerd Ranch Guide", nbPages = 600),
        Book(isbn = "9781491903117", title = "Designing Data-Intensive Applications", nbPages = 616),
        Book(isbn = "9780132778046", title = "Effective Java", nbPages = 384),
        Book(isbn = "9781430263234", title = "Android Cookbook", nbPages = 772),
        Book(isbn = "9781492056300", title = "Fluent Python", nbPages = 1014),
        Book(isbn = "9783031161278", title = "Learning SQL", nbPages = 291),
        Book(isbn = "9781394217274", title = "Automate the Boring Stuff with Python", nbPages = 272),
        Book(isbn = "9781484244678", title = "Learning Kotlin", nbPages = 508),
        Book(isbn = "9781638353690", title = "Kotlin in Action", nbPages = 360),
    )

    /**
     * TODO for Students (TP1 - Exercise 2):
     * Add 5 more books to the list above.
     * Choose books related to Computer Science, Programming, or any topic you like.
     * Remember to include complete information (ISBN, title, nbPages).
     *
     * Tip: You can find ISBN numbers for books on:
     * - Google Books
     * - Amazon
     * - GoodReads
     */

    /**
     * Get all books from the repository
     * @return List of all books
     */
    fun getAllBooks(): List<Book> {
        return booksList
    }

    /**
     * Get a book by ISBN
     * @param isbn The ISBN of the book to find
     * @return The book if found, null otherwise
     */
    fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }

