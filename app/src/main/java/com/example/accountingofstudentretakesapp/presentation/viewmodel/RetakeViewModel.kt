package com.example.accountingofstudentretakesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.accountingofstudentretakesapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class RetakeViewModel(
   // val filterUseCase: FilterNobelPrizeUseCase,
    private val authRepository: AuthRepository,
   // private val addFavouriteUseCase: AddFavouriteUseCase,
   // private val removeFavouriteUseCase: RemoveFavouriteUseCase,
   // private val getProfileUseCase: GetProfileUseCase,
   // private val showFavoriteUseCase: ShowFavoriteUseCase
) : ViewModel() {
  //  private val _uiState = MutableStateFlow<UiState<List<NobelPrize>>>(UiState.Loading)
    // val uiState: StateFlow<UiState<List<NobelPrize>>> = _uiState


    init {

    }


    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}

