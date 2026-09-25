package com.rhesdev.warta.feature.news.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetArticleBodyUseCase
import com.rhesdev.warta.feature.news.domain.usecase.GetNewsByLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Detail state holder loading one article by link from the activity intent.
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getNewsByLinkUseCase: GetNewsByLinkUseCase,
    private val getArticleBodyUseCase: GetArticleBodyUseCase
) : ViewModel() {

    private var newsLink: String = ""

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadNewsDetail(link: String) {
        if (link != newsLink) {
            newsLink = link
            loadNewsDetail()
        }
    }

    fun retry() {
        loadNewsDetail()
    }

    private fun loadNewsDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val news = getNewsByLinkUseCase(newsLink)
                _uiState.update {
                    it.copy(
                        news = news,
                        isLoading = false,
                        fullText = news?.content?.takeIf { content -> content.isNotBlank() }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun loadFullText() {
        val state = _uiState.value
        if (state.fullText != null || state.isLoadingBody) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingBody = true, bodyError = null) }
            try {
                val body = getArticleBodyUseCase(newsLink)
                if (body != null) {
                    _uiState.update { it.copy(fullText = body, isLoadingBody = false) }
                } else {
                    _uiState.update {
                        it.copy(bodyError = "Versi lengkap tidak tersedia", isLoadingBody = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(bodyError = e.message, isLoadingBody = false)
                }
            }
        }
    }
}