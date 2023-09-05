package com.bkcoding.contactsyncapp.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkcoding.contactsyncapp.utils.Result
import com.bkcoding.contactsyncapp.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


private const val MIN_COUNT_TO_SEARCH = 1
private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_QUERY = "searchQuery"

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchContactUiState: StateFlow<SearchContactUiState> =
        /**
         * return count of contacts
         */
        flowOf(10)
            .flatMapLatest { count ->
                if (count < MIN_COUNT_TO_SEARCH) {
                    flowOf(SearchContactUiState.SearchContactNotReady)
                } else {
                    searchQuery.flatMapLatest { query ->
                        /**
                         * fetch contacts and convert them to flow of result
                         */
                        flowOf(
                            if (query.length < SEARCH_QUERY_MIN_LENGTH) 0 else 1
                        ).asResult()
                            .map {
                                when (it) {
                                    is Result.Success -> SearchContactUiState.Success(listOf())
                                    is Result.Loading -> SearchContactUiState.Loading
                                    is Result.Error -> SearchContactUiState.Failed
                                }
                            }
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SearchContactUiState.Loading
            )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }
}

sealed interface SearchContactUiState {
    object Loading : SearchContactUiState
    object Failed : SearchContactUiState

    /**
     * Meaning sync in progress database is empty
     */
    object SearchContactNotReady : SearchContactUiState
    data class Success(
        val contacts: List<String>
    ) : SearchContactUiState
}