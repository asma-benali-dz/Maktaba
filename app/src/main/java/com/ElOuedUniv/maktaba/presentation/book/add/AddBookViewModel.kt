package com.ElOuedUniv.maktaba.presentation.book.add

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddBookUiAction, context: Context? = null) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
                validateInputs()
            }
            is AddBookUiAction.OnIsbnChange -> {
                val filteredIsbn = action.isbn.filter { it.isDigit() }.take(13)
                _uiState.update { it.copy(isbn = filteredIsbn) }
                validateInputs()
            }
            is AddBookUiAction.OnPagesChange -> {
                val filteredPages = action.pages.filter { it.isDigit() }
                _uiState.update { it.copy(nbPages = filteredPages) }
                validateInputs()
            }
            AddBookUiAction.OnAddClick -> {
                if (_uiState.value.isFormValid && context != null) {
                    addBookWithImage(context)
                }
            }
        }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
        validateInputs()
    }

    private fun validateInputs() {
        val state = _uiState.value
        val isFormValid = state.title.isNotBlank() &&
                state.isbn.length == 13 &&
                state.nbPages.isNotBlank() &&
                state.imageUri != null
        _uiState.update { it.copy(isFormValid = isFormValid) }
    }

    private fun addBookWithImage(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val state = _uiState.value

                val imageBytes = try {
                    state.imageUri?.let { uri ->
                        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    }
                } catch (e: Exception) {
                    Log.e("AddBookVM", "Error reading image: ${e.message}")
                    null
                }

                if (imageBytes == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Please select a valid image") }
                    return@launch
                }

                val success = repository.addBookWithFiles(
                    title = state.title,
                    isbn = state.isbn,
                    nbPages = state.nbPages.toIntOrNull() ?: 0,
                    imageBytes = imageBytes,
                    pdfBytes = null
                )

                if (success) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Server error: Failed to save book") }
                }
            } catch (e: Exception) {
                Log.e("AddBookVM", "Unexpected error: ${e.message}")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Unknown error occurred") }
            }
        }
    }
}
