package com.itfitness.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itfitness.app.data.models.*
import com.itfitness.app.data.repository.MockRepository
import com.itfitness.app.data.repository.mockRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val repository: MockRepository = mockRepository) : ViewModel() {

    val trainers: StateFlow<List<Trainer>> = repository.getTrainers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleTrainerLike(trainerId: String) {
        repository.toggleTrainerLike(trainerId)
    }

    fun getStories(): List<Story> = repository.getStories()
    fun getBanners(): List<Banner> = repository.getBanners()
    fun getTariffs(): List<Tariff> = repository.getTariffs()
    fun getGroupClasses(): List<GroupClass> = repository.getGroupClasses()
    fun getUserProfile(): UserProfile = repository.getUserProfile()
    fun getPaymentHistory(): List<PaymentTransaction> = repository.getPaymentHistory()
}
