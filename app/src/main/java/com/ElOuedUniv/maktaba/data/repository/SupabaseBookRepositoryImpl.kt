package com.ElOuedUniv.maktaba.data.repository

import android.util.Log
import com.ElOuedUniv.maktaba.data.model.Book
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Singleton
class SupabaseBookRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : BookRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllBooks(): Flow<List<Book>> = _refreshTrigger
        .flatMapLatest {
            flow {
                try {
                    val response = supabaseClient.postgrest.from("Books").select()
                    val books = response.decodeList<Book>()
                    emit(books)
                } catch (e: Exception) {
                    Log.e("SupabaseRepo", "Fetch error: ${e.message}")
                    emit(emptyList())
                }
            }
        }
        .flowOn(Dispatchers.IO)

    override fun getBookByIsbn(isbn: String): Book? = null

    suspend fun getRemoteBookByIsbn(isbn: String): Book? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = supabaseClient.postgrest.from("Books")
                .select { filter { eq("Isbn", isbn) } }
            response.decodeSingleOrNull<Book>()
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Get book error: ${e.message}")
            null
        }
    }

    override fun addBook(book: Book) {
        repositoryScope.launch {
            try {
                supabaseClient.postgrest.from("Books").insert(book)
                _refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                Log.e("SupabaseRepo", "Insert error: ${e.message}")
            }
        }
    }

    override suspend fun deleteBook(isbn: String): Boolean = withContext(Dispatchers.IO) {
        try {
            supabaseClient.postgrest.from("Books").delete { filter { eq("Isbn", isbn) } }
            _refreshTrigger.emit(Unit)
            true
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Delete error: ${e.message}")
            false
        }
    }

    override suspend fun addBookWithFiles(
        title: String,
        isbn: String,
        nbPages: Int,
        imageBytes: ByteArray?,
        pdfBytes: ByteArray?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            var uploadedImageUrl: String? = null
            val projectRef = "ppollwbvlgcedfsaeosd"

            if (imageBytes != null && imageBytes.isNotEmpty()) {
                val fileName = "cover_${System.currentTimeMillis()}_$isbn.jpg"
                try {
                    val bucket = supabaseClient.storage.from("book_covers")
                    bucket.upload(fileName, imageBytes) {
                        upsert = true
                    }
                    uploadedImageUrl = "https://$projectRef.supabase.co/storage/v1/object/public/book_covers/$fileName"
                } catch (e: Exception) {
                    Log.e("SupabaseRepo", "UPLOAD FAILED: ${e.message}")
                }
            }

            val newBook = Book(
                isbn = isbn,
                title = title,
                nbPages = nbPages,
                imageUrl = uploadedImageUrl,
                pdfUrl = null
            )

            return@withContext try {
                supabaseClient.postgrest.from("Books").insert(newBook)
                _refreshTrigger.emit(Unit)
                true
            } catch (e: Exception) {
                Log.e("SupabaseRepo", "DATABASE INSERT FAILED: ${e.message}")
                false
            }

        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Unexpected error: ${e.message}")
            false
        }
    }
}
