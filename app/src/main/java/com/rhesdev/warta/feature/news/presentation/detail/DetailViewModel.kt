package com.rhesdev.warta.feature.news.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetNewsByLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Detail state holder loading one article by link.
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNewsByLinkUseCase: GetNewsByLinkUseCase
) : ViewModel() {

    private val newsLink: String = savedStateHandle["newsLink"] ?: ""

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadNewsDetail()
    }

    fun retry() {
        loadNewsDetail()
    }

    private fun loadNewsDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val news = getNewsByLinkUseCase(newsLink)
                _uiState.update { it.copy(news = news, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
